package connector;/**
 * Created by DELL on 2018/9/13.
 */

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.HashedWheelTimer;
import io.netty.util.concurrent.DefaultThreadFactory;
import processor.Processor;

import java.net.SocketAddress;
import java.util.concurrent.ThreadFactory;

/**
 * user is lwb
 **/


public class NettyConnector {

    private Bootstrap bootStrap;
    private EventLoopGroup worker;
    private ChannelGroup group;
    private final ConnectorIdleStateTrigger idleStateTrigger = new ConnectorIdleStateTrigger();
    private  int nWorkers = Runtime.getRuntime().availableProcessors() << 1;
    protected final HashedWheelTimer timer = new HashedWheelTimer(new DefaultThreadFactory("connector.timer", true));
    private final ChannelOutboundHandler encoder = new IMMessageEncoder();
    private final IMConnectorHandler handler = new IMConnectorHandler();
    private SocketAddress address;


    public NettyConnector(){
        init();
    }

    public NettyConnector withProcessor(Processor processor) {
       handler.processor(processor);
        return this;
    }

    public Processor processor(){
        return handler.processor();
    }


    protected void init(){
        ThreadFactory workerFactory = workerThreadFactory("andorid.connnctor");
        worker =  new NioEventLoopGroup(nWorkers, workerFactory);
        bootStrap = new Bootstrap().group(worker)
                                    .channel(NioSocketChannel.class);
    }

    public ChannelFuture connect(SocketAddress address){
        this.address = address;
        group = group();

        final ConnectionWatchdog dog = new ConnectionWatchdog(bootStrap,timer,address,group,this) {
            @Override
            public ChannelHandler[] handlers() {
                return new ChannelHandler[]{
                        this,
                        new IdleStateHandler(0, 30,0),
                        idleStateTrigger,
                        new IMMessageDecoder(),
                        encoder,
                        handler
                };
            }
        };

        ChannelFuture future = null;
        try {
            synchronized (bootStrap){
                bootStrap.handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(dog.handlers());
                    }
                });
                future = bootStrap.connect(address);
                future.addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        boolean succeed = f.isSuccess();
                        if (!succeed) {
                            Channel channel = f.channel();
                            ChannelPipeline entries = channel.pipeline();
                            entries.fireChannelInactive();
                        }
                    }
                });
            }
            future.sync();
        }catch (Throwable t){
            //throw new ConnectFailedException("Connects to [" + address + "] fails", t);
            System.out.println("Connects to [" + address + "] fails");
        }
        return future;
    }

    protected ThreadFactory workerThreadFactory(String name) {
        return new DefaultThreadFactory(name, Thread.MAX_PRIORITY);
    }

    public ChannelGroup group(){
        if(group == null){
            group = new NettyChannelGroup0();
        }
        return group;
    }
}
