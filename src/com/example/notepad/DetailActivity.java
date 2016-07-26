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

		//�������ݿ�
		intent = getIntent();
		id = intent.getIntExtra("id", 1);
		db = new DB(this, "note.db3", null, 1);
		String sql = "select * from note where _id="+id;
		cursor = db.getReadableDatabase().rawQuery(sql, null);
		
		if(cursor.moveToFirst()){
			//��ȡ���ݿ��е�����
			contentString = cursor.getString(cursor.getColumnIndex("content"));
			
			//��ȡ���ݿ��е�����
			date_timeString = cursor.getString(cursor.getColumnIndex("date"));
			
		}
		
		//��ȡtextview��ʵ��
		TextView date_time = (TextView) findViewById(R.id.date_time);
		//��ȡedittext��ʵ��
		EditText content = (EditText) findViewById(R.id.content);
		date_time.setText(date_timeString);
		content.setText(contentString);
		
		//�ײ���ť����
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
					Toast.makeText(DetailActivity.this, "�����ɹ���", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(DetailActivity.this, "sd�������ڣ�", Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.d_save:
				
				EditText messageEditText = (EditText) findViewById(R.id.content);
				String message = messageEditText.getText().toString();
				if(contentString.equals(message)){
					Toast.makeText(DetailActivity.this, "��δ���±�ǩ��", Toast.LENGTH_LONG).show();
				}else{
					if(save(id, message)){
						Toast.makeText(DetailActivity.this, "�޸ĳɹ���", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(DetailActivity.this, "�޸�ʧ�ܣ�", Toast.LENGTH_LONG).show();
					}
				}

				break;
			default:
				break;
			}
		}
	};

	//�޸����ݱ������
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
