package clientnetty;/**
 * Created by DELL on 2018/9/13.
 */

import connector.JProtocolHeader;
import controller.MessingSendListener;
import io.netty.channel.Channel;
import messageHandler.TextMessageHandler;

/**
 * user is lwb
 **/


public class Test {
    static CommManager manager = null;
    static boolean isConnected = false;
    static int time1 = 3;
    static String ipandport = "192.168.1.123:8888";
    public static void main(String[] args){
        manager = CommManager.getInstance();
        System.out.println("一次而已mmmmmmmmmmmmmmmmmmm");
        manager.addListener(new CommManager.ActiveAndInactiveListener() {
            @Override
            public void connected(Channel jChannel) {
                isConnected = true;
                time1 = 100;
                System.out.println("连接建立成功");
                manager.auth("zhangsan", new MessingSendListener() {

                    @Override
                    public void sendSuccessful() {
                        System.out.println("认证成功");
                    }

                    @Override
                    public void sendFailure() {

                    }
                });
            }

            @Override
            public void unConnected(Channel jChannel) {
                System.out.println("断开连接");
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("去重连33333333333333333");
                manager.connnecteSever();
            }
        });
        manager.addHandler(JProtocolHeader.TEXT,new TextMessageHandler());
        System.out.println("连接服务器");
        manager.connnecteSever();

    }
}
