package com.hfad.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
     ListView listView ;
    static ArrayAdapter arrayadapter;
     static ArrayList<String> notes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences=getApplicationContext().getSharedPreferences("com.hfad.mynotes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set==null){
            notes.add("Example note...");
        }else{
            notes= new ArrayList<>(set);
        }
        listView=findViewById(R.id.listview);
        notes.add("Example note...");
      arrayadapter =new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,notes);
        listView.setAdapter(arrayadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent intent =new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteId",i);
                startActivity(intent);

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id) {
               final int itemtodelet=i;
                new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(itemtodelet);
                                arrayadapter.notifyDataSetChanged();
                                HashSet<String> set =new HashSet<>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

                return true;
            }
        });
    }

    //menu code

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
          return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.add_note){
            Intent intent =new Intent(getApplicationContext(),NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }else{return false;}
    }
}
