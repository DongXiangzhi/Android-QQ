package cn.edu.ldu.qqclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

import cn.edu.ldu.qqclient.beans.User;
import cn.edu.ldu.qqclient.util.Message;
import cn.edu.ldu.qqclient.util.Translate;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);//加载布局文件视图
    }
    public void registerClickHandler(View view) { //注册按钮的响应函数
        try {
            //获取用户输入的注册信息，并作非空校验和密码一致性校验
            EditText etUserId= (EditText) findViewById(R.id.etUserId);
            EditText etNickName= (EditText) findViewById(R.id.etNickName);
            EditText etPassword= (EditText) findViewById(R.id.etPassword);
            EditText etPassword2= (EditText) findViewById(R.id.etPassword2);
            EditText etEmail= (EditText) findViewById(R.id.etEmail);
            String id= String.valueOf(etUserId.getText());
            String nickName= String.valueOf(etNickName.getText());
            String password=String.valueOf(etPassword.getText());
            String password2=String.valueOf(etPassword2.getText());
            String email=String.valueOf(etEmail.getText());
            if (id.equals("") || password.equals("") || nickName.equals("") || email.equals("")) {
                Toast.makeText(this, "各注册项目不能为空！", Toast.LENGTH_LONG).show();
                return;
            }else if (!password.equals(password2)) {
                Toast.makeText(this, "两次输入的密码不一致！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取服务器地址和端口
            String remoteName="192.168.1.102";//用服务器的IP地址，不能用127.0.0.1
            InetAddress remoteAddr=InetAddress.getByName(remoteName);
            int remotePort=50000;
            //创建UDP套接字
            DatagramSocket clientSocket=new DatagramSocket();
            //构建用户注册消息
            Message msg=new Message();
            msg.setUserId(id);//登录名
            msg.setPassword(password);//密码
            msg.setType("M_REGISTER"); //注册消息类型
            msg.setToAddr(remoteAddr); //目标地址
            msg.setToPort(remotePort); //目标端口
            msg.setNickName(nickName); //昵称
            msg.setEmail(email); //Email
            Random r=new Random();
            int index=r.nextInt(16)+10;//生成10--25之间随机整数
            String headImage="i90"+index+".jpg"; //组合出一个头像文件名
            msg.setHeadImage(headImage);
            byte[] data= Translate.ObjectToByte(msg); //消息对象序列化
            //定义注册报文
            DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,remotePort);
            //发送注册报文
            clientSocket.send(packet);
            //接收服务器回送的报文
            DatagramPacket backPacket=new DatagramPacket(data,data.length);
            clientSocket.receive(backPacket);
            Message backMsg=(Message)Translate.ByteToObject(data);
            //处理注册结果
            if (backMsg.getType().equalsIgnoreCase("M_REGISTER_DONE")) { //注册成功
                Intent loginIntent=new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
            }else { //注册失败
                Toast.makeText(this, "用户注册失败！", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
