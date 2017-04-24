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
import cn.edu.ldu.qqclient.beans.User;
//用户数据适配器
public class UserItemAdapter extends ArrayAdapter<User> {
    List<User> mUsers; //用户列表
    LayoutInflater mInflater; //布局加载器
    public UserItemAdapter(Context context, List<User> objects) {
        super(context, R.layout.list_item, objects);
        mUsers=objects; //获取数据源列表
        mInflater=LayoutInflater.from(context); //获取布局加载器
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user=mUsers.get(position);//从数据源列表取出第position个数据
        if (convertView == null) { //获取list_item视图对象
            convertView=mInflater.inflate(R.layout.list_item,parent,false);
        }
        //用当前user更新list_item中各个控件
        TextView textView= (TextView) convertView.findViewById(R.id.tvNickName_list);
        ImageView imageView= (ImageView) convertView.findViewById(R.id.ivHeadImg_list);
        textView.setText(user.getNickName());//设置昵称
        //设置头像
        InputStream inputStream = null;
        try {
            String imageFile=user.getHeadImg(); //头像文件名
            inputStream=getContext().getAssets().open(imageFile); //从assets文件夹获取头像
            Drawable d=Drawable.createFromStream(inputStream,null);//头像转为Drawable类型
            imageView.setImageDrawable(d); //设置头像控件
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
