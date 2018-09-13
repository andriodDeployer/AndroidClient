package connector;/**
 * Created by DELL on 2018/8/30.
 */


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.internal.SystemPropertyUtil;
import payload.GRequestPayload;
import payload.GResponsePayload;

import java.util.List;

/**
 * user is lwb
 **/
public class IMMessageDecoder extends ReplayingDecoder<IMMessageDecoder.State> {

    // 协议体最大限制, 默认5M
    private static final int MAX_BODY_SIZE = SystemPropertyUtil.getInt("jupiter.io.decoder.max.body.size", 1024 * 1024 * 500);
    private static final boolean USE_COMPOSITE_BUF = SystemPropertyUtil.getBoolean("jupiter.io.decoder.composite.buf", false);


    public IMMessageDecoder(){
        super(State.MAGIC);
        if (USE_COMPOSITE_BUF) {
            setCumulator(COMPOSITE_CUMULATOR);
        }
    }

    private final JProtocolHeader header = new JProtocolHeader();

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        switch (state()){
            case MAGIC:
                checkMagic(in.readShort());
                checkpoint(State.SIGN);
            case SIGN:
                header.sign(in.readByte());
                checkpoint(State.STATUS);
            case STATUS:
                header.status(in.readByte());
                checkpoint(State.ID);
            case ID:
                header.id(in.readLong());
                checkpoint(State.BODY_SIZE);
            case BODY_SIZE:
                header.bodySize(in.readInt());
                checkpoint(State.BODY);
            case BODY:
                switch (header.messageCode()){
                    case JProtocolHeader.HEARTBEAT:
                        break;
                    case JProtocolHeader.AUTH:
                    case JProtocolHeader.GROUP_TEXT:
                    case JProtocolHeader.GROUP_FILE:
                    case JProtocolHeader.FILE:
                    case JProtocolHeader.TEXT: {
                        int length = checkBodySize(header.bodySize());
                        byte[] bytes = new byte[length];
                        in.readBytes(bytes);

                        GRequestPayload request = new GRequestPayload(header.id());

                        request.bytes(header.serializerCode(), bytes);

                        out.add(request);
                        break;
                    }
                    case JProtocolHeader.RESPONSE: {
                        int length = checkBodySize(header.bodySize());
                        byte[] bytes = new byte[length];
                        in.readBytes(bytes);

                        GResponsePayload response = new GResponsePayload(header.id());
                        response.status(header.status());
                        response.bytes(header.serializerCode(), bytes);
                        out.add(response);
                        break;
                    }
                    default:
                        throw new IllegalStateException("Unsupport type");

                }
                checkpoint(State.MAGIC);



        }



    }

    private void checkMagic(Short magic)  {
        if(magic != JProtocolHeader.MAGIC){
            throw new IllegalStateException("Error magic "+magic);
        }
    }

    private static int checkBodySize(int size)  {
        if (size > MAX_BODY_SIZE) {
            throw new IllegalStateException("beyongde max size ");
        }
        return size;
    }

    enum State {
        MAGIC,
        SIGN,
        STATUS,
        ID,
        BODY_SIZE,
        BODY
    }
}
