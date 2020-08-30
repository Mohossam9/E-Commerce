package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Shoppingcart_Activity extends AppCompatActivity {

    ListView products_listview;
    ArrayAdapter<String> products_adapter;
    DatabaseCustomersHelper databaseCustomersHelper;
    SharedPreferences sharedPreferences;
    ArrayList products;
    TextView pricetxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppingcart_);
        int cartid=getIntent().getExtras().getInt("cartid");

        products_listview=(ListView)findViewById(R.id.mycartlistview_shoppingcart_id);
        products_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        databaseCustomersHelper= new DatabaseCustomersHelper(this);
        products=databaseCustomersHelper.fetch_all_cart_products(cartid);
        products_listview.setAdapter(products_adapter);
        products_adapter.addAll(products);
        registerForContextMenu(products_listview);

        pricetxt=(TextView)findViewById(R.id.totalpriceTxt);
        float totalprice=databaseCustomersHelper.total_price(products,cartid);
        pricetxt.setText(String.valueOf(totalprice));


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String productname=((TextView)info.targetView).getText().toString();
        int cartid=getIntent().getExtras().getInt("cartid");

        databaseCustomersHelper=new DatabaseCustomersHelper(this);

        if(item.getItemId()==R.id.Deletequantitiy){

            products_adapter.remove(productname);
            products_adapter.notifyDataSetChanged();
            products.remove(productname);
            float totalprice=databaseCustomersHelper.total_price(products,cartid);
            pricetxt.setText(String.valueOf(totalprice));

            databaseCustomersHelper.delete_quantity(productname,cartid);
            Toast.makeText(getApplicationContext(),"Quantity Deleted",Toast.LENGTH_LONG).show();
            return true;

        }
        else if(item.getItemId()==R.id.Updatequantitiy){
            Intent i=new Intent(Shoppingcart_Activity.this,update_quantity.class);
            i.putExtra("cartid",cartid);
            i.putExtra("productname",productname);
            i.putExtra("quantity",databaseCustomersHelper.get_quantity_item(productname,cartid));
            i.putExtra("availquantity",databaseCustomersHelper.get_product_quantity(productname));
            startActivity(i);
            return  true;
        }
  return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.product_context,menu);
    }



}
