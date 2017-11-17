package com.example.computer.ljp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import cn.bmob.v3.Bmob;

public class MainActivity extends Activity {
    private boolean isConnecting=false;
    private Thread mThreadClient=null;
    private Socket mSocketClient=null;
    static BufferedReader mBufferedReaderClient=null;
    static PrintWriter mPrintWriterClient=null;
    private String recvMessageClient="",Str_prompt="";
    private String IPaddr;
    // 定义按钮，触发发送、接收函数
    private Button btnSend_up;
    private Button btnSend_down;
    private Button btnSend_left;
    private Button btnSend_right;
    private Button btnSend_center;
    private Button btnSend_up1;
    private Button btnSend_left1;
    private Button btnSend_right1;
    //定义文本框 显示接收到的数据
    private TextView TVprompt;
    private TextView TVdata;
    private TextView wendu;
    private TextView shidu;
    Message prompt_msg = new Message();
    private static Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "dcd1546c8df450aa203b637c9415617f");
        //实例化按钮 并且绑定监听器
        btnSend_up = (Button) findViewById(R.id.button_up);
        btnSend_up.setOnClickListener(btnDoSugOnClick);
        btnSend_down = (Button) findViewById(R.id.button_down);
        btnSend_down.setOnClickListener(btnDoSugOnClick);
        btnSend_left = (Button) findViewById(R.id.button_left);
        btnSend_left.setOnClickListener(btnDoSugOnClick);
        btnSend_right = (Button) findViewById(R.id.button_right);
        btnSend_right.setOnClickListener(btnDoSugOnClick);
        btnSend_center = (Button) findViewById(R.id.button_center);
        btnSend_center.setOnClickListener(btnDoSugOnClick);
        btnSend_up1 = (Button) findViewById(R.id.button_up1);
        btnSend_up1.setOnClickListener(btnDoSugOnClick);
//        btnSend_down1 = (Button) findViewById(R.id.button_down1);
//        btnSend_down1.setOnClickListener(btnDoSugOnClick);
        btnSend_left1 = (Button) findViewById(R.id.button_left1);
        btnSend_left1.setOnClickListener(btnDoSugOnClick);
        btnSend_right1 = (Button) findViewById(R.id.button_right1);
        btnSend_right1.setOnClickListener(btnDoSugOnClick);
//        btnSend_center1 = (Button) findViewById(R.id.button_center1);
//        btnSend_center1.setOnClickListener(btnDoSugOnClick);
        //实例化文本视图
        TVprompt=(TextView)findViewById(R.id.TV_prompt);
        TVdata=(TextView)findViewById(R.id.TV_data);
        wendu=(TextView)findViewById(R.id.wendu);
        shidu=(TextView)findViewById(R.id.shidu);

