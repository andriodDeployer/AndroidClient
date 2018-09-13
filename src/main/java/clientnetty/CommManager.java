package clientnetty;/**
 * Created by DELL on 2018/9/13.
 */

import connector.NettyConnector;
import controller.MessingSendListener;
import io.netty.channel.Channel;
import messageHandler.ClientMessageHandler;
import processor.AbstractClientProcessor;


/**
 * user is lwb
 **/


public class CommManager {

    private static CommManager instance = null;

    public static CommManager getInstance(){
        if(instance == null){
            synchronized (CommManager.class){
                if(instance == null){
                    instance = new CommManager();
                }
            }
        }
        return instance;
    }

    private DefaultImClient cliet;

    private CommManager(){
        cliet = new DefaultImClient("192.168.1.123",8888).withConnector(new NettyConnector(), new AbstractClientProcessor.ActiveAndInactiveListener() {
            @Override
            public void handleActive(Channel jChannel) {
                if(activeAndInactiveListener!=null)
                    activeAndInactiveListener.connected(jChannel);
            }

            @Override
            public void handleInActive(Channel jChannel) {
                if(activeAndInactiveListener!=null)
                    activeAndInactiveListener.unConnected(jChannel);
            }
        });
    }

    private ActiveAndInactiveListener activeAndInactiveListener;
    public void addListener(ActiveAndInactiveListener listener){
        this.activeAndInactiveListener = listener;
    }

    public interface ActiveAndInactiveListener{
        void connected(Channel jChannel);
        void unConnected(Channel jChannel);
    }

    public void connnecteSever(){
        cliet.connecteServe();
    }

    public void auth(String idKey, MessingSendListener listener){
        cliet.auth(idKey,listener);
    }

    public void addHandler(byte type, ClientMessageHandler handler){
        cliet.addMessageHandler(type,handler);
    }

}
