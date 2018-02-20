package com.example.syamanth.chat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Date;

public class MainActivity extends AppCompatActivity {


    EditText input;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.send)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (input.getText().toString().trim().length() != 0) {

                            sendMessage(new Chat(new Date().getTime(), input.getText().toString().trim()));
                        } else
                            Toast.makeText(MainActivity.this, "Message Can't be empty", Toast.LENGTH_SHORT).show();
                    }
                });

        input = (EditText) findViewById(R.id.input);

        recyclerView.setAdapter(new FireChatAdapter(FirebaseDatabase.getInstance().getReference("Messages"), Chat.class));
    }

    private void sendMessage(Chat input) {

        FirebaseDatabase.getInstance().getReference("Messages")
                .push().setValue(input)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    class FireChatAdapter extends FirebaseRecyclerAdapter<FireChatAdapter.FireChatViewHolder, Chat>

    {

        public FireChatAdapter(Query query, Class<Chat> itemClass) {
            super(query, itemClass);
        }

        @Override
        public FireChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FireChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_tile, parent, false));
        }

        @Override
        public void onBindViewHolder(FireChatViewHolder holder, int position) {

            try {

                Chat current = getItem(position);
                if (current != null) {
                    holder.content.setText(current.content);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void itemAdded(Chat item, String key, int position) {
            recyclerView.scrollToPosition(position);
        }

        @Override
        protected void itemChanged(Chat oldItem, Chat newItem, String key, int position) {

        }

        @Override
        protected void itemRemoved(Chat item, String key, int position) {

        }

        @Override
        protected void itemMoved(Chat item, String key, int oldPosition, int newPosition) {

        }

        class FireChatViewHolder extends RecyclerView.ViewHolder {

            TextView content;

            public FireChatViewHolder(View itemView) {
                super(itemView);
                content = (TextView) itemView.findViewById(R.id.content);
            }
        }
    }
}
