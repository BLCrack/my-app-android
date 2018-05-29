package com.findme.my_app_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.findme.my_app_android.interfaces.RESTAPInterface;
import com.findme.my_app_android.models.Device;
import com.findme.my_app_android.models.User;
import com.findme.my_app_android.models.UserCredentials;
import com.findme.my_app_android.security.TokenHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private RESTAPInterface restAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private User loginUser;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);


        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logowanie
                EditText loginEditText = findViewById(R.id.loginEditText);
                String login = loginEditText.getText().toString();
                EditText passwordEditText = findViewById(R.id.passwordEditText);
                String password = passwordEditText.getText().toString();
                Log.d("login: ", login);
                Log.d("password: ", password);
                UserCredentials.getInstance().setUsername(login);
                UserCredentials.getInstance().setPassword(password);

                login(UserCredentials.getInstance(), v);
            }
        });
    }

    private void login(UserCredentials userCredentials, final View v){
        Call<TokenHolder> call =  restAPI.login(userCredentials);
        call.enqueue(new Callback<TokenHolder>() {
            @Override
            public void onResponse(Call<TokenHolder> call, Response<TokenHolder> response) {
                //retrofit authentication token save
                if(response.isSuccessful()){
                    Log.d("token", response.body().getToken());
                    //add token
                    TokenHolder.getInstance().setToken(response.body().getToken());
                    openDeviceChoiceActivity();
                }
                else{
                    openAlert(v);
                }
            }

            @Override
            public void onFailure(Call<TokenHolder> call, Throwable t) {
                openAlert(v);
            }
        });

    }

    public void openDeviceChoiceActivity(){
        Intent intent = new Intent(this, DeviceChoiceActivity.class);
        startActivity(intent);
    }

    public void openAlert(View view)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Niepoprawne dane logowania!");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this,"Zaloguj się ponownie",Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}