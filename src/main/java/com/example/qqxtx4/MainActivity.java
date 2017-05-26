package com.example.qqxtx4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.quickIndexView)
    QuickIndex quickIndexView;
    @BindView(R.id.listview)
    ListView listview;
    ArrayList<Friend> friends = new ArrayList<>();
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        quickIndexView.setOnLetterChangeListener(new QuickIndex.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                //根据当前触摸的字母，去列表中查找首字母和letter一样的条目，找到后将其置顶
                for (int i = 0; i < friends.size(); i++) {
                    String s = friends.get(i).pinyin.charAt(0) + "";
                    if (s.equals(letter)) {
                        //说明找到了，将其置顶
                        listview.setSelection(i);
                        //找到一次赶紧break
                            break;
                        }
                }
                //显示出当前触摸的字母
                showCurrent(letter);
            }

            @Override
            public void onRelease() {
                tvCurrent.setVisibility(View.GONE);
            }
        });
        //准备数据
        prepareData();
        listview.setAdapter(new FriendAdapter(this,R.layout.adapter_friend, friends));
        //对数据进行排序
        Collections.sort(friends);

    }
    /**
     * 显示当前触摸的字母
     * @param letter
     */
    private void showCurrent(String letter) {
        tvCurrent.setText(letter);
        tvCurrent.setVisibility(View.VISIBLE);
    }

    private void prepareData() {
        friends.add(new Friend("李伟"));
        friends.add(new Friend("张三"));
        friends.add(new Friend("阿三"));
        friends.add(new Friend("阿四"));
        friends.add(new Friend("段誉"));
        friends.add(new Friend("段正淳"));
        friends.add(new Friend("张三丰"));
        friends.add(new Friend("陈坤"));
        friends.add(new Friend("林俊杰1"));
        friends.add(new Friend("陈坤2"));
        friends.add(new Friend("王二a"));
        friends.add(new Friend("林俊杰a"));
        friends.add(new Friend("张四"));
        friends.add(new Friend("林俊杰"));
        friends.add(new Friend("王二"));
        friends.add(new Friend("王二b"));
        friends.add(new Friend("赵四"));
        friends.add(new Friend("杨坤"));
        friends.add(new Friend("赵子龙"));
        friends.add(new Friend("杨坤1"));
        friends.add(new Friend("李伟1"));
        friends.add(new Friend("宋江"));
        friends.add(new Friend("宋江1"));
        friends.add(new Friend("李伟3"));
    }

}
