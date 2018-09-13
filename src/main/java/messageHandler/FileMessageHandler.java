package messageHandler;/**
 * Created by DELL on 2018/9/6.
 */


import group.im1.message.FileMessage;
import group.im1.message.GroupMessage;
import group.im1.message.Message;

/**
 * user is lwb
 **/


public class FileMessageHandler extends ClientMessageHandler {
   // InternalLogger logger = InternalLoggerFactory.getInstance(FileMessageHandler.class);

    @Override
    public void process(Message msg) {

        FileMessage message ;
        if(msg instanceof GroupMessage){
            message = ((FileMessage)((GroupMessage) msg).getMessage());
        }else{
            message = (FileMessage) msg;
        }
       // logger.info("client recive file message : {},file length is {}.",msg,message.content().length);
    }
}
