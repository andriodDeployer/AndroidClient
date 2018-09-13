package payload;
/**
 * Created by DELL on 2018/8/30.
 */


import java.util.concurrent.atomic.AtomicLong;

/**
 * user is lwb
 **/


public class GRequestPayload extends PayloadHolder {

    private static final AtomicLong sequence = new AtomicLong();
    private final long requestId;
    private transient long timestemp;
    private transient byte type;
    public GRequestPayload(){
        this(sequence.incrementAndGet());
    }
    public GRequestPayload(long requestId){
        this.requestId = requestId;
    }

    public long requestId(){
        return requestId;
    }

    public long timestemp(){
        return timestemp;
    }

    public void timestemp(long timestemp){
        this.timestemp = timestemp;
    }
    public void type(byte type){
        this.type = type;
    }
    public byte type(){
        return type;
    }
}
