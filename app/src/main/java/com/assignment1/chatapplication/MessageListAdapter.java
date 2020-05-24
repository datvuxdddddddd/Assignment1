package com.assignment1.chatapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


import androidx.recyclerview.widget.RecyclerView;

import static com.assignment1.chatapplication.MessageAttr.VIEW_TYPE_MESSAGE_RECEIVED;
import static com.assignment1.chatapplication.MessageAttr.VIEW_TYPE_MESSAGE_SENT;


public class MessageListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private List<MessageAttr> mMessageList;
    public MessageListAdapter(Context context, List<MessageAttr> messageList) {
        this.mContext = context;
        this.mMessageList = messageList;
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        MessageAttr message = mMessageList.get(position);

        if (message.getMsgType() == VIEW_TYPE_MESSAGE_SENT) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }



    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageAttr message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.sent_message_body);
            timeText = itemView.findViewById(R.id.sent_message_time);
        }

        void bind(MessageAttr message) {
            messageText.setText(message.getMsgContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.received_message_body);
            timeText = itemView.findViewById(R.id.received_message_time);
            nameText = itemView.findViewById(R.id.received_message_name);
        }

        void bind(MessageAttr message) {
            messageText.setText(message.getMsgContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTime());

            //Receive sender's name
            nameText.setText(message.getSender());
        }
    }
}

