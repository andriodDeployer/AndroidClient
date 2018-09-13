package connector;


import io.netty.channel.Channel;

/**
 * Created by DELL on 2018/9/13.
 */
public interface ChannelGroup {

    Channel next();
    boolean add(Channel channel);
    boolean isEmpty();
    boolean isAvailable();
    boolean waitForAvailable(long timeoutMillis);
    boolean remove(Channel channel);
}
