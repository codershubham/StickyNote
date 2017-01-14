package com.shubham.navinote;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView list;
    String search;
    File file1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        list = (ListView)findViewById(R.id.listView1);
        list.setOnItemClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        search = getIntent().getStringExtra("key").toString();
        list.setAdapter(new NotesAdapter(search));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity((new Intent(this,AddNoteActivity.class))
                .putExtra("fileName",((TextView)view.findViewById(R.id.tvName)).getText().toString()));
    }

    class NotesAdapter extends BaseAdapter {
        ArrayList<Notes> noteList;
        SimpleDateFormat fmt;

        public NotesAdapter(final String search){
            noteList = new ArrayList<>();
            fmt = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a");
            File myFolder = new File(getFilesDir(),"notes");
            if(!myFolder.exists()){
                myFolder.mkdir();
            }
            File[] fileList = myFolder.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if(file.getName().endsWith(".note") && file.getName().startsWith(search))
                        return true;
                    else
                        return false;
                }
            });

            if(fileList.length==0) {
                Toast.makeText(SearchActivity.this,"There is no note exist for such name",Toast.LENGTH_SHORT).show();
                finish();
            }
            for(File file : fileList){
                Notes note = new Notes();
                note.setName(file.getName());
                Date d = new Date(file.lastModified());
                note.setDate(fmt.format(d));
                noteList.add(note);
            }
        }


        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int position) {
            return noteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view ;
            if(convertView==null){
                view= getLayoutInflater().inflate(R.layout.view_note,parent,false);
            }
            else{
                view = convertView;
            }

            Notes note = noteList.get(position);
            ((TextView)view.findViewById(R.id.tvName)).setText(note.getName().substring(0,note.getName().indexOf(".note")));
            ((TextView)view.findViewById(R.id.tvDate)).setText(note.getDate());
            return view;
        }
    }
}
