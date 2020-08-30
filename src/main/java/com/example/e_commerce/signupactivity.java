package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class signupactivity extends AppCompatActivity {

    Button registbtn;
    EditText customername;
    EditText username;
    EditText password;
    EditText date;
    RadioButton male;
    RadioButton female;
    EditText job;
    EditText answer1;
    EditText answer2;
    Calendar mycalender;
    DatabaseCustomersHelper customersHelper;

    //related to calender code 
    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(mycalender.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);

        registbtn=(Button)findViewById(R.id.Registerbtn);
        customername=(EditText)findViewById(R.id.customernametxt);
        username=(EditText)findViewById(R.id.usernametxt);
        password=(EditText)findViewById(R.id.passwordtxt);
        date=(EditText)findViewById(R.id.birthdatetxt);
        male=(RadioButton) findViewById(R.id.maleradiobutton);
        female=(RadioButton) findViewById(R.id.Femaleradiobutton);
        job=(EditText)findViewById(R.id.jobtxt);
        answer1=(EditText)findViewById(R.id.Question1answertxt_signup_id);
        answer2=(EditText)findViewById(R.id.Question2answertxt_signup_id);
        mycalender=Calendar.getInstance();
        customersHelper=new DatabaseCustomersHelper(this);

        //calender code
        final DatePickerDialog.OnDateSetListener chosendate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                mycalender.set(Calendar.YEAR, year);
                mycalender.set(Calendar.MONTH, monthOfYear);
                mycalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(signupactivity.this, chosendate, mycalender.get(Calendar.YEAR), mycalender.get(Calendar.MONTH), mycalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        //when click on regist button
        registbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(customername.getText().toString().equals("") || username.getText().toString().equals("") || password.getText().toString().equals("")  ||date.getText().toString().equals("") || job.getText().toString().equals("") || answer1.getText().toString().equals("") || answer2.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please Complete all data first !",Toast.LENGTH_LONG).show();
                    return;
                }
                boolean existance=customersHelper.check_exist_username(username.getText().toString());
                if(existance)
                    Toast.makeText(getApplicationContext(),"This name is already taken ! try another username", Toast.LENGTH_LONG).show();
                else
                {
                    String gender="";
                    if (male.isChecked())
                    {
                        gender="Male";
                    }
                    else if(female.isChecked())
                    {
                        gender="Female";
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Please Complete all data first !",Toast.LENGTH_LONG).show();
                        return;
                    }
                    boolean insertion=customersHelper.insert_customer(customername.getText().toString(),username.getText().toString(),password.getText().toString(),gender,date.getText().toString(),job.getText().toString(),answer1.getText().toString(),answer2.getText().toString());
                    String name=username.getText().toString();
                    username.setText("");
                    password.setText("");
                    customername.setText("");
                    job.setText("");
                    date.setText("");
                    if(insertion) {
                        Toast.makeText(getApplicationContext(), "Registeration is completed ", Toast.LENGTH_LONG).show();
                        int custid=customersHelper.get_customer_id(name);
                        customersHelper.insert_Shopping_cart(custid);
                        Intent i = new Intent(signupactivity.this, signinactivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error Occured in insertion ",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
