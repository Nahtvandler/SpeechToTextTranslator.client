package com.nahtvandler.SpeechToTextTranslator.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
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

public class CreateRoomActivity extends AppCompatActivity {

    private Button createRoomButton;
    private EditText roomNameTextField;
    private EditText roomInfoTextField;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room_activity);

        roomNameTextField = (EditText) findViewById(R.id.roomNameTextField);
        roomInfoTextField = (EditText) findViewById(R.id.roomInfoTextField);
    }


    public void doCreateRoom(View view) {

        RoomHttpDto roomHttpDto = new RoomHttpDto();
        roomHttpDto.setName(roomNameTextField.getText().toString());
        roomHttpDto.setDescription(roomInfoTextField.getText().toString());

        ClientHttpDto clientHttpDto = (ClientHttpDto) getIntent().getSerializableExtra("client");
        roomHttpDto.setOwner(clientHttpDto);

        ChatRoomApi api = NetworkService.getInstance().getChatRoomApi();
        Call call = api.createRoom(roomHttpDto);
        call.enqueue(new Callback<RoomHttpDto>() {
            @Override
            public void onResponse(Call<RoomHttpDto> call, Response<RoomHttpDto> response) {
                if (HttpURLConnection.HTTP_OK != response.code()) {
                    // TODO show error message
                    //showErrorMessage("network error");
                    return;
                }

                RoomHttpDto result = response.body();
                Intent intent = new Intent(CreateRoomActivity.this, MessageListActivity.class);
                intent.putExtra("room", result);
                intent.putExtra("login", clientHttpDto.getLogin());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RoomHttpDto> call, Throwable t) {
                // TODO show error message
//                showErrorMessage("network error");
                t.printStackTrace();
            }
        });
    }
}
