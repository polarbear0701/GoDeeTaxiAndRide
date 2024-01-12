package com.example.godee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.godee.ModelClass.MessageModel;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessageModel> messageList;
    private String currentUserId;

    public MessageAdapter(List<MessageModel> messageList, String currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        MessageModel message = messageList.get(position);

        if(message.getSenderId().equals(currentUserId)){
            // If the current user is the sender
            holder.textMessageSender.setText(message.getText());
            holder.textMessageSender.setVisibility(View.VISIBLE);
            holder.textMessageReceiver.setVisibility(View.GONE);
        } else {
            // If the current user is the receiver
            holder.textMessageReceiver.setText(message.getText());
            holder.textMessageReceiver.setVisibility(View.VISIBLE);
            holder.textMessageSender.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessageSender;
        TextView textMessageReceiver;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textMessageSender = itemView.findViewById(R.id.text_message_sender);
            textMessageReceiver = itemView.findViewById(R.id.text_message_receiver);
        }
    }
}

