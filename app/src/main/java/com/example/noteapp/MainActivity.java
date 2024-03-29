package com.example.noteapp;

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
    ListView listView;
    static ArrayAdapter arrayAdapter;
    AlertDialog.Builder alert;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.add_note){
            Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    static ArrayList<String> notes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listitem);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set==null){
            notes.add("Example notes");
        }
        else{
            notes=new ArrayList<>(set);
        }

         arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NoteEditor.class);
                intent.putExtra("noteid",position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Are you sure to delete");
                alert.setIcon(R.drawable.ic_launcher_background);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.noteapp", Context.MODE_PRIVATE);
                        HashSet<String> set = new HashSet(MainActivity.notes);
                        sharedPreferences.edit().putStringSet("notes",set).apply();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
                  return true;

               }




        });
    }
}
