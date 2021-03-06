package com.cami7ord.viaticando.expenses;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.BuildConfig;
import com.cami7ord.viaticando.MyJsonObjectRequest;
import com.cami7ord.viaticando.MyRequestQueue;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Expense;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends BaseActivity {

    private TextView expenseTitle;
    private TextView expenseCategory;
    private TextView expenseValue;
    private TextView expenseStatus;

    private ImageView expenseImage;
    private TextView expenseDate;
    private TextView expenseNit;
    private TextView expenseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.e("Detail Activity", "ID: " + getIntent().getIntExtra("expenseId", 0));

        if(null != getActionBar()) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        expenseTitle = findViewById(R.id.expense_name);
        expenseCategory = findViewById(R.id.expense_category);
        expenseValue = findViewById(R.id.expense_amount);
        expenseStatus = findViewById(R.id.expense_status);

        expenseImage = findViewById(R.id.expense_img);
        expenseDate = findViewById(R.id.expense_date);
        expenseNit = findViewById(R.id.expense_nit);
        expenseDescription = findViewById(R.id.expense_description);

        String json = getIntent().getStringExtra("expenseObject");

        setExpenseInfo(new Gson().fromJson(json, Expense.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setExpenseInfo(Expense expense) {

        expenseTitle.setText(expense.getDescription());
        expenseValue.setText(Utilities.formatPrice(expense.getValue()));
        if(!expense.isApproved()) {
            expenseStatus.setText("Pendiente");
            expenseStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_24dp, 0, 0, 0);
        }
        expenseDate.setText(Utilities.simpleServerDateFormat(expense.getDate()));
        expenseNit.setText(expense.getNit());
        expenseDescription.setText(expense.getDescription());

        String url = expense.getPhotoURL();
        Log.e("Picasso url", url);
        Picasso.with(this).load(url).placeholder(R.drawable.img_placeholder).into(expenseImage);

        updateCategory(expense.getCategoryId());

    }

    private void updateCategory(int categoryId) {

        String url = BuildConfig.BASE_URL + "Categories/" + categoryId;

        MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Trips Res:", response.toString());
                        parseCategoryResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);

    }

    private void parseCategoryResponse(JSONObject response) {
        try {
            expenseCategory.setText(response.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
