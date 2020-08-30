package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class resetpassactivity extends AppCompatActivity {

    Button submitbtn;
    EditText newpass;
    EditText newanswer1;
    EditText newanswer2;
    DatabaseCustomersHelper databaseCustomersHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassactivity);


        submitbtn=(Button)findViewById(R.id.submitbtn_resertpass_id);
        newpass=(EditText)findViewById(R.id.newpasstxt_resertpass_id);
        newanswer1=(EditText)findViewById(R.id.newQ1answertxt_resetpass_id);
        newanswer2=(EditText)findViewById(R.id.newQ2answertxt_resetpass_id);
        databaseCustomersHelper=new DatabaseCustomersHelper(this);

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=getIntent().getExtras().getString("name");
                String pass=newpass.getText().toString();
                String ans1=newanswer1.getText().toString();
                String ans2=newanswer2.getText().toString();

                newpass.setText("");
                newanswer1.setText("");
                newanswer2.setText("");

                if(pass.equals("") || ans1.equals("") || ans2.equals(""))
                    Toast.makeText(getApplicationContext(),"Please complete all data !",Toast.LENGTH_LONG).show();
                else
                {
                    databaseCustomersHelper.update_pass_answers(username,pass,ans1,ans2);
                    Toast.makeText(getApplicationContext(),"Updated Successfully ",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(resetpassactivity.this,Home_Activity.class);
                    i.putExtra("name",username);
                    startActivity(i);
                }
            }
        });


    }
}