//        p2.setName("lucky");
//        p2.setAddress("北京海淀");
//        p2.save(new SaveListener<String>() {
//            @Override
//            public void done(String objectId,BmobException e) {
//                if(e==null){
//                    toast("添加数据成功，返回objectId为："+objectId);
//                }else{
//                    toast("创建数据失败：" + e.getMessage());
//                }
//            }
//        });
        inputTitleDialog();
    }

    private View.OnClickListener btnDoSugOnClick = new View.OnClickListener() {  //按下之后又放开
        private byte[] msg_temp=null;
        @Override
        public void onClick(View arg0) {   //传入被电击的View控件 这里就是button
            Button btn = (Button) arg0;
            switch (btn.getId()) {
                case R.id.button_up:
                    msg_temp=getResources().getString(R.string.data_up).getBytes();
                    break;
                case R.id.button_down:
                    msg_temp=getResources().getString(R.string.data_down).getBytes();
                    break;
                case R.id.button_left:
                    msg_temp=getResources().getString(R.string.data_left).getBytes();
                    break;
                case R.id.button_right:
                    msg_temp=getResources().getString(R.string.data_right).getBytes();
                    break;
                case R.id.button_center:
                    msg_temp=getResources().getString(R.string.data_center).getBytes();
                    break;
                case R.id.button_up1:
                    msg_temp=getResources().getString(R.string.data_up1).getBytes();
                    break;
//                case R.id.button_down1:
//                    msg_temp=getResources().getString(R.string.data_down1).getBytes();
//                    break;
                case R.id.button_left1:
                    msg_temp=getResources().getString(R.string.data_left1).getBytes();
                    break;
                case R.id.button_right1:
                    msg_temp=getResources().getString(R.string.data_right1).getBytes();
                    break;
//                case R.id.button_center1:
//                    msg_temp=(byte)getResources().getInteger(R.integer.data_center1);
//                    break;
            }
            if(isConnecting&&mSocketClient!=null)
            {
                try
                {
                    mPrintWriterClient.print(msg_temp);
                    for (byte i:msg_temp){
                        mPrintWriterClient.write(i);
                    }
                    mPrintWriterClient.flush();
                }catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(MainActivity.this, "发送数据错误"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }else
            {
                Toast.makeText(MainActivity.this, "请连接服务器", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.remotetemp, menu);
        return true;
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isConnecting){
            menu.getItem(1).setTitle("断开连接");
        }else{
            menu.getItem(1).setTitle("连接服务器");
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case (R.id.action_about): {
                display_about();
                break;
            }
            case (R.id.action_search): {
                this.inputTitleDialog();
                break;
            }
            case (R.id.action_exit): {
                System.exit(0);
                break;
            }
            default: return false;
        }
        return true;
    }
    private void inputTitleDialog() {
        final EditText inputIP = new EditText(this);
        inputIP.setText("192.168.123.169:8080");
        inputIP.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(inputIP);
        builder.setTitle("请输入IP地址");

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                IPaddr=inputIP.getText().toString();
                if(isConnecting)
                {
                    isConnecting = false;
                    try
                    {
                        if(mSocketClient!=null)
                        {
                            mSocketClient.close();
                            mSocketClient = null;
                            mPrintWriterClient.close();
                            mPrintWriterClient = null;
                        }
                    }catch (IOException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                    mThreadClient.interrupt();
                }else
                {
                    isConnecting=true;
                    mThreadClient = new Thread(mRunnable);
                    mThreadClient.start();
                }
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }
    //连接wifi程序
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            String msgText = IPaddr;
            if(msgText.length()<=0)
            {
                Str_prompt = "IP输入错误，请重新输入";
                prompt_msg.what = 1;
                mHandler.sendMessage(prompt_msg);
                return;
            }
            int start = msgText.indexOf(":");
            if((start==-1)||(start+1>=msgText.length()))
            {
                Str_prompt = "IP输入错误，请重新输入";
                prompt_msg.what = 1;
                mHandler.sendMessage(prompt_msg);
                return;
            }
            String sIP= msgText.substring(0,start);
            String sPort = msgText.substring(start+1);
            Str_prompt="IP:"+sIP+"\n"+"PORT:"+sPort+"\n"+"状态：";
            int port = Integer.parseInt(sPort);
            try
            {
                //建立socket连接
                mSocketClient = new Socket(sIP,port);
                mBufferedReaderClient=new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
                mPrintWriterClient=new PrintWriter(mSocketClient.getOutputStream(),true);
                Str_prompt =Str_prompt+"已连接";
            }catch (Exception e) {
//                Toast.makeText(MainActivity.this,"连接IP错误",Toast.LENGTH_SHORT).show();
                Str_prompt =Str_prompt+"未连接";
                return;
            }
            prompt_msg.what = 3;
            mHandler.sendMessage(prompt_msg);
            char[] buffer = new char[256];
            int count = 0;
            while(isConnecting)
            {
                try
                {
                    if((count = mBufferedReaderClient.read(buffer))>0)
                    {
                        recvMessageClient ="数据：\n";
                        recvMessageClient += getInfoBuff(buffer,count);
                        handler.post(new Runnable() {    //更新数据 只能这样更新
                            @Override
                            public void run() {
                                TVdata.setText(recvMessageClient);
                                wendu.setText(Cal(recvMessageClient));
                                shidu.setText(Bal(recvMessageClient));
                            }
                        });
                    }
                }catch (Exception e) {
                    Str_prompt = "接收数据错误";
                    prompt_msg.what = 1;
                    mHandler.sendMessage(prompt_msg);
                }
            }
        }
    };
    private String Cal(String s){
        String[] mStrings  = s.split("_");
        if(mStrings.length>7){
            return mStrings[7];
        }else {
            return "无";
        }
    }

    private String Bal(String s){
        String[] mStrings  = s.split("_");
        if(mStrings.length>7){
            return mStrings[8];
        }else {
            return "无";
        }
    }
    private String getInfoBuff(char[] buff,int count)
    {
        char[] temp = new char[count];
        for (int i = 0; i < count; i++) {
            temp[i]=buff[i];
        }
        return new String(temp);
    }
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(msg.what==1)
            {
                Toast.makeText(getApplicationContext(),"Client:"+recvMessageClient,Toast.LENGTH_SHORT).show();
            }
            if(msg.what==2)    //有数据返回
            {
                TVdata.setText(recvMessageClient);
            }
            if(msg.what==3)
            {
                TVprompt.setText(Str_prompt);
            }
        };
    };
    protected void display_about() {
        Dialog dialog =new Dialog(MainActivity.this);
        dialog.setTitle("关于软件");
        dialog.setContentView(R.layout.about_app);
        String str_code=new  String("");
        str_code +="up:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_up));
        str_code +=" down:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_down));
        str_code +=" left:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_left));
        str_code +=" right:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_right));
        str_code +=" center:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_center));
        TextView APP_buttoncode=(TextView)dialog.findViewById(R.id.APP_buttoncode);
        APP_buttoncode.setText(str_code);
        str_code ="";
        str_code +="up:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_up1));
        str_code +=" down:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_down1));
        str_code +=" left:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_left1));
        str_code +=" right:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_right1));
        str_code +=" center:0x";
        str_code +=String.format("%02d",(Integer)getResources().getInteger(R.integer.data_center1));
        APP_buttoncode=(TextView)dialog.findViewById(R.id.APP_buttoncode1);
        APP_buttoncode.setText(str_code);
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            if(mSocketClient!=null)
            {
                mSocketClient.close();
                mPrintWriterClient.close();
                mSocketClient = null;
                mPrintWriterClient = null;
            }
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        System.exit(0);
    }
}
