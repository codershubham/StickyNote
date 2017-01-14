package com.shubham.navinote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public class DeleteNoteActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        list = (ListView)findViewById(R.id.listView1);
        fetchList();
        ArrayAdapter<String > adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice,noteList);

        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    public void fetchList(){
        noteList = new ArrayList<String>();
        File myFolder = new File(getFilesDir(),"notes");
        File[] fileList = myFolder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.getName().endsWith(".note"))
                    return true;
                else
                    return false;
            }
        });
        for(File file : fileList){
            noteList.add(file.getName().toString().substring(0,file.getName().toString().indexOf(".note")));
        }
    }

    public void deleteFile(View view){

        SparseBooleanArray item = list.getCheckedItemPositions();
        String str = "";
        int cnt=0;
        for(int i=0;i<noteList.size();i++){
            if(item.get(i)==true){
                cnt++;
            }
        }
        if(cnt>0) {
            new AlertDialog.Builder(this).setTitle("Confirmation").setMessage("Do you want to delete these notes ? ")
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            SparseBooleanArray item = list.getCheckedItemPositions();
                            String str = "";
                            int count = 0;
                            for (int i = 0; i < noteList.size(); i++) {
                                if (item.get(i) == true) {
                                    count++;
                                    str = noteList.get(i);
                                    new File(getFilesDir(), "notes/" + str + ".note").delete();

                                    // str+=noteList.get(i)+" ";
                                }
                            }
                            dialog.dismiss();
                            if ( count > 1) {
                                Toast.makeText(DeleteNoteActivity.this, count + " notes are deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (count == 1) {
                                Toast.makeText(DeleteNoteActivity.this, "1 notes is deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    }).create().show();
        }
        else {
            Toast.makeText(DeleteNoteActivity.this, "Please select some notes", Toast.LENGTH_SHORT).show();
        }
    }
}
