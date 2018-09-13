package processor;/**
 * Created by DELL on 2018/8/30.
 */


import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import message.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * user is lwb
 **/


public abstract class AbstractProcessor implements Processor{

    private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractProcessor.class);
    protected  CloseableExecutor executor; //业务线程
    protected static Map<Byte, MessageHandler> handlers = new HashMap<Byte, MessageHandler>();

    @Override
    public void shutdown() {
        if (executor != null){
            executor.shutdown();
        }
    }

    protected void addMessageHandler(Byte type,MessageHandler handler){
        handlers.put(type,handler);
    }

    public MessageHandler getMessageHandler(byte key){
        return handlers.get(key);
    }
}
