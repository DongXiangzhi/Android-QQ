package cn.edu.ldu.qqclient.util;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Message，消息类，定义会话消息结构，规定会话协议。
 * type表示的消息类型的含义如下：
 * M_LOGIN:用户登录消息；
 * M_SUCCESS:登录成功；
 * M_FAULURE:登录失败；
 * M_ACK:服务器对登录用户的回应消息；
 * M_REGISTER:注册消息
 * M_REGISTER_DONE:注册成功
 * M_REGISTER_LOST：注册失败
 * M_DONE:文件传送成功
 * M_LOST：文件传送失败
 * M_MSG:会话消息；
 * M_PMSG:私聊消息
 * M_SELFMSG:自己给自己的消息
 * M_QUIT:用户退出消息
 * @author 董相志，版权所有2016--2018，upsunny2008@163.com
 */
public class Message implements Serializable {
    private String userId=null; //用户id
    private String nickName=null; //昵称
    private String password=null; //密码
    private String headImage=null;//头像
    private String type=null; //消息类型
    private String text=null; //消息体
    private String email=null; //邮件地址
    private InetAddress toAddr=null; //目标用户地址
    private int toPort; //目标用户端口
    private String targetId=null; //目标用户id
    private String msgTime; //消息发生时间
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getNickName() {return nickName;}
    public void setNickName(String nickName) {this.nickName = nickName;}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getHeadImage() { return headImage;}
    public void setHeadImage(String headImage) {this.headImage = headImage;}
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public InetAddress getToAddr() {
        return toAddr;
    }
    public void setToAddr(InetAddress toAddr) {
        this.toAddr = toAddr;
    }
    public int getToPort() {
        return toPort;
    }
    public void setToPort(int toPort) {
        this.toPort = toPort;
    }
    public String getTargetId() {
        return targetId;
    }
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    public String getMsgTime() {return msgTime;}
    public void setMsgTime(String msgTime) {this.msgTime = msgTime;}
}
