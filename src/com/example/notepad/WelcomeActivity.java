package com.example.notepad;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class WelcomeActivity extends Activity {
	
	TextView timeTextView;
	ImageView welcome;
	private int time = 3;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
		//获取TextView实例
		timeTextView = (TextView) findViewById(R.id.time);
		
		//获取ImageView
		welcome = (ImageView) findViewById(R.id.welcome_font);
		
		
		final Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				//注意取值范围：内嵌的if要遵从外面的if语句，如果time>1,会达不到效果。
				if(msg.what == 0x123 && time>=1){
					timeTextView.setText(time+"跳转");
					time--;
					if(time == 0){
						Intent intent = new Intent(WelcomeActivity.this,ListActivity.class);
						startActivity(intent);
						finish();
					}
				}
				
			};
		};
		
		//定时器实例
		final Timer timer = new Timer();	
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0x123;
				handler.sendMessage(msg);
			}
		}, 0, 1000);
		
	
		
	}

}
