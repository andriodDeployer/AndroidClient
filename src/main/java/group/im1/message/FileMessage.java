package group.im1.message;/**
 * Created by DELL on 2018/9/9.
 */

/**
 * user is lwb
 **/


public class FileMessage extends Message {
    private byte[] content;
    private String fileName;
    private int fileSize;

    public FileMessage(String sender,String receiver,byte[] content,String fileName){
        this.type = JProtocolHeader.FILE;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.fileName = fileName;
        this.fileSize = content.length;
    }

    public byte[] content(){
        return content;
    }

    public String fileName(){
        return fileName;
    }

    public int fileSize(){
        return fileSize;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
