package com.nahtvandler.SpeechToTextTranslator.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nahtvandler.SpeechToTextTranslator.client.recycle.RecyclerViewActivity;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;

import org.w3c.dom.Text;


public class WorkspaceActivity extends AppCompatActivity {

    private TextView loginTextField;

    private TextView firstNameTextField;
    private TextView middleNameTextField;
    private TextView lastNameTextField;

    private Button pingButton;
    private TextView pingText;
    Activity activity;

    private Button openChatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_activity);
        activity = this;

        loginTextField = (TextView) findViewById(R.id.loginText);
        firstNameTextField = (TextView) findViewById(R.id.firstNameText);
        middleNameTextField = (TextView) findViewById(R.id.middleNameText);
        lastNameTextField = (TextView) findViewById(R.id.lastNameText);

        ClientHttpDto client = (ClientHttpDto) getIntent().getSerializableExtra("client");
        loginTextField.setText(client.getLogin());
        firstNameTextField.setText(client.getFirstName());
        middleNameTextField.setText(client.getMiddleName());
        lastNameTextField.setText(client.getLastName());

        pingText = (TextView) findViewById(R.id.pingTExt);
        pingButton = (Button) findViewById(R.id.pingButton);

        pingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = "/questions/" + 1;
                SpeechToTextTranslatorApplication.instance().getSubscriptions().addSubscription(topic, topicMessage -> {
                    Message message = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<Message>() {
                    }.getType());
                    activity.runOnUiThread(() -> {
                        pingText.setText(message.getMessage());
                    });
                });

                SpeechToTextTranslatorApplication.instance().getStompClient().send("/topic/ping", "ping").subscribe();
            }
        });

        openChatButton = (Button) findViewById(R.id.openChat);
        openChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkspaceActivity.this, MessageListActivity.class);
                intent.putExtra("login", client.getLogin());
                //Intent intent = new Intent(WorkspaceActivity.this, RecyclerViewActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.createRoomMenuItem:
                Intent createRoomIntent = new Intent(WorkspaceActivity.this, CreateRoomActivity.class);
                createRoomIntent.putExtra("client", getIntent().getSerializableExtra("client"));
                startActivity(createRoomIntent);
                return true;
            case R.id.findRoomMenuItem:
                // TODO start find room intent
                Intent findRoomIntent = new Intent(WorkspaceActivity.this, FindRoomActivity.class);
                findRoomIntent.putExtra("client", getIntent().getSerializableExtra("client"));
                startActivity(findRoomIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
