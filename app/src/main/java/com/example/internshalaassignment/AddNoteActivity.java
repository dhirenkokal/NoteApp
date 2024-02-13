package com.example.internshalaassignment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class AddNoteActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText contentEditText;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleEditText = findViewById(R.id.editTextTitle);
        contentEditText = findViewById(R.id.editTextContent);
        databaseHelper = new DatabaseHelper(this);
    }

    public void saveNote(View view) {
        String newTitle = titleEditText.getText().toString().trim();
        String newContent = contentEditText.getText().toString().trim();
        if (!newTitle.isEmpty() && !newContent.isEmpty()) {
            NoteModel newNoteModel = new NoteModel(newTitle, newContent);
            long noteId = databaseHelper.addNote(newNoteModel);
            if (noteId != -1) {
                Toast.makeText(this, "NoteModel added successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add note", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}

