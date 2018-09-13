package message;/**
 * Created by DELL on 2018/8/31.
 */


import controller.DefaultSendFuture;
import controller.MessingSendListener;
import group.im1.message.Message;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import payload.GRequestPayload;
import serialization.Serializer;
import serialization.SerializerFactory;
import serialization.SerializerType;

/**
 * user is lwb
 **/

public abstract class AbstractEndPoint implements EndPoint {
    @Override
    public void sentMessage(final Message message, Channel channel, final MessingSendListener listener) {
        final GRequest request = createRequest(message);
        final DefaultSendFuture future = DefaultSendFuture.with(request.requestId(),channel);
        future.addListener(listener);
        channel.writeAndFlush(request.payload()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){
                    System.out.println("消息发送成功");
                    future.markSent();
                }else{
                    GResponse response = new GResponse(request.requestId());
                    response.status(Status.CLIENT_ERROR);
                    DefaultSendFuture.fakeReceived(channel,response);
                }
            }
        });
    }

    private Serializer serializer = SerializerFactory.getSerializer(SerializerType.JAVA.value());
    private GRequest createRequest(Message message) {
        GRequest request = new GRequest(new GRequestPayload());
        byte s_code = serializer.code();
        byte[] bytes = serializer.writeObject(message);
        request.bytes(s_code,bytes);
        request.messageType(message.type());
        return request;
    }

}
