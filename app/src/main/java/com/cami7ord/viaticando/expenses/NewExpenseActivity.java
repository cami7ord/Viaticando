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
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.BuildConfig;
import com.cami7ord.viaticando.MyJsonArrayRequest;
import com.cami7ord.viaticando.MyJsonObjectRequest;
import com.cami7ord.viaticando.MyRequestQueue;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.data.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;

public class NewExpenseActivity extends BaseActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private MaterialSpinner spinner;
    private String photoURL;

    //UI
    private TextView expenseValue;
    private TextView expenseNit;
    private TextView expenseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        Bundle extras = getIntent().getBundleExtra("image");
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        ((ImageView)findViewById(R.id.new_expense_img)).setImageBitmap(imageBitmap);

        uploadExpensePhoto(imageBitmap);
        downloadCategories();

        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        ((EditText)(findViewById(R.id.new_expense_date))).setText(day+"/"+month+"/"+year);

        expenseValue = findViewById(R.id.new_expense_value);
        expenseNit = findViewById(R.id.new_expense_nit);
        expenseDescription = findViewById(R.id.new_expense_description);

        findViewById(R.id.new_expense_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createExpense();

            }
        });
    }

    private void downloadCategories() {

        String url = BuildConfig.BASE_URL + "Categories";

        MyJsonArrayRequest jsonRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Categories Res:", response.toString());
                        parseCategories(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);
    }

    private void parseCategories(JSONArray response) {

        Category[] ITEMS = new Category[response.length()];
        JSONObject jsonObject;

        try {

            for(int i=0 ; i<response.length() ; i++) {

                jsonObject = response.getJSONObject(i);

                Category category = new Category();
                category.setCategoryId(jsonObject.getInt("categoryId"));
                category.setName(jsonObject.getString("name"));
                category.setAccountingNumber(jsonObject.getInt("accountingNumber"));

                ITEMS[i] = category;
            }

            ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_spinner_item, ITEMS);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner = findViewById(R.id.new_expense_category);
            spinner.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                photoURL = taskSnapshot.getDownloadUrl().toString();
                hideProgressDialog();
            }
        });
    }

    private void createExpense(){

        JSONObject body = createBody();

        Log.e("BODY", body.toString());

        String url = BuildConfig.BASE_URL + "Expenses";

        MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest
                (Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Expense Created:", response.toString());

                        DialogFragment newFragment = new CheckDoneDialog();
                        newFragment.show(getSupportFragmentManager(), "missiles");

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.e("Expense Error:", error.toString());
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);

    }

    private JSONObject createBody() {

        JSONObject body = new JSONObject();

        try {

            body.put("tripId", 1);
            body.put("nit", expenseNit.getText().toString());
            body.put("date", "2017-11-03T00:14:36.884Z");
            body.put("description", expenseDescription.getText().toString());
            body.put("value", expenseValue.getText().toString());
            body.put("photoURL", Uri.decode(photoURL));
            body.put("categoryId", ((Category)spinner.getSelectedItem()).getCategoryId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;

    }
}
