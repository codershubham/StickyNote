package com.shubham.navinote;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    ListView list;
  //  final CharSequence[] sort = {"Name","Date & Time"};
    String sortBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = (ListView)findViewById(R.id.listView1);
        list.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "To add note go to menu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list.setAdapter(new NotesAdapter());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add) {
            startActivity(new Intent(this,AddNoteActivity.class));
        } else if (id == R.id.nav_delete) {
            startActivity(new Intent(this,DeleteNoteActivity.class));
        } else if (id == R.id.nav_search) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            final EditText edittext = new EditText(this);
            alert.setMessage("Search a File");
            alert.setTitle("Search");

            alert.setView(edittext);

            alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Editable atext = edittext.getText();

                    if(!atext.toString().equals(""))
                    startActivity((new Intent(MainActivity.this,SearchActivity.class)).putExtra("key",atext.toString()));
                    else
                    Toast.makeText(MainActivity.this, "Please do a valid search", Toast.LENGTH_SHORT).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        } else if (id == R.id.nav_sort) {
            final String[] course = {"Date & Time"};
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sort by ");
            builder.setSingleChoiceItems(course, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sortBy=course[which].toString();
                    Toast.makeText(MainActivity.this,"Sorted by "+sortBy,Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.create().show();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity((new Intent(this,AddNoteActivity.class))
                .putExtra("fileName",((TextView)view.findViewById(R.id.tvName)).getText().toString()));
    }

    class NotesAdapter extends BaseAdapter {
        ArrayList<Notes> noteList;
        SimpleDateFormat fmt;

        public NotesAdapter(){
            noteList = new ArrayList<>();
            fmt = new SimpleDateFormat("dd-MMMM-yyyy hh:mm a");
            File myFolder = new File(getFilesDir(),"notes");
            if(!myFolder.exists()){
                myFolder.mkdir();
            }
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
