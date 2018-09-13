package clientnetty;/**
 * Created by DELL on 2018/9/13.
 */

import group.im1.message.Message;
import messageHandler.ClientMessageHandler;

/**
 * user is lwb
 **/


public class TextHandler extends ClientMessageHandler {
    @Override
    public void process(Message msg) {
        System.out.println("收到文本消息"+msg);
    }
}
