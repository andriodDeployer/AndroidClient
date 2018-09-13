package processor;/**
 * Created by DELL on 2018/8/30.
 */


import group.im1.message.Message;
import io.netty.channel.Channel;
import message.GRequest;
import message.MessageTask;
import messageHandler.ClientMessageHandler;

/**
 * user is lwb
 **/


public class RequestMessageTask extends MessageTask {

   // private static final InternalLogger logger = InternalLoggerFactory.getInstance(RequestMessageTask.class);

    public RequestMessageTask(AbstractClientProcessor processor, Channel jChannel, GRequest request){
        super(jChannel,processor,request);
    }

    @Override
    protected void processMessage(Message msg) {
        AbstractClientProcessor clientProcessor = (AbstractClientProcessor)processor;
        ((ClientMessageHandler)clientProcessor.getMessageHandler(msg.type())).process(msg);
    }
}
