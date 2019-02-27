package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SingUp extends AppCompatActivity {

    TextView singup, singin;
    LinearLayout singUpLayout, singInLayout;
    Button btnSingin, btnSingUp;
    EditText singUpeditname, singUpeditpass, singIneditname, singIneditpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inti();
        checkUserIsLogin();

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singup.setTextSize(20);
                singup.setTextColor(getResources().getColor(R.color.colorPrimary));
                singin.setTextSize(18);
                singin.setTextColor(getResources().getColor(R.color.colorAccent));
                singInLayout.setVisibility(View.GONE);
                singUpLayout.setVisibility(View.VISIBLE);

            }
        });

        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singup.setTextSize(18);
                singup.setTextColor(getResources().getColor(R.color.colorAccent));

                singin.setTextSize(20);
                singin.setTextColor(getResources().getColor(R.color.colorPrimary));

                singUpLayout.setVisibility(View.GONE);
                singInLayout.setVisibility(View.VISIBLE);
            }
        });



        // Sing Up Logic
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog dialog = new ProgressDialog(SingUp.this);
                dialog.setMessage("Signing in");
                dialog.setCancelable(false);
                dialog.show();

                ParseUser user = new ParseUser();
                user.setUsername(singUpeditname.getText().toString());
                user.setPassword(singUpeditpass.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            dialog.hide();
                            FancyToast.makeText(getApplicationContext(),"SingUp Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                            goToSocial();
                        } else {
                            dialog.hide();
                            FancyToast.makeText(getApplicationContext(),"Error",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();
                        }
                    }
                });
            }
        });


        // Sing In Logic
        btnSingin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog dialog = new ProgressDialog(SingUp.this);
                dialog.setMessage("Signing in");
                dialog.setCancelable(false);
                dialog.show();
                ParseUser.logInInBackground(singIneditname.getText().toString(), singIneditpass.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {
                            dialog.hide();
                            FancyToast.makeText(getApplicationContext(),"LogIn Success",FancyToast.LENGTH_LONG,FancyToast.SUCCESS,false).show();
                            goToSocial();
                        } else {
                            dialog.hide();
                            FancyToast.makeText(getApplicationContext(),"Rong user name or password",FancyToast.LENGTH_LONG,FancyToast.ERROR,false).show();

                        }
                    }
                });
            }
        });


    }

    public void inti() {
        singup = findViewById(R.id.singup);
        singin = findViewById(R.id.singin);
        singUpLayout = findViewById(R.id.singuplayout);
        singInLayout = findViewById(R.id.singinlayout);

        btnSingUp = findViewById(R.id.singupbtn);
        btnSingin = findViewById(R.id.singinbtn);

        singUpeditname = findViewById(R.id.singupeditname);
        singUpeditpass = findViewById(R.id.singupeditpass);

        singIneditname = findViewById(R.id.singineditname);
        singIneditpass = findViewById(R.id.singineditpass);
    }

    public void goToSocial() {
        Intent i = new Intent(SingUp.this, SocilaMediaActivity.class);
        i.putExtra("name", ParseUser.getCurrentUser().getUsername());
        startActivity(i);
    }

    public void checkUserIsLogin() {
        if (ParseUser.getCurrentUser() != null) {
            goToSocial();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
//        if (ParseUser.getCurrentUser() != null)
//            finish();
    }
}
