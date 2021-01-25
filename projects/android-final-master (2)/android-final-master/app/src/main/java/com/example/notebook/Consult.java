package com.example.notebook;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Data;
import presenter.MyAdapter;
import presenter.MyDatabase;
import presenter.MyOpenHelper;

public class Consult extends AppCompatActivity {
    TextView textview;
    Button button;
    EditText editText;
    FloatingActionButton floatingActionButton;
    int ids;
    ListView listView;
    LayoutInflater layoutInflater;
    ArrayList<Data> arrayList;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult);
        textview=(TextView)findViewById(R.id.consult_textview);
        button=(Button)findViewById(R.id.consult_button);
        editText=(EditText)findViewById(R.id.consult_input);
        listView = (ListView)findViewById(R.id.layout_listview);
        layoutInflater = getLayoutInflater();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.consult_finish);
        Intent intent3 = this.getIntent();
        ids = intent3.getIntExtra("ids",0);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "正在查询……",Toast.LENGTH_LONG).show();
                queryText();
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }



    public void queryText() {
        MyOpenHelper helper = new MyOpenHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        //String str=editText.getText().toString();
        //Cursor c =db.query("mybook",new String[]{query_input},title+"  LIKE ? "new String[] { "%" + str[0] + "%" }, null, null, null);
        // Cursor c =db.query("mybook",null,"title = ?",new String[]{query_input},null,null,null);
        //Cursor  c=db.query("mybook", new String[]{"title"},"title"+"  like '%" + str[0] + "%'", null, null, null, null);
        String str;
        str=editText.getText().toString();
        Cursor c = db.query("mybook", new String[]{"title"}, "title"+"  LIKE ? ", new String[] { "%" + str + "%" }, null, null, null);
        if(c!=null && c.getCount()>=1){
            //String[] result = c.getColumnNames();
            while(c.moveToNext()){
                //String title = c.getString(c.getColumnIndex("title"));
                Toast.makeText(this, "查询到了相关单词！", Toast.LENGTH_SHORT).show();
                myDatabase = new MyDatabase(this);
                arrayList = myDatabase.getarray1(str);
                MyAdapter adapter = new MyAdapter(layoutInflater,arrayList);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(),NewNote.class);
                        intent.putExtra("ids",arrayList.get(position).getIds());
                        startActivity(intent);
                        Consult.this.finish();
                    }
                });
            }
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {   //长按删除
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(Consult.this)
                            //.setTitle("确定要删除此便签？")
                            .setMessage("确定要删除此便签？")
                            .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myDatabase.toDelete(arrayList.get(position).getIds());
                                    MyAdapter myAdapter = new MyAdapter(layoutInflater,arrayList);
                                    listView.setAdapter(myAdapter);
                                }
                            })
                            .create()
                            .show();
                    return true;
                }
            });
        }
        else{
            Toast.makeText(this, "没有查询到相关单词！", Toast.LENGTH_SHORT).show();
        }
    }


    public void onBackPressed(){
        Intent intent=new Intent(Consult.this,MainActivity.class);
        startActivity(intent);
        Consult.this.finish();
    }//返回

}