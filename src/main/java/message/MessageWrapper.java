package message;/**
 * Created by DELL on 2018/9/6.
 */


import io.netty.channel.Channel;
import javafx.util.Pair;
import payload.GRequestPayload;
import serialization.Serializer;
import serialization.SerializerFactory;
import serialization.SerializerType;
import group.im1.message.Message;

/**
 * user is lwb
 **/


public class MessageWrapper {
    private Serializer serializer = SerializerFactory.getSerializer(SerializerType.JAVA.value());;
    private final Message message;
    private Pair<String,Channel> receiverChannel;

    public MessageWrapper(Message message, Pair<String,Channel> channel){
        this.message = message;
        this.receiverChannel = channel;
    }

    public Message getMessage() {
        return message;
    }

    public Pair<String,Channel> getReceiverChannel() {
        return receiverChannel;
    }

    private GRequest createRequest(Message message) {
        GRequest request = new GRequest(new GRequestPayload());
        byte s_code = serializer.code();
        byte[] bytes = serializer.writeObject(message);
        request.bytes(s_code,bytes);
        request.messageType(message.type());
        return request;
    }

}
