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
import com.findme.my_app_android.models.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private RESTAPInterface restAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private User loginUser;
    private List<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                //sprawdzenie czy istnieje taki użytkownik i jesli tak to zalogowanie
                boolean isRegistered = false;
                for(User u : users)
                {
                    if((u.getLogin().equals(login)) && (u.getPassword().equals(password)))
                        isRegistered = true;
                }

                if(isRegistered)
                    openAddDeviceActivity();
                else
                {
                    openAlert(v);
                }
            }
        });

        //Init API
        Retrofit retrofit = RetrofitClient.getInstance();
        restAPI = retrofit.create(RESTAPInterface.class);

        getAllUsers();
    }

    private void getAllUsers() {
        compositeDisposable.add(restAPI.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<User>>() {
                    @Override
                    public void accept(List<User> allusers) throws Exception {
                        users = allusers;
                        for(User u: allusers)
                            Log.d("DANE MOJE", u.getLogin());
                    }
                }));
    }

    public void openAddDeviceActivity(){
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
