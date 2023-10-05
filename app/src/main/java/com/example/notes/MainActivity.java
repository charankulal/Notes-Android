package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuButton;
    NoteAdapter noteAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn=findViewById(R.id.add_note_button);
        recyclerView=findViewById(R.id.recycler_view);
        menuButton=findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this,NoteDetailsActivity.class)));
        menuButton.setOnClickListener((v)->showMenu());
        showNotes();
    }

    private void showNotes() {

        Query query=Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter=new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    private void showMenu() {
        //TODO: display menu
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}