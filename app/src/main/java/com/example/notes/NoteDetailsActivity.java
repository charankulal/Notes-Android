package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

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

        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);
    }

    private void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //Note is added to firebase
                    Utility.showToast(NoteDetailsActivity.this,"Note added successfully");
                }
                else {
                    //If adding is failed
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding Note");
                }
            }
        });
    }
}