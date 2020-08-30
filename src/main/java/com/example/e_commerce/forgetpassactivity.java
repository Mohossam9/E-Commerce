package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class forgetpassactivity extends AppCompatActivity {

    EditText username;
    EditText answer1;
    EditText answer2;
    Button submitbtn;
    DatabaseCustomersHelper databaseCustomersHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassactivity);

        username=(EditText)findViewById(R.id.usernametxt_forgetpass_id);
        answer1=(EditText)findViewById(R.id.Question1answer_forgetpass_id);
        answer2=(EditText)findViewById(R.id.Question2answer_forgetpass_id);
        submitbtn=(Button)findViewById(R.id.submitbtn_forgetpass_id);

        databaseCustomersHelper=new DatabaseCustomersHelper(this);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=username.getText().toString();
                String ans1=answer1.getText().toString();
                String ans2=answer2.getText().toString();

                username.setText("");
                answer1.setText("");
                answer2.setText("");

                if(name.equals("") || ans1.equals("") || ans2.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please complete all data requirements !",Toast.LENGTH_LONG).show();
                }
                else
                {
                    boolean result=databaseCustomersHelper.checkanswers(name,ans1,ans2);
                    if(result)
                    {
                        Intent i = new Intent(forgetpassactivity.this,resetpassactivity.class);
                        i.putExtra("name",name);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Invalid Data Please try again !",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
