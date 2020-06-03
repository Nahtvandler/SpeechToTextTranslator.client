package com.nahtvandler.SpeechToTextTranslator.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.RoomHttpDto;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    private EditText messageText;
    private Button sendButton;

    private List<Message> messageList = new ArrayList<>();
    Activity activity;
    String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        activity = this;

        messageText = (EditText) findViewById(R.id.edittext_chatbox);
        sendButton = (Button) findViewById(R.id.button_chatbox_send);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);

        User currentUser = new User();
        currentUser.setNickname(getIntent().getStringExtra("login"));

        mMessageAdapter = new MessageListAdapter(this, messageList, currentUser);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecycler.setAdapter(mMessageAdapter);

        RoomHttpDto roomHttpDto = (RoomHttpDto) getIntent().getSerializableExtra("room");

        setTitle("Room#" + roomHttpDto.getId());

//        //подписываемся на топик пинга
//        String topic = "/questions/" + 1;
//        SpeechToTextTranslatorApplication.instance().getSubscriptions().addSubscription(topic, topicMessage -> {
//            Message message = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<Message>() {
//            }.getType());
//            //mMessageAdapter.notifyItemInserted(messageList.size() - 1);
//            activity.runOnUiThread(() -> {
//                messageList.add(message);
//                mMessageAdapter.notifyDataSetChanged();
//
//            });
//        });
//        //-----------------------------------------
        topic = "/rooms/room#" + roomHttpDto.getId();
        SpeechToTextTranslatorApplication.instance().getSubscriptions().addSubscription(topic, topicMessage -> {
            Message message = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<Message>() {
            }.getType());

            if (message.getSender().getNickname().equals(currentUser.getNickname())) {
                return;
            }

            activity.runOnUiThread(() -> {
                messageText.setText("");
                messageList.add(message);
                mMessageAdapter.notifyDataSetChanged();
            });
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.setSender(currentUser);
                message.setMessage(messageText.getText().toString());
                message.setCreatedAt(System.currentTimeMillis());
                message.setChannelId(roomHttpDto.getId());

                String body = new Gson().toJson(message);
                SpeechToTextTranslatorApplication.instance().getStompClient().send("/topic/sendMessage", body).subscribe();

                messageList.add(message);
                mMessageAdapter.notifyDataSetChanged();
            }
        });
    }
}