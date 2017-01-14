package com.shubham.navinote;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class AddNoteActivity extends AppCompatActivity {
    EditText t1,t2;
    File file1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        t1 = (EditText)findViewById(R.id.editText1);
        t2 = (EditText)findViewById(R.id.editText2);
        StringBuffer sb=new StringBuffer();
        String tvName = getIntent().getStringExtra("fileName");
        try {
            File file = new File(getFilesDir(),"notes/"+tvName+".note");
            file1=file;
            FileInputStream is = new FileInputStream(file);

            int x=is.read();
            while(x!=-1){
                sb.append((char)x);
                x=is.read();
            }
        }catch (Exception ex){}
        t1.setText(tvName);
        t2.setText(sb);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.noteDelete:
                new android.app.AlertDialog.Builder(this).setTitle("Confirmation").setMessage("Do you want to delete this note ? ")
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                file1.delete();
                                dialog.dismiss();
                                Toast.makeText(AddNoteActivity.this, "Note is deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    public void saveFile(View view){
        String name = t1.getText().toString();
       if (!name.equals("")) {
           if (!name.endsWith(".note")) {
               name += ".note";
           }
           String content = t2.getText().toString();
           try {
               File myFolder = new File(getFilesDir(), "notes");
               if (!myFolder.exists()) {
                   myFolder.mkdir();
               }

               FileOutputStream os = new FileOutputStream(new File(myFolder, name));
               os.write(content.getBytes());
               os.close();

               t1.setText("");
               t2.setText("");
               Toast.makeText(AddNoteActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
           } catch (Exception ex) {
               Toast.makeText(AddNoteActivity.this, "Note Not Saved : " + ex, Toast.LENGTH_SHORT).show();
           }
           finish();
       }
       else
           Toast.makeText(AddNoteActivity.this,"Please name your file",Toast.LENGTH_SHORT).show();
    }
}
