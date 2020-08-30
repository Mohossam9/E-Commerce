package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class update_quantity extends AppCompatActivity {

    DatabaseCustomersHelper databaseCustomersHelper;
    TextView oldqun;
    TextView availqun;
    EditText quantitytxt;
    Button updatebtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_quantity);
        int quantity=getIntent().getExtras().getInt("quantity");
        int availquantity=getIntent().getExtras().getInt("availquantity");

        databaseCustomersHelper=new DatabaseCustomersHelper(this);
        quantitytxt=(EditText)findViewById(R.id.newquantityTxt);
        updatebtn=(Button)findViewById(R.id.updatequantityBtn);
        oldqun=(TextView)findViewById(R.id.currentquantityTxt);
        availqun=(TextView)findViewById(R.id.dbquantityTxt);
        oldqun.setText(String.valueOf(quantity));
        availqun.setText(String.valueOf(availquantity));

        quantitytxt.setText(String.valueOf(quantity));

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qun=Integer.parseInt(quantitytxt.getText().toString());
                if(databaseCustomersHelper.update_item_quantity(getIntent().getExtras().getString("productname"),getIntent().getExtras().getInt("cartid"),qun-1));
                Toast.makeText(getApplicationContext(),"updated ",Toast.LENGTH_LONG).show();
            }
        });


    }
}
