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

package threadfactory;


import processor.CloseableExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Provide a {@link ThreadPoolExecutor} implementation of executor.
 * Thread pool executor factory.
 *
 * jupiter
 * org.jupiter.rpc.executor
 *
 * @author jiachun.fjc
 */
public class ThreadPoolExecutorFactory extends AbstractExecutorFactory {

 //   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadPoolExecutorFactory.class);

    @Override
    public CloseableExecutor newExecutor(Target target, String name) {
        //ThreadPoolExecutor可以理解对ThreadFactory的一种封装，或者说管理，将ThreadFactory产生的Thread进行管理，比如说什么时候创建，创建多少个等。
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                coreWorkers(target),
                maxWorkers(target),
                120L,
                TimeUnit.SECONDS,
                workQueue(target),
                threadFactory(name));

        return new CloseableExecutor() {

            @Override
            public void execute(Runnable r) {
                executor.execute(r);//在业务线程中调用
            }

            @Override
            public void shutdown() {
              //  logger.warn("ThreadPoolExecutorFactory#{} shutdown.", executor);
                executor.shutdownNow();
            }
        };
    }

    private BlockingQueue<Runnable> workQueue(Target target) {
        BlockingQueue<Runnable> workQueue = null;
        int queueCapacity = queueCapacity(target);
        workQueue = new LinkedBlockingQueue<>(queueCapacity);
        return workQueue;
    }


    enum WorkQueueType {
        LINKED_BLOCKING_QUEUE,
        ARRAY_BLOCKING_QUEUE;

       static WorkQueueType parse(String name) {
            for (WorkQueueType type : values()) {
                if (type.name().equalsIgnoreCase(name)) {
                    return type;
                }
            }
            return null;
        }
    }
}
