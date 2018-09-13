package message;


import controller.MessingSendListener;
import group.im1.message.Message;
import io.netty.channel.Channel;

/**
 * Created by DELL on 2018/8/31.
 */
public interface EndPoint {
    void sentMessage(Message message, Channel jChannel, MessingSendListener listener);
}
