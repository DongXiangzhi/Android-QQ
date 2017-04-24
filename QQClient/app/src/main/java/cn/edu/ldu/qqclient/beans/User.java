package cn.edu.ldu.qqclient.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.InetAddress;
import java.sql.Timestamp;
public class User implements Parcelable { //定义会员实体类，属性名与字段名相同，类型也相同
    private int id; //对应数据表中的id
    private String nickName; //对应数据表中的name
    private String password; //对应数据表中的password
    private String email; //对应数据表中的email
    private Timestamp time; //对应数据表中的time
    private String headImg; //头像文件名，对应数据表中的headimage
    private InetAddress toAddr=null; //目标用户地址
    private int toPort; //目标用户端口
    private String targetId=null; //目标用户id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String name) {this.nickName = name; }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Timestamp getTime() {
        return time;
    }
    public void setTime(Timestamp time) {
        this.time = time;
    }
    public String getHeadImg() { return headImg; }
    public void setHeadImg(String headImg) { this.headImg = headImg; }
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
    public User() { }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nickName);
        dest.writeString(this.password);
        dest.writeString(this.email);
        dest.writeSerializable(this.time);
        dest.writeString(this.headImg);
        dest.writeSerializable(this.toAddr);
        dest.writeInt(this.toPort);
        dest.writeString(this.targetId);
    }
    protected User(Parcel in) {
        this.id = in.readInt();
        this.nickName = in.readString();
        this.password = in.readString();
        this.email = in.readString();
        this.time = (Timestamp) in.readSerializable();
        this.headImg = in.readString();
        this.toAddr = (InetAddress) in.readSerializable();
        this.toPort = in.readInt();
        this.targetId = in.readString();
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
