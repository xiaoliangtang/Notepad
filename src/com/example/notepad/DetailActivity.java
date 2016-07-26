package com.example.notepad;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.util.DB;
import com.example.util.ScreenShot;

import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends Activity {
	
	DB db;
	Cursor cursor;
	String date_timeString,contentString;
	Intent intent;
	int id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		//创建数据库
		intent = getIntent();
		id = intent.getIntExtra("id", 1);
		db = new DB(this, "note.db3", null, 1);
		String sql = "select * from note where _id="+id;
		cursor = db.getReadableDatabase().rawQuery(sql, null);
		
		if(cursor.moveToFirst()){
			//获取数据库中的内容
			contentString = cursor.getString(cursor.getColumnIndex("content"));
			
			//获取数据库中的日期
			date_timeString = cursor.getString(cursor.getColumnIndex("date"));
			
		}
		
		//获取textview的实例
		TextView date_time = (TextView) findViewById(R.id.date_time);
		//获取edittext的实例
		EditText content = (EditText) findViewById(R.id.content);
		date_time.setText(date_timeString);
		content.setText(contentString);
		
		//底部按钮操作
		ImageView d_back = (ImageView) findViewById(R.id.d_back);
		ImageView d_screenshot = (ImageView) findViewById(R.id.d_screenshot);
		ImageView d_save = (ImageView) findViewById(R.id.d_save);
		
		d_back.setOnClickListener(listener);
		d_screenshot.setOnClickListener(listener);
		d_save.setOnClickListener(listener);
		
	}
	
	
	private View.OnClickListener listener = new View.OnClickListener() {
		
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.d_back:
				finish();
				break;
			case R.id.d_screenshot:
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				String filename = df.format(date);
				
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					String filePath = Environment.getExternalStorageDirectory() + "/DCIM/" + filename +"screenshot.png";
					ScreenShot.shoot(getParent(), new File(filePath));
					Toast.makeText(DetailActivity.this, "截屏成功！", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(DetailActivity.this, "sd卡不存在！", Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.d_save:
				
				EditText messageEditText = (EditText) findViewById(R.id.content);
				String message = messageEditText.getText().toString();
				if(contentString.equals(message)){
					Toast.makeText(DetailActivity.this, "您未更新便签！", Toast.LENGTH_LONG).show();
				}else{
					if(save(id, message)){
						Toast.makeText(DetailActivity.this, "修改成功！", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(DetailActivity.this, "修改失败！", Toast.LENGTH_LONG).show();
					}
				}

				break;
			default:
				break;
			}
		}
	};

	//修改数据保存操作
	private boolean save(int id,String message) {
		
		String updateSQL = "update note set content='"+message+"' where _id="+id;
		db = new DB(this, "note.db3", null, 1);
		db.getWritableDatabase().execSQL(updateSQL);
		return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}

}
