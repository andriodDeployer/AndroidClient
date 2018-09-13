package messageHandler;
/**
 * Created by DELL on 2018/9/6.
 */


import group.im1.message.Message;

/**
 * user is lwb
 **/


public class TextMessageHandler extends ClientMessageHandler {
    //private InternalLogger logger = InternalLoggerFactory.getInstance(TextMessageHandler.class);

    @Override
    public void process(Message msg) {
        System.out.println("收到客户端消息");
    }


}
