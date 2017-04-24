package cn.edu.ldu.qqclient.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.edu.ldu.qqclient.R;
import cn.edu.ldu.qqclient.util.Message;

public class MessageItemAdapter extends ArrayAdapter<Message> {
    List<Message> mMessages; //消息对象列表
    LayoutInflater mInflater; //布局加载器
    public MessageItemAdapter(Context context, List<Message> objects) {
        super(context, R.layout.recv_msg, objects);
        mMessages=objects; //获取消息对象列表
        mInflater=LayoutInflater.from(context); //获取布局加载器
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message msg=mMessages.get(position); //获取当前消息对象
        if (convertView == null) {
            if (msg.getType().equalsIgnoreCase("M_SELFMSG")) {//根据消息类型选择布局文件
                //加载发送消息的布局文件
                convertView=mInflater.inflate(R.layout.send_msg,parent,false);
            }else{ //加载接收消息的布局文件
                convertView=mInflater.inflate(R.layout.recv_msg,parent,false);
            }
        }
        //设置布局文件中各控件的值
        TextView tvTime= (TextView) convertView.findViewById(R.id.tvTime);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.ivHead);
        TextView tvMsg= (TextView) convertView.findViewById(R.id.tvMessage);
        tvTime.setText(msg.getMsgTime()); //设置时间
        tvMsg.setText(msg.getText()); //设置消息内容
        //设置头像
        InputStream inputStream = null;
        try {
            String imageFile=msg.getHeadImage(); //头像文件名
            inputStream=getContext().getAssets().open(imageFile);//从assets头像文件夹获取
            Drawable d=Drawable.createFromStream(inputStream,null);//转换为Drawable对象
            imageView.setImageDrawable(d);//设置头像控件
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream!= null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return convertView; //返回视图对象
    }
}
