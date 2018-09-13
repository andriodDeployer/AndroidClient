package message;/**
 * Created by DELL on 2018/8/30.
 */


import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import payload.GRequestPayload;
import payload.GResponsePayload;
import processor.AbstractProcessor;
import serialization.Serializer;
import serialization.SerializerFactory;
import group.im1.message.Message;

/**
 * user is lwb
 **/


public abstract class MessageTask implements Runnable{

    //private static final InternalLogger logger = InternalLoggerFactory.getInstance(MessageTask.class);
    protected Channel jChannel; //发送者channel
    protected AbstractProcessor processor;
    protected GRequest request;

    public MessageTask(Channel jChannel, AbstractProcessor processor, GRequest request) {
        this.jChannel = jChannel;
        this.processor = processor;
        this.request = request;
    }

    @Override
    public void run() {
        final GRequest _request = request;
        Message msg;
        GRequestPayload payload = _request.payload();
        byte serializerCode = payload.serializerCode();
        Serializer serializer = SerializerFactory.getSerializer(serializerCode);

        try{

            byte[] bytes = payload.bytes();
            msg = serializer.readObject(bytes, Message.class);
            payload.clear();

            _request.message(msg);
        }catch (Throwable t){
            //todo 发送过来的消息无法序列化
            return;
        }

        //返回响应
        GResponsePayload responsePayload = new GResponsePayload(payload.requestId());
        byte[] bytes = serializer.writeObject("ok");
        responsePayload.bytes(serializerCode,bytes);
        responsePayload.status(Status.OK.value());
        writeResponse(responsePayload);

        processMessage(msg);
    }

    private void writeResponse(final GResponsePayload response) {
        ChannelFuture future = jChannel.writeAndFlush(response);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if(channelFuture.isSuccess()){

                }else{

                }
            }
        });
    }

    protected abstract void processMessage(Message msg);
}
