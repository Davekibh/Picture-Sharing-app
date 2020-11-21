package com.example.insta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{

    Boolean signUpModeActive = true;
    TextView loginTextView;
    EditText passwordEditText;
    EditText usernameEditText;

    public void showUserList(){

        Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN){

            signUp(view);

        }

        return false;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.loginTextView){

            Button signUpButton = findViewById(R.id.signUpButton);

            if (signUpModeActive){

                signUpModeActive = false;
                signUpButton.setText("Login");
                loginTextView.setText("Or, SignUp");

            }else{

                signUpModeActive = true;
                signUpButton.setText("SignUp");
                loginTextView.setText("Or, Login");

            }

        }else if (view.getId() == R.id.background || view.getId() == R.id.imageView){

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }

    }

    public void signUp(View view){

        usernameEditText = findViewById(R.id.usernameEditText);


        if (usernameEditText.getText().toString() == "" || passwordEditText.getText().toString() == ""){

            Toast.makeText(this, "A Username and Password is required!", Toast.LENGTH_SHORT).show();

        }else{

            if (signUpModeActive) {

                ParseUser user = new ParseUser();

                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Toast.makeText(MainActivity.this, "SignUp Successful!!", Toast.LENGTH_SHORT).show();
                            showUserList();

                        } else {

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }else{

                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {


                        if (user != null){

                            Toast.makeText(MainActivity.this, "Login Successful!!", Toast.LENGTH_SHORT).show();
                            showUserList();

                        }else{

                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("Instagram");

        loginTextView = findViewById(R.id.loginTextView);

        loginTextView.setOnClickListener(this);

        passwordEditText = findViewById(R.id.passwordEditText);

        passwordEditText.setOnKeyListener(this);

        ConstraintLayout background = findViewById(R.id.background);

        ImageView imageView = findViewById(R.id.imageView);

        background.setOnClickListener(this);

        imageView.setOnClickListener(this);

        //ParseInstallation.getCurrentInstallation().saveInBackground();

        if (ParseUser.getCurrentUser() != null){

            showUserList();

        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }
}