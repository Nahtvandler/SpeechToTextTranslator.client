package com.nahtvandler.SpeechToTextTranslator.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nahtvandler.SpeechToTextTranslator.client.rest.ChatRoomApi;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.RoomHttpDto;
import com.nahtvandler.SpeechToTextTranslator.client.rest.NetworkService;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindRoomActivity extends AppCompatActivity {

    private EditText roomIdTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room);

        roomIdTextField = (EditText) findViewById(R.id.roomIdTextField);
    }


    public void doConnect(View view) {
        ChatRoomApi chatRoomApi = NetworkService.getInstance().getChatRoomApi();
        Call call = chatRoomApi.connect(roomIdTextField.getText().toString());
        call.enqueue(new Callback<RoomHttpDto>() {
            @Override
            public void onResponse(Call<RoomHttpDto> call, Response<RoomHttpDto> response) {
                if (HttpURLConnection.HTTP_OK != response.code()) {
                    // TODO show error message
                    //showErrorMessage("network error");
                    return;
                }

                RoomHttpDto result = response.body();
                ClientHttpDto currentUser = (ClientHttpDto) getIntent().getSerializableExtra("client");

                Intent intent = new Intent(FindRoomActivity.this, MessageListActivity.class);
                intent.putExtra("room", result);
                intent.putExtra("login", currentUser.getLogin());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RoomHttpDto> call, Throwable t) {
                //TODO show error message
                t.printStackTrace();
            }
        });
    }
}
