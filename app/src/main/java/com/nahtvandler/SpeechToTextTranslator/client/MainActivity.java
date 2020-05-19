package com.nahtvandler.SpeechToTextTranslator.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.ClientHttpDto;
import com.nahtvandler.SpeechToTextTranslator.client.rest.DTOs.LoginPair;
import com.nahtvandler.SpeechToTextTranslator.client.rest.LoginApi;
import com.nahtvandler.SpeechToTextTranslator.client.rest.NetworkService;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button registrationButton;
    private Button backButton;

    private TextView loginTextField;
    private TextView passwordTextField;

    private TextView firstNameTExtView;
    private TextView firstNameTextField;
    private TextView middleNameTextView;
    private TextView middleNameTextField;
    private TextView lastNameTExtView;
    private TextView lastNameTextField;

    private TextView errorTextView;

    private List<TextView> registrationWidgets = new ArrayList<>();

    private boolean isRegistrationMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        registrationButton = (Button) findViewById(R.id.registrationButton);
        backButton = (Button) findViewById(R.id.backButton);

        loginTextField = (TextView) findViewById(R.id.loginTextField);
        loginTextField.requestFocus();
        passwordTextField = (TextView) findViewById(R.id.passwordTextField);

        firstNameTExtView = (TextView) findViewById(R.id.firstNameTextView);
        firstNameTextField = (TextView) findViewById(R.id.firstNameText);
        middleNameTextView = (TextView) findViewById(R.id.middleNameTextView);
        middleNameTextField = (TextView) findViewById(R.id.middleNameText);
        lastNameTExtView = (TextView) findViewById(R.id.lastNameTextView);
        lastNameTextField = (TextView) findViewById(R.id.lastNameText);
        initRegistrationWidgetsList();
        hideRegistrationWidgets(true);

        errorTextView = (TextView) findViewById(R.id.errorTextView);
        hideErrorMessage();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorMessage();
                String login = loginTextField.getText().toString();
                String password = passwordTextField.getText().toString();
                doLogin(login, password);
            }
        });

        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorMessage();
                if (!isRegistrationMode) {
                    changeRegistrationMode(true);
                } else {
                    ClientHttpDto request = new ClientHttpDto();
                    request.setLogin(loginTextField.getText().toString());
                    request.setFirstName(firstNameTextField.getText().toString());
                    request.setMiddleName(middleNameTextField.getText().toString());
                    request.setLastName(lastNameTextField.getText().toString());
                    request.setPassword(passwordTextField.getText().toString());

                    doRegistration(request);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideErrorMessage();
                changeRegistrationMode(false);
            }
        });
    }

    public void initRegistrationWidgetsList() {
        registrationWidgets = Arrays.asList(
                firstNameTExtView,
                firstNameTextField,
                middleNameTextView,
                middleNameTextField,
                lastNameTExtView,
                lastNameTextField,
                backButton
        );
    }

    public void hideRegistrationWidgets(boolean status) {
        if (status) {
            registrationWidgets.forEach(p -> {
                p.setVisibility(View.GONE);
            });
        } else {
            registrationWidgets.forEach(p -> {
                p.setVisibility(View.VISIBLE);
            });
        }
    }

    public void changeRegistrationMode(boolean status) {
        if (status) {
            registrationButton.setText("confirm");
            hideRegistrationWidgets(false);
            loginButton.setVisibility(View.GONE);
        } else {
            registrationButton.setText("registration");
            hideRegistrationWidgets(true);
            loginButton.setVisibility(View.VISIBLE);
        }
        isRegistrationMode = status;
    }

    public void clearFields() {
        List<TextView> textFields = Arrays.asList(
                firstNameTExtView,
                firstNameTextField,
                middleNameTextView,
                middleNameTextField,
                lastNameTExtView,
                lastNameTextField,
                loginTextField,
                passwordTextField
        );
        textFields.forEach(p -> {
            p.setText("");
        });
    }

    public void showErrorMessage(String message) {
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(message);
    }

    public void hideErrorMessage() {
        errorTextView.setVisibility(View.GONE);
    }

    private void doRegistration(ClientHttpDto request) {
        LoginApi loginApi = NetworkService.getInstance().getLoginApi();
        Call call = loginApi.registeration(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (HttpURLConnection.HTTP_OK != response.code()) {
                    showErrorMessage("network error");
                    return;
                }

                changeRegistrationMode(false);
                clearFields();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                showErrorMessage("network error");
                t.printStackTrace();
            }
        });
    }

    private void doLogin(String login, String password) {
        LoginApi loginApi = NetworkService.getInstance().getLoginApi();
        Call call = loginApi.login(login, password);
        call.enqueue(new Callback<LoginPair>() {
            @Override
            public void onResponse(@NonNull Call<LoginPair> call, @NonNull Response<LoginPair> response) {
                if (HttpURLConnection.HTTP_OK != response.code()) {
                    showErrorMessage("network error");
                    return;
                }

                LoginPair pair = response.body();
                String status = pair.getStatus();
                if (status.equals(LoginStatusEnum.SUCCES.getStatus())) {
                    Intent intent = new Intent(MainActivity.this, WorkspaceActivity.class);
                    intent.putExtra("client", pair.getClient());
                    startActivity(intent);
                } else if (status.equals(LoginStatusEnum.CLIENT_NOT_FOUND.getStatus())) {
                    showErrorMessage("client not found");
                } else if (status.equals(LoginStatusEnum.INCORRECT_PASSWORD.getStatus())) {
                    showErrorMessage("incorrect password");
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                showErrorMessage("network error");
                t.printStackTrace();
            }
        });
    }


}
