package processor;

/**
 * Created by DELL on 2018/8/30.
 */


import controller.DefaultSendFuture;
import io.netty.channel.Channel;
import message.GRequest;
import message.GResponse;
import payload.GRequestPayload;
import payload.GResponsePayload;
import threadfactory.ConsumerExecutors;

/**
 * user is lwb
 **/


public class DefaultClientProcessor extends AbstractClientProcessor {
   // private final InternalLogger logger = InternalLoggerFactory.getInstance(DefaultClientProcessor.class);
    public DefaultClientProcessor(){
        this(ConsumerExecutors.executor());
    }

    public DefaultClientProcessor(CloseableExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void handleRequest(Channel channel, GRequestPayload payload) {
      //  logger.info("client received message {}",payload.toString());
        RequestMessageTask task = new RequestMessageTask(this, channel,new GRequest(payload));
        if(executor == null){
            task.run();
        }else{
            executor.execute(task);
        }
    }

    @Override
    public void handleResonse(Channel channel, GResponsePayload payload) {
        //直接再io线程中处理
       // logger.info("client recive response id: {}",payload.responseId());
        DefaultSendFuture.received(channel,new GResponse(payload));

    }




}
