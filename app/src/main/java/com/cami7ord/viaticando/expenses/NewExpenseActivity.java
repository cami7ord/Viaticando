package com.cami7ord.viaticando.expenses;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class NewExpenseActivity extends BaseActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Bundle extras = getIntent().getBundleExtra("image");
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        ((ImageView)findViewById(R.id.new_expense_img)).setImageBitmap(imageBitmap);

        uploadExpensePhoto(imageBitmap);

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
