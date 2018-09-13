package messageHandler;/**
 * Created by DELL on 2018/9/9.
 */


import group.im1.message.Message;
import message.MessageHandler;

/**
 * user is lwb
 **/


public abstract class ClientMessageHandler extends MessageHandler {

    public abstract void process(Message msg);
}
