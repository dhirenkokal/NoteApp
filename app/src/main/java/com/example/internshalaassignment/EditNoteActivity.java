package com.example.internshalaassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditNoteActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText contentEditText;
    private DatabaseHelper databaseHelper;
    private NoteModel currentNoteModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        titleEditText = findViewById(R.id.editTextTitle);
        contentEditText = findViewById(R.id.editTextContent);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        if (intent.hasExtra("note_id")) {
            long noteId = intent.getLongExtra("note_id", -1);
            currentNoteModel = databaseHelper.getNoteById(noteId);

            titleEditText.setText(currentNoteModel.getTitle());
            contentEditText.setText(currentNoteModel.getContent());
        }
    }

    public void saveNote(View view) {
        String updatedTitle = titleEditText.getText().toString().trim();
        String updatedContent = contentEditText.getText().toString().trim();

        if (!updatedTitle.isEmpty() && !updatedContent.isEmpty()) {
            currentNoteModel.setTitle(updatedTitle);
            currentNoteModel.setContent(updatedContent);

            int rowsAffected = databaseHelper.updateNote(currentNoteModel);

            if (rowsAffected > 0) {
                Toast.makeText(this, "NoteModel updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update note", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}

