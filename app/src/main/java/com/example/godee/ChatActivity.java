package com.example.godee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.godee.ModelClass.MessageModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private String currentUserID;
    private String otherUserID = "2XSlmylRYNTcTjyr8LwFj5AcPgE3"; // Hardcoded other user ID
    private FirebaseFirestore db;
    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        currentUserID = getIntent().getStringExtra("CURRENT_USER_ID");
        Log.d("test ID", currentUserID);
        db = FirebaseFirestore.getInstance();

        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.message_send_btn);

        recyclerView = findViewById(R.id.recycler_view_messages); //
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Add test messages
        messageList.add(new MessageModel("Test message 1", currentUserID, new Timestamp(new Date())));
        messageList.add(new MessageModel("Test message 2", currentUserID, new Timestamp(new Date())));
        messageAdapter.notifyDataSetChanged();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageInput.getText().toString();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                }
            }
        });

        listenForMessages(); // Start listening for messages
    }
    private String generateChatId(String currentUserID, String otherUserID) {
        if (currentUserID.compareTo(otherUserID) < 0) {
            return currentUserID + "_" + otherUserID;
        } else {
            return otherUserID + "_" + currentUserID;
        }
    }

    private void sendMessage(String text) {
        Map<String, Object> message = new HashMap<>();
        String chatId = generateChatId(currentUserID, otherUserID);
        message.put("senderId", currentUserID);
        message.put("text", text);
        message.put("timestamp", new Timestamp(new Date()));

        db.collection("chats").document(chatId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    // Clear the input field after sending
                    messageInput.setText("");
                })
                .addOnFailureListener(e -> {
                    // Handle sending failure
                });
    }

    private void listenForMessages() {
        String chatId = generateChatId(currentUserID, otherUserID);
        Log.d("TestChatID", "Listening for messages in chat: " + chatId);

        db.collection("chats").document(chatId)
                .collection("messages")
                //.orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("TestChatActivity", "Error fetching messages: ", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                DocumentSnapshot document = dc.getDocument();
                                MessageModel message = document.toObject(MessageModel.class);
                                Log.d("TestChatActivity", "Fetched message: " + message.getMessage());
                                messageList.add(message);
                            //}
                        }


                        messageAdapter.notifyDataSetChanged(); // Notify adapter of data change
                        if (messageList.size() > 0) {
                            recyclerView.smoothScrollToPosition(messageList.size() - 1); // Scroll to the bottom
                        }
                    }
                });
    }


}