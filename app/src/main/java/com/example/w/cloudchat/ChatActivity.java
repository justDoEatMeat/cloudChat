package com.example.w.cloudchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private static int MAX_MESSAGE_LENGTH = 150;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");



    EditText mEditTextMessage;
    Button mSendButton;
    RecyclerView mMessagerRecycler;

    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mMessagerRecycler = findViewById(R.id.message_recycler);

        mMessagerRecycler.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this, messages);

        mMessagerRecycler.setAdapter(dataAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = mEditTextMessage.getText().toString();

                if (msg.equals("")) {
                    Toast.makeText(getApplicationContext(), "ВВедите сообщение", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (msg.length() == MAX_MESSAGE_LENGTH) {
                    Toast.makeText(getApplicationContext(), "Слишком длинное сообщение!", Toast.LENGTH_SHORT).show();
                    return;

                }

                myRef.push().setValue(msg);
                mEditTextMessage.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagerRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
