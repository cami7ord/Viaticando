package com.cami7ord.viaticando.expenses;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;

public class NewExpenseActivity extends BaseActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Bundle extras = getIntent().getBundleExtra("image");
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        ((ImageView)findViewById(R.id.new_expense_img)).setImageBitmap(imageBitmap);

        uploadExpensePhoto(imageBitmap);

        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);


        String[] ITEMS = {"Transporte", "Alimentación", "Otros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = findViewById(R.id.new_expense_category);
        spinner.setAdapter(adapter);

        ((EditText)(findViewById(R.id.new_expense_date))).setText(day+"/"+month+"/"+year);

        findViewById(R.id.new_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new CheckDoneDialog();
                newFragment.show(getSupportFragmentManager(), "missiles");
            }
        });
    }

    private void uploadExpensePhoto(Bitmap bitmap) {

        showProgressDialog();

        // Get the data from an ImageView as bytes
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        String path = "invoices/" + UUID.randomUUID() + ".png";
        StorageReference invoicesRef = storage.getReference(path);

        //StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("text", "Custom metadata").build();

        UploadTask uploadTask = invoicesRef.putBytes(data);//, metadata);
        uploadTask.addOnFailureListener(NewExpenseActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("Upload", "Error");
                hideProgressDialog();
            }
        }).addOnSuccessListener(NewExpenseActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Log.e("Upload", "Success:" + taskSnapshot.getDownloadUrl());
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                hideProgressDialog();
            }
        });
    }
}
