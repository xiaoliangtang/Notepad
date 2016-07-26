package com.example.notepad;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.util.DB;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class AddnoteActivity extends Activity {
	
	DB db;
	ImageView back,save;
	EditText et_title,et_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnote);
		//创建数据库
		db = new DB(this, "note.db3", null, 1);
		
		back = (ImageView) findViewById(R.id.iv_return);
		save = (ImageView) findViewById(R.id.iv_save);
		
		back.setOnClickListener(listener);
		save.setOnClickListener(listener);
	}
	
	private View.OnClickListener listener = new View.OnClickListener() {
		
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.iv_return:
				finish();
				break;
				
			case R.id.iv_save:
				et_title = (EditText) findViewById(R.id.et_title);
				et_content = (EditText) findViewById(R.id.et_content);
				String title = et_title.getText().toString();
				String content = et_content.getText().toString();
				//日期处理
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = sdf.format(date);
				
				if(title.length()==0){
					Toast.makeText(AddnoteActivity.this, "便签主题不能为空！", Toast.LENGTH_SHORT).show();
				}else{
					//将数据插入数据中
					String insertSql = "insert into note values(null,?,?,?)";
					db.getReadableDatabase().execSQL(insertSql,new String[]{title,content,time});
					Toast.makeText(AddnoteActivity.this, "便签添加成功！", Toast.LENGTH_SHORT).show();
					Intent scapeListIntent = new Intent(AddnoteActivity.this,ListActivity.class);
					scapeListIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(scapeListIntent);	
					
					finish();
				}

				break;

			default:
				break;
			}
			
		}
	};

}
