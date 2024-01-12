package com.example.godee;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godee.ModelClass.MessageModel;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private String currentUserID;
    private String otherUserID = "2XSlmylRYNTcTjyr8LwFj5AcPgE3"; // Hardcoded other user ID
    private FirebaseFirestore db;
    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList;
    private static final String CHANNEL_ID = "chat_notifications";

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
        messageAdapter = new MessageAdapter(messageList, currentUserID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        Objects.requireNonNull(getSupportActionBar()).hide();
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
        createNotificationChannel();
        listenForMessages(); // Start listening for messages
    }
    private String generateChatId(String currentUserID, String otherUserID) {
        db = FirebaseFirestore.getInstance();
        ArrayList<String> al = new ArrayList<>();
        db.collection("drivers").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            al.add(document.getId());
//                            Log.d("TestChatActivity", document.getId() + " => " + document.getData());
                        }
                        Log.d("TestChatActivity", currentUserID + "_" + al.get(2));
                    } else {
                        Log.d("TestChatActivity", "Error getting documents: ", task.getException());
                    }
                });
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
        db.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e("TestChatActivity", "Error fetching messages: ", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                DocumentSnapshot document = dc.getDocument();
                                MessageModel message = document.toObject(MessageModel.class);
                                Log.d("TestChatActivity", "Fetched message: " + message.getText());
                                messageList.add(message);

                                // Check if the current user is not the sender of the message
                                if (!message.getSenderId().equals(currentUserID)) {
                                    createNotification(message.getText());
                                }
                            }
                        }
                        messageAdapter.notifyDataSetChanged();
                        if (messageList.size() > 0) {
                            recyclerView.smoothScrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void createNotification(String messageBody) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Specify the PendingIntent flag based on the Android version
        int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE :
                PendingIntent.FLAG_UPDATE_CURRENT;

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, flags);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.icon_send)
                        .setContentTitle("New Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }






}