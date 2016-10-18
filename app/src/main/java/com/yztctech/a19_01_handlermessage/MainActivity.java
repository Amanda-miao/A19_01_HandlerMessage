package com.yztctech.a19_01_handlermessage;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 演示通过Handler实现线程间数据交互
 * 1、创建Handler实例
           Handlder  handler = new Handler();
 * 2、创建Message实例
          Message msg = handler.obtainMessage();
 * 3、绑定数据
         ar1,arg2 简单的int数据
         what int数据，存放目的或者状态
         obj  放任意的对象
 * 4、发送消息
         handler.sendMessage(msg);
 * 5、接收消息
        重写Handler上的方法
         handleMessage(Message msg) {

        }
 */
public class MainActivity extends AppCompatActivity {

    //子线程状态常量
    private static final int COUNT_START = 0; //即将开始
    private static final int COUNT_RUNNING = 1; //开始
    private static final int COUNT_END = 2; //结束

    private TextView countTv;
    private Button startBtn;

    //创建Handler实例
    private Handler countHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //接收消息
            switch (msg.what) {
                case COUNT_START:
                    countTv.setText("倒计时开始");
                    break;
                case COUNT_RUNNING:
                    countTv.setText(String.valueOf(msg.arg1));
                    break;
                case COUNT_END:
                    String txt = (String) msg.obj;
                    countTv.setText(txt);
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        countTv = (TextView) findViewById(R.id.tv_count);
        startBtn = (Button) findViewById(R.id.btn_start);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //启动子线程
                new Thread(new CountRunnable()).start();
            }
        });
    }

    class CountRunnable implements Runnable{

        @Override
        public void run() {
            //即将开始倒计时阶段
            Message startMsg = countHandler.obtainMessage();
            startMsg.what = COUNT_START;
            countHandler.sendMessage(startMsg);

            //开始倒计时
            int count = 10;
            while(count > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count --;
                //创建Message
                Message msg = countHandler.obtainMessage();
                //绑定数据
                msg.arg1 = count;
                msg.what = COUNT_RUNNING;
                //发送消息
                countHandler.sendMessage(msg);
            }

            //倒计时结束
            Message endMsg = countHandler.obtainMessage();
            endMsg.what = COUNT_END;
            endMsg.obj = "倒公交噶的哥嫂几个大搜集";
            countHandler.sendMessage(endMsg);

        }
    }

}
