package processor;/**
 * Created by DELL on 2018/8/31.
 */


import connector.JProtocolHeader;
import io.netty.channel.Channel;
import messageHandler.*;

/**
 * user is lwb
 **/


public abstract class AbstractClientProcessor extends AbstractProcessor{

    static {
        ClientMessageHandler textHandler = new TextMessageHandler();
        ClientMessageHandler fileHandler = new FileMessageHandler();
        handlers.put(JProtocolHeader.TEXT,textHandler);
        handlers.put(JProtocolHeader.FILE,fileHandler);
        handlers.put(JProtocolHeader.AUTH,new AuthMessageHandler());
        handlers.put(JProtocolHeader.GROUP_TEXT,new GroupMessageHandler(textHandler));
        handlers.put(JProtocolHeader.GROUP_FILE,new GroupMessageHandler(fileHandler));
    }

    private ActiveAndInactiveListener activeAndInactiveListener;
    public AbstractClientProcessor withActiveAndInactiveListener(ActiveAndInactiveListener activeAndInactiveListener){
        this.activeAndInactiveListener = activeAndInactiveListener;
        return this;
    }

    @Override
    public void handleActive(Channel channel) {
        if(activeAndInactiveListener != null){
            activeAndInactiveListener.handleActive(channel);
        }

    }

    @Override
    public void handleInactive(Channel channel) {
        if(activeAndInactiveListener!=null){
            activeAndInactiveListener.handleInActive(channel);
        }

    }

    public void addMessageHandler(byte type, ClientMessageHandler messageHandler){
        handlers.put(type,messageHandler);
    }


    public interface ActiveAndInactiveListener{
        void handleActive(Channel jChannel);
        void handleInActive(Channel jChannel);
    }



}
