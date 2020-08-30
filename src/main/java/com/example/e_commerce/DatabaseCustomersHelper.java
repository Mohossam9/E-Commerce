package com.example.e_commerce;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CalendarContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseCustomersHelper extends SQLiteOpenHelper {
    private static String databasename="e^com";
    SQLiteDatabase Ecommerce;

    public DatabaseCustomersHelper(@Nullable Context context) {
        super(context, databasename, null, 55);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table customers (customerid integer primary key autoincrement , customername text not null , username text unique not null , password text not null , gender varchar(6) not null , birthdate text not null , job text not null , answer1 text not null ,answer2 text not null);");
        db.execSQL("create table categories(categoryid integer Primary Key ,categoryname text not null);");
        db.execSQL("create table products(productid integer primary key autoincrement , productname text unique not null , image text not null , description text not null,price text not null, quantity integer not null , categoryid integer not null,FOREIGN KEY(categoryid) REFERENCES categories(categoryid));");
        db.execSQL("create table shoppingcart(cartid integer primary key autoincrement ,customerid integer not null, FOREIGN KEY(customerid) REFERENCES customers(customerid))");
        db.execSQL("create table cartitems(itemid integer primary key autoincrement ,productid integer not null ,quantity integer not null ,cartid integer not null,FOREIGN KEY(cartid) REFERENCES shoppingcart(cartid),FOREIGN KEY(productid) REFERENCES products(productid))");
        /*db.execSQL("create table orders (orderid integer primary key autoincrement , orderdate text not null , orderaddress text not null , customerid integer not null ,FOREIGN KEY(customerid) REFERENCES customers (customerid));");
        db.execSQL("create table orderdetails (quantity text not null ,orderid integer not null,productid integer not null, Primary Key (orderid,productid),FOREIGN KEY(orderid) REFERENCES orders (orderid),FOREIGN KEY(productid) REFERENCES products (productid));"); */


        insert_category(db,"laptops",1);
        insert_category(db,"Mobiledevices",2);
        insert_category(db,"Accessories",3);
        insert_category(db,"Clothes",4);
        insert_category(db,"Perfumes",5);
        insert_category(db,"Cars",6);
        insert_category(db,"Books",7);

        insert_product(db,"Lenovo Legion Y520","Lenovo-Legion-Y520-pic","Lenovo Legion Y520-15IKBN Intel Core I7-7700HQ 16GB Ram 1TB+128GB SSD VGA GTX 1050 4GB WIN 10","195000",5,1);
        insert_product(db,"Samsung A70","Samsung-A70-pic","Dual SIM ROM: 128GB - RAM: 6GB 4G LTE - Display: 6.7 inch Full HD -Battery: 4,500mAh - Rear Camera: 32MP + 8MP + 5MP - Front Camera: 32MP" ,"7500",15,2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists customers ");
        db.execSQL("drop table if exists categories ");
        db.execSQL("drop table if exists cartitems ");
        db.execSQL("drop table if exists products ");
        db.execSQL("drop table if exists shoppingcart ");
        onCreate(db);
    }

    public boolean authentication(String user_name,String password)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select username,password from customers where username = ? and password = ? ",new String[]{user_name,password});
        cur.moveToFirst();
        Ecommerce.close();
        if(cur.getCount()!=0)
        {
            return true;
        }
        return false;
    }

    public boolean insert_customer(String customername, String username, String password ,String gender ,String date,String job,String answer1,String answer2)
    {
        Ecommerce=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("customername",customername);
        row.put("username",username);
        row.put("password",password);
        row.put("gender",gender);
        row.put("birthdate",date);
        row.put("job",job);
        row.put("answer1",answer1);
        row.put("answer2",answer2);

        long res=Ecommerce.insert("customers",null,row);

        Ecommerce.close();

        if(res!=-1)
        {
            return true;
        }
        return false;

    }

    public boolean check_exist_username(String username)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select username from customers where username = ?",new String[]{username});
        cur.moveToFirst();
        if(cur.getCount()!=0)
            return true;
        return false;
    }

    public boolean checkanswers(String username, String answer1,String answer2)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select username,answer1,answer2 from customers where username =? and answer1= ? and answer2= ?",new String[]{username,answer1,answer2});
        cur.moveToFirst();
        if(cur.getCount()!=0)
            return true;
        return false;
    }

    public void update_pass_answers(String username,String newpass, String newanswer1,String newanswer2)
    {
        Ecommerce=getWritableDatabase();
        ContentValues row= new ContentValues();
        row.put("password",newpass);
        row.put("answer1",newanswer1);
        row.put("answer2",newanswer2);

        Ecommerce.update("customers",row,"username= ?",new String[]{username});
        Ecommerce.close();

    }
    public ArrayList fetch_all_products()
    {
        ArrayList arr=new ArrayList();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res=db.rawQuery("select * from products",null);
        res.moveToFirst();
        while(!res.isAfterLast())
        {
            arr.add(res.getString(1));
            res.moveToNext();
        }
        return arr;
    }
    public void insert_category(SQLiteDatabase db,String name,int id)
    {
        ContentValues row=new ContentValues();
        row.put("categoryid",id);
        row.put("categoryname",name);
        db.insert("categories",null,row);
    }

    public void insert_product(SQLiteDatabase db ,String productname,String image ,String desc,String price,int quan,int catid)
    {
        ContentValues row=new ContentValues();
        row.put("productname",productname);
        row.put("image",image);
        row.put("description",desc);
        row.put("price",price);
        row.put("quantity",quan);
        row.put("categoryid",catid);
        db.insert("products",null,row);
    }


    public ArrayList get_specific_category_products(int categoryid)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select productname from products where categoryid = ?",new String[]{String.valueOf(categoryid)});
        ArrayList arr=new ArrayList();
        cur.moveToFirst();
        while(!cur.isAfterLast()) {
            arr.add(cur.getString(0));
            cur.moveToNext();
        }
        return arr;
    }

    public ArrayList get_product_details(String productname)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select productname,quantity,description,price from products where productname = ?",new String[]{String.valueOf(productname)});
        ArrayList arr=new ArrayList();
        cur.moveToFirst();
        int i=0;
        while(i<4) {
            arr.add(cur.getString(i));
            i++;
        }
        return arr;
    }

    public ArrayList search_by_text(String text)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select productname from products where productname like ?",new String[]{"%"+text+"%"});
        ArrayList arrayList= new ArrayList();
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            arrayList.add(cur.getString(0));
            cur.moveToNext();
        }
        return arrayList;
    }

    public ArrayList search_by_category(String des)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select productname from products where description like ?",new String[]{"%"+des+"%"});
        ArrayList arrayList= new ArrayList();
        cur.moveToFirst();
        while(!cur.isAfterLast())
        {
            arrayList.add(cur.getString(0));
            cur.moveToNext();
        }
        return arrayList;
    }

    public void insert_Shopping_cart (int customerid)
    {
        Ecommerce=getWritableDatabase();
        ContentValues row=new ContentValues();
        row.put("customerid",customerid);
        Ecommerce.insert("shoppingcart",null,row);
        Ecommerce.close();
    }

    public int get_customer_id(String customername)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select customerid from customers where username = ?",new String[]{customername});
        cur.moveToFirst();
        Ecommerce.close();
        return cur.getInt(0);
    }
    public int check_existance_item(String productname,int cartid )
    {
        Ecommerce=getReadableDatabase();
        int productid=get_product_id(productname);
        Cursor cur=Ecommerce.rawQuery("select quantity from cartitems where productid =?  and cartid= ?",new String[]{String.valueOf(productid),String.valueOf(cartid)});
        cur.moveToFirst();
        if(cur.getCount()!=0)
            return cur.getInt(0);
        return 0;
    }

    public int get_product_id(String productname)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select productid from products where productname= ?",new String[]{productname});
        cur.moveToFirst();
        return cur.getInt(0);
    }

    public int get_product_quantity(String productname)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select quantity from products where productname= ?",new String[]{productname});
        cur.moveToFirst();
        return cur.getInt(0);
    }
    public int get_quantity_item(String productname , int cartid)
    {
        Ecommerce=getReadableDatabase();
        int productid=get_product_id(productname);
        Cursor cur=Ecommerce.rawQuery("select quantity from cartitems where productid= ? and cartid =?",new String[]{String.valueOf(productid),String.valueOf(cartid)});
        cur.moveToFirst();
        return cur.getInt(0);
    }

    public void delete_quantity(String productname, int cartid){
        Ecommerce=getWritableDatabase();
        int productid=get_product_id(productname);
        Cursor cur=Ecommerce.rawQuery("delete from cartitems where productid= ? and cartid =?",new String[]{String.valueOf(productid),String.valueOf(cartid)});
        cur.moveToFirst();
    }

    public boolean update_item_quantity(String productname , int cartid,int quantity)
    {
        Ecommerce=getWritableDatabase();

        int productid=get_product_id(productname);
        if(check_quantity_product(productname,quantity+1)) {
            ContentValues row= new ContentValues();
            row.put("quantity", quantity + 1);
            Ecommerce.update("cartitems", row, "productid = ? and cartid= ? ", new String[]{String.valueOf(productid), String.valueOf(cartid)});
            return true;
        }
        else
        {
            return false;
        }

    }

    public void update_item_quan(String productname , int cartid,int quantity)
    {
           Ecommerce=getWritableDatabase();
           int productid=get_product_id(productname);
            ContentValues row= new ContentValues();
            row.put("quantity", quantity );
            Ecommerce.update("cartitems", row, "productid = ? and cartid= ? ", new String[]{String.valueOf(productid), String.valueOf(cartid)});


    }
    public boolean insert_item_shoppingcart(String productname,int cartid)
    {
        Ecommerce=getWritableDatabase();
        int quantity=check_existance_item(productname,cartid);
        if(quantity>0)
        {
            if(update_item_quantity(productname,cartid,quantity))
                return true;
            else
                return false;
        }
        else {
            int productid=get_product_id(productname);
            ContentValues row = new ContentValues();
            row.put("productid", productid);
            row.put("quantity",1);
            row.put("cartid",cartid);
            Ecommerce.insert("cartitems",null,row);
            Ecommerce.close();
            return true;
        }

    }
    public boolean check_quantity_product(String productname,int number)
    {
        Ecommerce=getWritableDatabase();
        int quantity=get_product_quantity(productname);
        if(quantity>=number) {
            return true;
        }
        return false;
    }


    public int get_cart_id(int customerid)
    {
        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select cartid from shoppingcart where customerid = ?",new String[]{String.valueOf(customerid)});
        cur.moveToFirst();
        Ecommerce.close();
        return cur.getInt(0);
    }

    public ArrayList get_all_cart_products_ids(int cartid)
    {
        Ecommerce=getReadableDatabase();
        ArrayList arrayList= new ArrayList();
        Cursor cur=Ecommerce.rawQuery("select productid from cartitems where cartid= ?",new String[]{String.valueOf(cartid)});
        cur.moveToFirst();
        while (!cur.isAfterLast())
        {
            arrayList.add(cur.getInt(0));
            cur.moveToNext();
        }
        Ecommerce.close();
        return arrayList;
    }

   public String get_productname(int productid)
   {
       Ecommerce=getReadableDatabase();
       Cursor cur=Ecommerce.rawQuery("select productname from products where productid= ?",new String[]{String.valueOf(productid)});
       cur.moveToFirst();
       Ecommerce.close();
       return cur.getString(0);
   }

   public ArrayList fetch_all_cart_products(int cartid)
   {
       ArrayList<Integer> arrayList=get_all_cart_products_ids(cartid);
       ArrayList<String> products=new ArrayList<String>();
       int i=0;
       while(i<arrayList.size())
       {
          String productname=get_productname(arrayList.get(i));
          products.add(productname);
           i++;
       }
       return products;
   }



    public int get_price(String productname)
    {

        Ecommerce=getReadableDatabase();
        Cursor cur=Ecommerce.rawQuery("select price from products where productname= ?",new String[]{productname});
        cur.moveToFirst();
        Ecommerce.close();
        return cur.getInt(0);

    }

    public float total_price(ArrayList productnames,int cartid )
    {
        int i=0;
        float total=0;
        while(i<productnames.size())
        {
            String currentproduct=productnames.get(i).toString();
            float price=get_price(currentproduct);
            int productid=get_product_id(currentproduct);
            int quantity=get_quantity_item(currentproduct,cartid);
            total+=price*quantity;
            i++;
        }
        return total;
    }

    public void insert_order(String date , String address,int customer_id)
    {
        Ecommerce=getWritableDatabase();
        ContentValues row= new ContentValues();
        row.put("orderdate",date);
        row.put("orderaddress",address);
        row.put("customerid",customer_id);
        Ecommerce.insert("orders",null,row);
        Ecommerce.close();
    }

    public void insert_order_details(int quantity,int productid,int orderid)
    {
        Ecommerce=getWritableDatabase();
        ContentValues row= new ContentValues();
        row.put("quantity",quantity);
        row.put("productid",productid);
        row.put("orderid",orderid);
        Ecommerce.insert("orderdetails",null,row);
        Ecommerce.close();
    }

}


