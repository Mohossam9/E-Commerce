package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class product_detailsActivity extends AppCompatActivity {

    TextView Product_name;
    TextView Product_description;
    TextView Product_price;
    TextView Product_quantity;
    Button addtocartbtn;
    Button showcartbtn;
    DatabaseCustomersHelper databaseCustomersHelper;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        final String name=getIntent().getExtras().getString("productname");
        String customername=getIntent().getExtras().getString("customername");

        databaseCustomersHelper=new DatabaseCustomersHelper(this);
        Product_name=(TextView)findViewById(R.id.Productnametxtview_productdetails_id);
        Product_description=(TextView)findViewById(R.id.productdescription_productdetails_id);
        Product_price=(TextView)findViewById(R.id.productPrice_productdetails_id);
        Product_quantity=(TextView) findViewById(R.id.Productquantity_productdetails_id);
        addtocartbtn=(Button)findViewById(R.id.Addtocartbtn_productdetails_id);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        ArrayList arrayList=databaseCustomersHelper.get_product_details(name);

        Product_name.setText(String.valueOf(arrayList.get(0)));
        Product_quantity.setText(String.valueOf(arrayList.get(1)));
        Product_description.setText(String.valueOf(arrayList.get(2)));
        Product_price.setText(String.valueOf(arrayList.get(3)));

        int customerid=databaseCustomersHelper.get_customer_id(customername);
        final int cartid=databaseCustomersHelper.get_cart_id(customerid);

        addtocartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(databaseCustomersHelper.insert_item_shoppingcart(Product_name.getText().toString(),cartid))
                Toast.makeText(getApplicationContext(),"Item is added to your cart" ,Toast.LENGTH_LONG).show();
                else
                {
                    Toast.makeText(getApplicationContext(),"No available items ",Toast.LENGTH_LONG).show();
                }
            }
        });




    }
}
