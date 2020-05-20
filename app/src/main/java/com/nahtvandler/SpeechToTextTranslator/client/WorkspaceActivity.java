package com.nahtvandler.SpeechToTextTranslator.client;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;


public class WorkspaceActivity extends AppCompatActivity {

    private TextView loginTextField;

    private TextView firstNameTextField;
    private TextView middleNameTextField;
    private TextView lastNameTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workspace_activity);

        loginTextField = (TextView) findViewById(R.id.loginText);
        firstNameTextField = (TextView) findViewById(R.id.firstNameText);
        middleNameTextField = (TextView) findViewById(R.id.middleNameText);
        lastNameTextField = (TextView) findViewById(R.id.lastNameText);

        ClientHttpDto client = (ClientHttpDto) getIntent().getSerializableExtra("client");
        loginTextField.setText(client.getLogin());
        firstNameTextField.setText(client.getFirstName());
        middleNameTextField.setText(client.getMiddleName());
        lastNameTextField.setText(client.getLastName());
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
                // TODO start create intent
                return true;
            case R.id.findRoomMenuItem:
                // TODO start find room intent
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
