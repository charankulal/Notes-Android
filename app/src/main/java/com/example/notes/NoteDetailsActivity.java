package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn,deleteNoteBtn;
    TextView pageTitle;
    Button deleteNoteTextView;

    String title,content,docId;

    boolean isEditMode=false;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText=findViewById(R.id.notes_title_edit_text);
        contentEditText=findViewById(R.id.notes_content_edit_text);
        saveNoteBtn=findViewById(R.id.save_note_btn);
        pageTitle=findViewById(R.id.page_title);
        deleteNoteTextView=findViewById(R.id.delete_note_text_view_btn);
        deleteNoteBtn=findViewById(R.id.delete_note_btn);

        // Receiving data via intent

        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");

        if (docId!=null&& !docId.isEmpty())
        {
            isEditMode=true;

        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(isEditMode)
        {
            pageTitle.setText("Edit your Note");
            deleteNoteTextView.setVisibility(View.VISIBLE);
        }


        saveNoteBtn.setOnClickListener((v) -> saveNote() );

        deleteNoteTextView.setOnClickListener((v)->deleteNotefromFirebase());
        deleteNoteBtn.setOnClickListener((v)->deleteNotefromFirebase());
    }

    private void deleteNotefromFirebase() {
        DocumentReference documentReference;
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    //Note is added to firebase
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted successfully");
                }
                else {
                    //If adding is failed
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting the Note");
                }
                startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
                finish();
            }

        });

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
        if(isEditMode){
            // Update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }else {
            //Create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
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
                startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
            }

        });
    }

}