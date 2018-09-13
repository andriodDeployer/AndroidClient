package messageHandler;/**
 * Created by DELL on 2018/9/6.
 */


import group.im1.message.Message;

/**
 * user is lwb
 **/


public class AuthMessageHandler extends ClientMessageHandler {
   // private InternalLogger logger = InternalLoggerFactory.getInstance(AuthMessageHandler.class);
    @Override
    public void process(Message msg) {
        //logger.info("client recive auth message : {}",msg);
        System.out.println("auth message process");
    }
}
