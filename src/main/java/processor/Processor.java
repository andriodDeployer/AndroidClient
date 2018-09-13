package processor;

import io.netty.channel.Channel;
import payload.GRequestPayload;
import payload.GResponsePayload;


/**
 * Created by DELL on 2018/8/30.
 */
public interface Processor {

    void handleRequest(Channel channel, GRequestPayload payload);
    void handleResonse(Channel channel, GResponsePayload payload);
    void handleInactive(Channel channel);
    void handleActive(Channel channel);
    void shutdown();

}
