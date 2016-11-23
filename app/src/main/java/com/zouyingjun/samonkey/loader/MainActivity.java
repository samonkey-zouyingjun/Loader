package com.zouyingjun.samonkey.loader;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText etinput;
    private String filterName;
    private TextView tv;
    private SimpleCursorAdapter adapter;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        loadData();
    }

    private void loadData() {
        adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_1,
                null,new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                new int[]{android.R.id.text1},0);
        setListAdapter(adapter);
        //初始化Loader
        getLoaderManager().initLoader(0,null,this);
    }

    private void initView() {
        etinput = (EditText) findViewById(R.id.et_input);
        tv = (TextView) findViewById(R.id.tv);
        lv = (ListView) findViewById(R.id.lv);
    }

    private void initData() {
    }
    public void onClick(View v){

        if(tv.getVisibility() == View.VISIBLE){
            tv.setVisibility(View.INVISIBLE);
        }
        String input = etinput.getText().toString();
        if(TextUtils.isEmpty(input)){
            return;
        }
        filterName = input;

        //使用新的Loader(清空旧的数据)
        getLoaderManager().restartLoader(0,null,this);

        adapter.notifyDataSetChanged();
    }


    public void setListAdapter(SimpleCursorAdapter listAdapter) {
        lv.setAdapter(listAdapter);
    }

    /**回调*/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //创建Loader对象，开始异步加载数据
        Uri uri;
        String[] pro=new String[]{ ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID};
        if(TextUtils.isEmpty(filterName)){
            uri= ContactsContract.Contacts.CONTENT_URI;
        }else{
            uri=Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI,
                    Uri.encode(filterName));
        }
        //创建Loader对象，开始异步加载数据
        return new CursorLoader(this,uri,pro, null, null,null);


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
