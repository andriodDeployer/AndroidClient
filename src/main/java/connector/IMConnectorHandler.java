package connector;/**
 * Created by DELL on 2018/8/30.
 */


import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import payload.GRequestPayload;
import payload.GResponsePayload;
import processor.Processor;

/**
 * user is lwb
 **/

@ChannelHandler.Sharable
public class IMConnectorHandler extends ChannelInboundHandlerAdapter {


    private Processor processor;

    public void processor(Processor processor) {
        this.processor = processor;
    }

    public Processor processor(){
        return processor;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel channel = ctx.channel();
        if(msg instanceof GRequestPayload){
            processor.handleRequest(channel, (GRequestPayload) msg);
        } else if (msg instanceof GResponsePayload){
            processor.handleResonse(channel, (GResponsePayload) msg);
        } else {
            System.out.println("Unexcepted message type received: {}, channel {}");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        processor.handleActive(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        processor.handleInactive(ctx.channel());
        super.channelInactive(ctx);
    }
}
