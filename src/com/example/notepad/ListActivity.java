package com.example.notepad;

import com.example.util.DB;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;

public class ListActivity extends Activity {

	DB db;
	Cursor cursor;
	SimpleCursorAdapter adapter;
	ImageView addNote,exit;
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		//创建数据库
		db = new DB(this, "note.db3", null, 1);
		
		//查询便签数据
		String sql = "select * from note order by date DESC";	
		cursor = db.getReadableDatabase().rawQuery(sql, null);

		addNote = (ImageView) findViewById(R.id.addnote);
		exit = (ImageView) findViewById(R.id.exit);
		listView = (ListView) findViewById(R.id.listNote);
		
		addNote.setOnClickListener(listener);
		exit.setOnClickListener(listener);
		
		//为结果集添加适配器
		inflateNote(cursor);
		
		//listView设置单击事件，以查看详细详细
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {			
				Intent intent = new Intent(ListActivity.this,DetailActivity.class);
				//intent携带id数据
				Bundle bundle = new Bundle();
				bundle.putInt("id", (int) id);	
				intent.putExtras(bundle);
				startActivity(intent);
				
				//Toast.makeText(ListActivity.this, "id="+id, Toast.LENGTH_LONG).show();
			}
			
		});
		
		//listView设置长按事件，以查看详细详细
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			/**
			 * position:从0开始
			 * id:插入数据库中的id
			 * */
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, final long id) {
				

				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListActivity.this);
				dialogBuilder.setIcon(R.drawable.ic_launcher);
				dialogBuilder.setTitle("便签提示");
				dialogBuilder.setMessage("您是否确认删除！");
				dialogBuilder.setPositiveButton("确定", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						//删除数据
						String delsql = "delete from note where _id="+id;
						db.getReadableDatabase().execSQL(delsql);
						
						//刷新listview的数据
						String sql = "select * from note";	
						cursor = db.getReadableDatabase().rawQuery(sql, null);
						inflateNote(cursor);
						listView.invalidate();
						Toast.makeText(ListActivity.this, "该便签删除成功！", Toast.LENGTH_LONG).show();
					}
				});
				
				dialogBuilder.setNegativeButton("取消", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});
				
				//创建显示弹出框
				dialogBuilder.create().show();
				
				//Toast.makeText(ListActivity.this, "position="+position+"id="+id, Toast.LENGTH_LONG).show();
							
				return true;
			}
			
		});
		
	}
	
	//自定义适配器处理
	@SuppressLint("InlinedApi")
	private void inflateNote(Cursor cursor) {
		adapter = new SimpleCursorAdapter(ListActivity.this, R.layout.activity_line, cursor, new String[]{"title","date"}, new int[]{R.id.title,R.id.date}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		listView.setAdapter(adapter);

	}
	
	private View.OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.addnote:
				Intent addnoteIntent = new Intent(ListActivity.this,AddnoteActivity.class);
				startActivity(addnoteIntent);
				break;

			case R.id.exit:
				finish();
				break;
			default:
				break;
			}
		}
	};

}
