package com.example.e_commerce;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Home_Activity extends AppCompatActivity {

    TextView welcome;
    public static EditText searchbytext;
    Button logout;
    Button showcartbtn;
    ImageButton searchbyvoicebtn;
    Button barcodebtn;
    ListView products_listview;
    ArrayAdapter<String> products_adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseCustomersHelper databaseCustomersHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
        final String customername=getIntent().getExtras().getString("name");

        products_listview=(ListView)findViewById(R.id.productslistview_homeactivity_id);
        databaseCustomersHelper=new DatabaseCustomersHelper(this);
        searchbytext=(EditText)findViewById(R.id.searchtxt_homaactivity_id);


        final ArrayList products_arrlist=databaseCustomersHelper.fetch_all_products();
        products_adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,products_arrlist);
        products_listview.setAdapter(products_adapter);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        editor=sharedPreferences.edit();


        searchbytext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                products_adapter.clear();
               ArrayList search_list=databaseCustomersHelper.search_by_text(String.valueOf(s));
               products_adapter.addAll(search_list);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.equals(""))
                {
                    products_adapter.clear();
                    products_adapter.addAll(products_arrlist);
                }
            }
        });


        logout=(Button)findViewById(R.id.LogOutbtn);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(getString(R.string.user_name),"");
                editor.putString(getString(R.string.user_pass),"");
                editor.putString("user_id","");
                editor.commit();
                Intent i = new Intent(Home_Activity.this,signinactivity.class);
                startActivity(i);
            }
        });

        searchbyvoicebtn=(ImageButton)findViewById(R.id.Searchbyvoicebtn_homeActivity_id);

        searchbyvoicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                startActivityForResult(i, 200);
            }
        });

        products_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String productname=((TextView)view).getText().toString();

                Intent i =new Intent(Home_Activity.this,product_detailsActivity.class);
                i.putExtra("productname",productname);
                i.putExtra("customername",customername);
                startActivity(i);
            }
        });

        showcartbtn=(Button)findViewById(R.id.Showcartbtn_HomeActivity_id);
        int customerid=databaseCustomersHelper.get_customer_id(customername);
        final int cartid=databaseCustomersHelper.get_cart_id(customerid);

        showcartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Home_Activity.this,Shoppingcart_Activity.class);
                i.putExtra("cartid",cartid);
                startActivity(i);
            }
        });

        barcodebtn=(Button)findViewById(R.id.Searchbybarcodebtn_homeActivity_id);
        barcodebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // Intent i = new Intent(Home_Activity.this,Scanbybarcode_Activity.class);
               // startActivity(i);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==200 && resultCode==RESULT_OK && data!=null)
        {
            ArrayList<String> text=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            searchbytext.setText(text.get(0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.useroptions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        ArrayList arr=new ArrayList();
        databaseCustomersHelper=new DatabaseCustomersHelper(this);
        products_listview=(ListView)findViewById(R.id.productslistview_homeactivity_id);
        products_adapter.clear();

        switch (item.getItemId())
        {
            case R.id.Laptopsid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(1);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Lenovolaptopid:
            {
                arr=databaseCustomersHelper.search_by_text("Lenovo");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Delllaptopid:
            {
                arr=databaseCustomersHelper.search_by_text("Dell");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Asuslaptopid:
            {
                arr=databaseCustomersHelper.search_by_text("Asus");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Hplaptopid:
            {
                arr=databaseCustomersHelper.search_by_text("Hp");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Toshibalaptopid:
            {
                arr=databaseCustomersHelper.search_by_text("Toshiba");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.MobileDevicesid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(2);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Htcmobileid:
            {
                arr=databaseCustomersHelper.search_by_text("Htc");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Huawaimobileid:
            {
                arr=databaseCustomersHelper.search_by_text("Huawai");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Iphonemobileid:
            {
                arr=databaseCustomersHelper.search_by_text("Iphone");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Nokiamobileid:
            {
                arr=databaseCustomersHelper.search_by_text("Nokia");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Samsungmobileid:
            {
                arr=databaseCustomersHelper.search_by_text("Samsung");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Accessoriesid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(3);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Menaccessoriesid:
            {
                arr=databaseCustomersHelper.search_by_text("Men Accessory");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Womenaccessoriesid:
            {
                arr=databaseCustomersHelper.search_by_text("Women Accessory");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Clothesid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(4);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Menclothesid:
            {
                arr=databaseCustomersHelper.search_by_text("Men clothe");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Womenclothesid:
            {
                arr=databaseCustomersHelper.search_by_text("Women clothe");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Perfumesid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(5);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Menperfumesid:
            {
                arr=databaseCustomersHelper.search_by_text("Men perfume");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Womenperfumesid:
            {
                arr=databaseCustomersHelper.search_by_text("Women perfume");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Carsid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(6);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Bmwcarid:
            {
                arr=databaseCustomersHelper.search_by_text("Bmw");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Mercedescarid:
            {
                arr=databaseCustomersHelper.search_by_text("Mercedes");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Skodacarid:
            {
                arr=databaseCustomersHelper.search_by_text("Skoda");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Jeepcarid:
            {
                arr=databaseCustomersHelper.search_by_text("Jeep");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Hyundaicarid:
            {
                arr=databaseCustomersHelper.search_by_text("Hyundai");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Kiacarid:
            {
                arr=databaseCustomersHelper.search_by_text("Kia");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Booksid:
            {
                arr=databaseCustomersHelper.get_specific_category_products(7);
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Romancebookid:
            {
                arr=databaseCustomersHelper.search_by_text("Romance");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Dramabookid:
            {
                arr=databaseCustomersHelper.search_by_text("Drama");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Sciencebookid:
            {
                arr=databaseCustomersHelper.search_by_text("Science");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.Healthbookid:
            {
                arr=databaseCustomersHelper.search_by_text("Health");
                products_adapter.addAll(arr);
                return true;
            }
            case R.id.CrimeandDetectivebookid:
            {
                arr=databaseCustomersHelper.search_by_text("Crime");
                products_adapter.addAll(arr);
                return true;
            }

        }
        return false;
    }


}
