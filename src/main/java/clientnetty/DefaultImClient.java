package clientnetty;/**
 * Created by DELL on 2018/9/13.
 */


import connector.ChannelGroup;
import connector.NettyConnector;
import controller.MessingSendListener;
import group.im1.message.AuthMessage;
import group.im1.message.Message;
import io.netty.channel.Channel;
import message.AbstractEndPoint;
import messageHandler.ClientMessageHandler;
import processor.AbstractClientProcessor;
import processor.DefaultClientProcessor;

import java.net.InetSocketAddress;

/**
 * user is lwb
 **/


public class DefaultImClient extends AbstractEndPoint{

    private final int port;
    private final String host;

    public DefaultImClient(String host, int port) {
        this.port = port;
        this.host = host;
    }

    private AbstractClientProcessor clientProcessor;

    private NettyConnector connector;
    public DefaultImClient withConnector(NettyConnector connector, AbstractClientProcessor.ActiveAndInactiveListener listener){
        this.connector = connector;
        if(connector.processor() == null){
            clientProcessor = new DefaultClientProcessor().withActiveAndInactiveListener(listener);
            connector.withProcessor(clientProcessor);
        }
        return this;
    }

    void connecteServe(){
        connector.connect(new InetSocketAddress(host,port));
    }


    public void auth(String id, final MessingSendListener listener){
        Message msg = new AuthMessage(id);
        //logger.debug("client begin send auth message: {}.",msg);
        sentMessage(msg, listener);
    }

    public void sentMessage(Message message,final MessingSendListener listener) {
        ChannelGroup group = connector.group();
        Channel channel = group.next();
        sentMessage(message,channel,listener);
    }

    public void addMessageHandler(byte type, ClientMessageHandler clientMessageHandler){
        clientProcessor.addMessageHandler(type,clientMessageHandler);
    }



}
