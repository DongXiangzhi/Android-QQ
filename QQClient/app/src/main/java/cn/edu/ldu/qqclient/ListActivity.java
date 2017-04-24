package cn.edu.ldu.qqclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import cn.edu.ldu.qqclient.adapter.UserItemAdapter;
import cn.edu.ldu.qqclient.beans.User;


public class ListActivity extends AppCompatActivity {
    public static final String CHAT_WITH_WHO = "chat_with_who";//Intent对象关键字常量
    private User user; //当前用户对象
    public static List<User> userList=new ArrayList<>(); //用户对象列表
    private List<String> nickNameList=new ArrayList<>(); //用户昵称列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list); //加载列表视图
        //通过Intent获取从LoginActivity传递过来的user对象
        user = getIntent().getExtras().getParcelable(LoginActivity.LOGIN_MESSAGE);
        //当前用户对象加到数据源列表中
        userList.add(user);
        //关联数据源与适配器
        UserItemAdapter adapter=new UserItemAdapter(this,userList);
        //关联适配器与列表视图
        ListView listView= (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        //为列表添加列表条目点击事件响应函数，跳转到聊天ChatActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User toUser=userList.get(position);
                if (user.getId()==toUser.getId()) {
                    Toast.makeText(ListActivity.this, "不能跟自己聊天！", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    user.setTargetId(String.valueOf(toUser.getId()));
                    goChatActivity(user);
                }
            }
        });
    }
    private void goChatActivity(User user) { //转到聊天页面
        Intent chatIntent=new Intent(this,ChatActivity.class);
        chatIntent.putExtra(CHAT_WITH_WHO,user);
        startActivity(chatIntent);
    }
}
