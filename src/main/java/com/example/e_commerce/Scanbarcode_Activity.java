 package com.example.e_commerce;

 import android.os.Bundle;

 import androidx.appcompat.app.AppCompatActivity;

public class Scanbarcode_Activity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView scannerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(scannerView);

    }




    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }



}
