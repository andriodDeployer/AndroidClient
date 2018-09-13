package messageHandler;/**
 * Created by DELL on 2018/9/6.
 */


import group.im1.message.Message;

/**
 * user is lwb
 **/

//组装饰
public class GroupMessageHandler extends DecoratorMessageHandler {
    //private final InternalLogger logger = InternalLoggerFactory.getInstance(GroupMessageHandler.class);

    public GroupMessageHandler(ClientMessageHandler handler) {
        super(handler);
    }

    @Override
    public void process(Message msg) {
     //   logger.info("client receiver group message: {}",msg);
        handler.process(msg);
    }
}
