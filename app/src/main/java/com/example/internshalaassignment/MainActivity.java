package com.example.internshalaassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<NoteModel> noteModelList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        noteModelList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        noteModelList.addAll(databaseHelper.getAllNotes());

        noteAdapter = new NoteAdapter(noteModelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(noteAdapter);

        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                NoteModel selectedNoteModel = noteModelList.get(position);
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("note_id", selectedNoteModel.getId());
                startActivity(intent);
            }
        });


        noteAdapter.setOnItemDeleteListener(new NoteAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                deleteNote(position);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNoteActivity.class));
            }
        });
    }

    private void deleteNote(int position) {
        NoteModel noteModelToDelete = noteModelList.get(position);
        long noteId = noteModelToDelete.getId();
        databaseHelper.deleteNote(noteId);
        noteModelList.remove(position);
        noteAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "NoteModel deleted successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteModelList.clear();
        noteModelList.addAll(databaseHelper.getAllNotes());
        noteAdapter.notifyDataSetChanged();
    }
}
