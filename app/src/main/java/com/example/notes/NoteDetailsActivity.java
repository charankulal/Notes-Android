package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText=findViewById(R.id.notes_title_edit_text);
        contentEditText=findViewById(R.id.notes_content_edit_text);
        saveNoteBtn=findViewById(R.id.save_note_btn);

        saveNoteBtn.setOnClickListener((v) -> saveNote() );
    }

    private void saveNote() {
        String noteTitle= titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();

        // Validation for title: Title shouldn't be null

        if(noteTitle==null||noteTitle.isEmpty())
        {
            titleEditText.setError("Title is required!");
            return;
        }
    }
}