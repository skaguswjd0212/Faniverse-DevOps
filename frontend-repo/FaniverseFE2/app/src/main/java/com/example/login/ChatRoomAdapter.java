package com.example.login;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChatRoomAdapter extends ArrayAdapter<ChatRoom> {

    public ChatRoomAdapter(Context context, List<ChatRoom> chatRooms) {
        super(context, 0, chatRooms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRoom chatRoom = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_room, parent, false);
        }

        TextView roomNameTextView = convertView.findViewById(R.id.room_name);
        TextView sellerNameTextView = convertView.findViewById(R.id.seller_name);
        ImageView roomImageView = convertView.findViewById(R.id.room_image);

        roomNameTextView.setText(chatRoom.getName());
        sellerNameTextView.setText(chatRoom.getSellerName());

        // drawable 리소스 설정
        roomImageView.setImageResource(chatRoom.getImageResId());

        return convertView;
    }
}
