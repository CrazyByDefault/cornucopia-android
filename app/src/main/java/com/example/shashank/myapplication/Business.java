package com.example.shashank.myapplication;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Business extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Resources resources = getBaseContext().getResources();
        Bitmap businessPic = BitmapFactory.decodeResource(resources , R.drawable.megathon_logo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ImageView img = (ImageView) findViewById(R.id.businessImage);
        img.setImageBitmap(businessPic);
        final Intent qr = new Intent(this, ScannerActivity.class);


        Button spinButton = (Button) findViewById(R.id.spinButton);
        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(qr);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.business_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(this, SpinWheelActivity.class);

        //noinspection SimplifiableIfStatement
        if (id == R.id.pieChart) {
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
