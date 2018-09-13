package group.im1.message;/**
 * Created by DELL on 2018/9/9.
 */


/**
 * user is lwb
 **/


public class GroupMessage extends Message {

    protected Message message;
    private String groupName;

    public GroupMessage(Message message){
       this(message,null);
    }

    public GroupMessage(Message message, String groupName){
        this.message = message;
        if(message.type() == JProtocolHeader.TEXT){
            this.type = JProtocolHeader.GROUP_TEXT;
        }else if(message.type() == JProtocolHeader.FILE){
            this.type = JProtocolHeader.GROUP_FILE;
        }

        if(groupName == null || groupName.equals(""))
            this.groupName = message.getReceiver();
        else
            this.groupName = groupName;
    }

    public void setOffline(){
        this.isOffline = true;
        this.message.setOffline();
    }


    @Override
    public String getReceiver() {
        return message.getReceiver();
    }

    @Override
    public String getSender() {
        return message.getSender();
    }

    @Override
    public String toString() {
        return "GroupMessage{" +
                "message=" + message +
                '}';
    }

    public Message getMessage(){
        return message;
    }

    public String groupName(){
        return groupName;
    }

    public void receiver(String receiver){
        this.message.receiver =  receiver;
        this.receiver = receiver;
    }

}
