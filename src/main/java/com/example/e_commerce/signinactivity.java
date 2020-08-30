package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class signinactivity extends AppCompatActivity {

    Button loginbtn;
    Button signupbtn;
    EditText username;
    EditText password;
    TextView forgetpass;
    CheckBox rememberme;
    DatabaseCustomersHelper databaseCustomersHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinactivity);


        loginbtn=(Button)findViewById(R.id.Signinbtn_signinform);
        username=(EditText)findViewById(R.id.username_signin_txt);
        password=(EditText)findViewById(R.id.password_sigin_txt);
        forgetpass=(TextView)findViewById(R.id.Forgetpassbtn_signin_id);
        rememberme=(CheckBox)findViewById(R.id.remembermebox) ;
        signupbtn=(Button)findViewById(R.id.Signupbtn);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();
        databaseCustomersHelper=new DatabaseCustomersHelper(getApplicationContext());

        checkpreferences();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                if (name == "" || pass == "") {
                    Toast.makeText(getApplicationContext(), "Please Enter All data !", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean authen = databaseCustomersHelper.authentication(name, pass);

                if (rememberme.isChecked() && authen)
                {
                    setremeber(name,pass);
                }
                else{
                    removeremember();
                }

                username.setText("");
                password.setText("");



                if (authen) {
                    Toast.makeText(getApplicationContext(), "Welcome to app", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(signinactivity.this, Home_Activity.class);
                    i.putExtra("name", name);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Data please try again ", Toast.LENGTH_LONG).show();
                }
            }
        });


        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(signinactivity.this,signupactivity.class);
                startActivity(i);
            }
        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent i =new Intent(signinactivity.this,forgetpassactivity.class);
             startActivity(i);
            }
        });

    }

    private void setremeber(String name ,String pass)
    {
        editor.putString(getString(R.string.user_name), name);
        editor.commit();

        editor.putString(getString(R.string.user_pass), pass);
        editor.commit();
    }

    private void removeremember()
    {
        editor.putString(getString(R.string.user_name), "");
        editor.commit();

        editor.putString(getString(R.string.user_pass),"");
        editor.commit();
    }

    private void checkpreferences()
    {
        String name=sharedPreferences.getString(getString(R.string.user_name),"");
        String pass=sharedPreferences.getString(getString(R.string.user_pass),"");

        if(!name.equals("") && !pass.equals(""))
        {
            Intent i = new Intent(signinactivity.this,Home_Activity.class);
            i.putExtra("name", name);
            startActivity(i);
        }
    }
}
