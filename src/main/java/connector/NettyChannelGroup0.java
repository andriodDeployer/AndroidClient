/*
 * Copyright (c) 2015 The Jupiter Project
 *
 * Licensed under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package connector;


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * jupiter
 * org.jupiter.transport.netty.channel
 *
 * @author jiachun.fjc
 */
public class NettyChannelGroup0 implements ChannelGroup {

    private static long LOSS_INTERVAL =  TimeUnit.MINUTES.toMillis(5);

    private static int DEFAULT_SEQUENCE_STEP = (Runtime.getRuntime().availableProcessors() << 3) + 1;

//    private static final AtomicReferenceFieldUpdater<CopyOnWriteArrayList, Object[]> channelsUpdater =
//            AtomicUpdater.newAtomicReferenceFieldUpdater(CopyOnWriteArrayList.class, Object[].class, "array");

    private static final AtomicIntegerFieldUpdater<NettyChannelGroup0> signalNeededUpdater =
            AtomicIntegerFieldUpdater.newUpdater(NettyChannelGroup0.class, "signalNeeded");

    private final ConcurrentLinkedQueue<Runnable> waitAvailableListeners = new ConcurrentLinkedQueue<>();


    private final List<Channel> channels = new ArrayList<Channel>();

    // 连接断开时自动被移除
    private final ChannelFutureListener remover = new ChannelFutureListener() {

        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            remove(future.channel());
        }
    };

    public boolean remove(Channel channel) {
        boolean removed = channels.remove(channel);
        if (removed) {
            timestamp = System.currentTimeMillis(); // reset timestamp

            if (channels.isEmpty()) {
                deadlineMillis = System.currentTimeMillis() + LOSS_INTERVAL;
            }
        }
        return removed;
    }

    private final AtomicInteger sequence = new AtomicInteger();


    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notifyCondition = lock.newCondition();
    // attempts to elide conditional wake-ups when the lock is uncontended.
    @SuppressWarnings("all")
    private volatile int signalNeeded = 0; // 0: false, 1: true

    private volatile boolean connecting = false;

    private volatile int capacity = Integer.MAX_VALUE;
    private volatile long timestamp = System.currentTimeMillis();
    private volatile long deadlineMillis = -1;



    @Override
    public Channel next() {
        for (;;) {
            Object[] elements = channels.toArray();
            int length = elements.length;
            if (length == 0) {
                if (waitForAvailable(1000)) { // wait a moment
                    continue;
                }
                throw new IllegalStateException("No channel");
            }
            if (length == 1) {
                return (Channel) elements[0];
            }

            int index = sequence.incrementAndGet();

            return (Channel) elements[index % length];
        }
    }


    @Override
    public boolean isEmpty() {
        return channels.isEmpty();
    }

    @Override
    public boolean add(Channel channel) {
        boolean added = channels.add(channel);
        if (added) {
            timestamp = System.currentTimeMillis(); // reset timestamp

            channel.closeFuture().addListener(remover);
            deadlineMillis = -1;

            if (signalNeededUpdater.getAndSet(this, 0) != 0) { // signal needed: true
                final ReentrantLock _look = lock;
                _look.lock();
                try {
                    notifyCondition.signalAll(); // must signal all
                } finally {
                    _look.unlock();
                }
            }

            notifyListeners();
        }
        return added;
    }


    @Override
    public boolean isAvailable() {
        return !channels.isEmpty();
    }

    @Override
    public boolean waitForAvailable(long timeoutMillis) {
        boolean available = isAvailable();
        if (available) {
            return true;
        }
        long remains = TimeUnit.MILLISECONDS.toNanos(timeoutMillis);

        final ReentrantLock _look = lock;
        _look.lock();
        try {
            // avoid "spurious wakeup" occurs
            while (!(available = isAvailable())) {
                signalNeeded = 1; // set signal needed to true
                if ((remains = notifyCondition.awaitNanos(remains)) <= 0) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            //ThrowUtil.throwException(e);
        } finally {
            _look.unlock();
        }

        return available;
    }


    void notifyListeners() {
        for (;;) {
            Runnable listener = waitAvailableListeners.poll();
            if (listener == null) {
                break;
            }
            listener.run();
        }
    }
}
