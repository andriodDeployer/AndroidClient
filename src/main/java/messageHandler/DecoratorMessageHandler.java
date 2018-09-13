package messageHandler;
/**
 * Created by DELL on 2018/9/6.
 */


import group.im1.message.Message;

/**
 * user is lwb
 **/


public class DecoratorMessageHandler extends ClientMessageHandler {

    protected final ClientMessageHandler handler;

    public DecoratorMessageHandler(ClientMessageHandler handler) {
        this.handler = handler;
    }


    @Override
    public void process(Message msg) {
        handler.process(msg);
    }
}
