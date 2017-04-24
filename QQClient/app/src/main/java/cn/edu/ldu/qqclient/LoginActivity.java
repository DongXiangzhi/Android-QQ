package cn.edu.ldu.qqclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import cn.edu.ldu.qqclient.beans.User;
import cn.edu.ldu.qqclient.util.Message;
import cn.edu.ldu.qqclient.util.Translate;


public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_MESSAGE = "LoginMessage";//Intent对象传递关键字
    public static  DatagramSocket clientSocket; //会话套接字
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);//加载登录布局视图
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login,menu); //加载菜单视图
        return true;
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
    public void loginClickHandler(View view) { //用户登录模块
        try {
            //获取登录用户ID和密码，并作简单非空验证
            EditText etUserId= (EditText) findViewById(R.id.etUserId_login);
            String id= String.valueOf(etUserId.getText());
            EditText etPassword= (EditText) findViewById(R.id.etPassword_login);
            String password=String.valueOf(etPassword.getText());
            if (id.equals("") || password.equals("")) {
                Toast.makeText(this, "帐号或密码不能为空！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取服务器地址和端口
            String remoteName="192.168.1.102";//根据主机IP设置，此处不能用127.0.0.1
            InetAddress remoteAddr=InetAddress.getByName(remoteName);
            int remotePort=50000;
            //创建UDP套接字
            clientSocket=new DatagramSocket();
            //构建用户登录消息
            Message msg=new Message();
            msg.setUserId(id);//登录名
            msg.setPassword(password);//密码
            msg.setType("M_LOGIN"); //登录消息类型
            msg.setToAddr(remoteAddr); //目标地址
            msg.setToPort(remotePort); //目标端口
           byte[] data= Translate.ObjectToByte(msg); //消息对象序列化
            //定义登录报文
            DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,remotePort);
            //发送登录报文
            clientSocket.send(packet);
            //接收服务器回送的报文
            DatagramPacket backPacket=new DatagramPacket(data,data.length);
            clientSocket.receive(backPacket);
            Message backMsg=(Message)Translate.ByteToObject(data);
            //处理登录结果
            if (backMsg.getType().equalsIgnoreCase("M_SUCCESS")) { //登录成功
                //接收服务器返回的用户信息
                User user=new User();
                user.setId(Integer.parseInt(backMsg.getUserId()));
                user.setNickName(backMsg.getNickName());
                user.setPassword(backMsg.getPassword());
                user.setHeadImg(backMsg.getHeadImage());
                user.setToAddr(msg.getToAddr());
                user.setToPort(msg.getToPort());
                //将登录用户的信息以Parcelable对象形式封装到Intent中
                Intent listIntent=new Intent(this,ListActivity.class);
                listIntent.putExtra(LOGIN_MESSAGE,user);
                startActivity(listIntent); //转到在线列表页面ListActivity
            }else { //登录失败
                Toast.makeText(this, "用户ID或密码错误！", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onRegistHandler(MenuItem item) { //点击注册命令后，转到注册页面
        Intent registerIntent=new Intent(this,RegisterActivity.class);
        startActivity(registerIntent);
    }
}
