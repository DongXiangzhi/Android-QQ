package cn.edu.ldu.qqclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.ldu.qqclient.adapter.MessageItemAdapter;
import cn.edu.ldu.qqclient.beans.User;
import cn.edu.ldu.qqclient.util.Message;
import cn.edu.ldu.qqclient.util.Translate;

public class ChatActivity extends AppCompatActivity {
    private User user; //用户对象
    private ListView listView; //显示聊天信息的列表
    private DatagramSocket clientSocket; //客户机套接字
    private byte[] data=new byte[8096]; //8K字节数组
    public List<Message> messageList=new ArrayList<>(); //消息列表
    public MessageItemAdapter adapter; //消息列表适配器
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);//加载聊天视图
        //通过Intent获取当前用户对象
        user = getIntent().getExtras().getParcelable(ListActivity.CHAT_WITH_WHO);
        //将消息列表关联到消息适配器
        adapter = new MessageItemAdapter(this,messageList);
        //消息适配器关联到列表视图
        listView = (ListView) findViewById(R.id.listChat);
        listView.setAdapter(adapter);
        clientSocket=LoginActivity.clientSocket;//获取登录时使用的会话套接字
        //启动消息接收线程
        Thread recvMsg=new ReceiveMessage(clientSocket,ChatActivity.this);
        recvMsg.start();
    }
    public void sendClickHandler(View view) { //发送消息
        //获取发送内容并进行非空校验
        EditText etMessage= (EditText) findViewById(R.id.etInput);
        String txtMessage= String.valueOf(etMessage.getText());
        if (txtMessage.equals("")) {
            Toast.makeText(this, "发送内容不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        //将待发送消息定义为显示在自己这一方的消息
        Message selfMsg=new Message();
        selfMsg.setUserId(String.valueOf(user.getId()));
        selfMsg.setToAddr(user.getToAddr());
        selfMsg.setToPort(user.getToPort());
        selfMsg.setHeadImage(user.getHeadImg());
        selfMsg.setNickName(user.getNickName());
        selfMsg.setTargetId(user.getTargetId());
        selfMsg.setMsgTime(getMsgTime());
        selfMsg.setType("M_SELFMSG");//发给自己的消息
        selfMsg.setText(txtMessage);
        //更新数据源，通知适配器，由适配器触发视图更新
        messageList.add(selfMsg);
        adapter.notifyDataSetChanged();
        try {
            //定义发送给对方的消息
            Message outMsg=new Message();
            outMsg.setUserId(String.valueOf(user.getId()));
            outMsg.setToAddr(user.getToAddr());
            outMsg.setToPort(user.getToPort());
            outMsg.setHeadImage(user.getHeadImg());
            outMsg.setNickName(user.getNickName());
            outMsg.setTargetId(user.getTargetId());
            outMsg.setMsgTime(getMsgTime());
            outMsg.setType("M_PMSG"); //私聊消息
            outMsg.setText(txtMessage);
            data= Translate.ObjectToByte(outMsg);//消息对象序列化
            //构建发送报文
            DatagramPacket packet=new DatagramPacket(data,data.length,outMsg.getToAddr(),outMsg.getToPort());
            clientSocket.send(packet); //发送
            etMessage.setText(""); //清空输入框
        } catch (IOException ex) {
            Toast.makeText(this, "发送消息失败："+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private String getMsgTime(){ //消息发送时间
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日 HH:mm");
        Date date=new Date();
        return format.format(date);
    }
    //消息接收和处理线程类
    public class ReceiveMessage extends Thread{
        private DatagramSocket clientSocket; //会话套接字
        private ChatActivity parent; //父类
        private byte[] data=new byte[8096]; //8K字节数组
        //构造函数
        public ReceiveMessage(DatagramSocket socket,ChatActivity parent) {
            clientSocket=socket; //会话套接字
            this.parent=parent;
        }
        @Override
        public void run() {
            while (true) { //无限循环，处理收到的各类消息
                try {
                    DatagramPacket packet=new DatagramPacket(data,data.length); //构建接收报文
                    clientSocket.receive(packet); //接收
                    Message msg=(Message) Translate.ByteToObject(data);//还原消息对象
                    String userId=msg.getUserId(); //当前用户id
                    if (msg.getType().equalsIgnoreCase("M_PMSG")) { //收到新消息
                        parent.messageList.add(msg);
                        parent.adapter.notifyDataSetChanged();
                    }//end if
                }catch (Exception ex) {  }//end try
            } //end while
        }//end run
    }//end class ReceiveMessage
}//end class ChatActivity