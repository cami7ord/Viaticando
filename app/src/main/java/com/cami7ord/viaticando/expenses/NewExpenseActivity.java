package com.cami7ord.viaticando.expenses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.cami7ord.viaticando.R;

public class NewExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Bundle extras = getIntent().getBundleExtra("image");
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        ((ImageView)findViewById(R.id.new_expense_img)).setImageBitmap(imageBitmap);

    }
}
