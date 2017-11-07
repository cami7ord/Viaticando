package com.cami7ord.viaticando.expenses;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.BuildConfig;
import com.cami7ord.viaticando.Constants;
import com.cami7ord.viaticando.MyJsonArrayRequest;
import com.cami7ord.viaticando.MyJsonObjectRequest;
import com.cami7ord.viaticando.MyRequestQueue;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Category;
import com.cami7ord.viaticando.data.Expense;
import com.cami7ord.viaticando.data.Trip;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Expense> expenses;
    private Trip mTrip;
    private Category[] categories;

    //UI
    private RelativeLayout mTripEmpty;
    private TextView mTripDates;
    private TextView mTripBudget;
    private TextView mTripTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        //.setAction("Action", null).show();
            }
        });

        mTripEmpty = findViewById(R.id.trip_detail_empty);
        mTripDates = findViewById(R.id.trip_detail_date);
        mTripBudget = findViewById(R.id.trip_detail_amount);
        mTripTotal = findViewById(R.id.trip_detail_total);

        mRecyclerView = findViewById(R.id.trip_detail_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        downloadCategories();

    }

    private void downloadCategories() {

        showProgressDialog();

        String url = BuildConfig.BASE_URL + "Categories";

        MyJsonArrayRequest jsonRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Categories Res:", response.toString());
                        categories = parseCategories(response);
                        setupTrip(getIntent().getStringExtra(Constants.EXTRA_TRIP_OBJ));
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);
    }

    private Category[] parseCategories(JSONArray response) {

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
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ITEMS;
    }

    private void setupTrip(String stringExtra) {

        mTrip = new Gson().fromJson(stringExtra, Trip.class);
        Log.e("Trip", mTrip.toString());

        setTitle(mTrip.getDestiny());

        mTripDates.setText("Desde " + Utilities.simpleServerDateFormat(mTrip.getStartDate()) +
                " - Hasta " + Utilities.simpleServerDateFormat(mTrip.getEndDate()));

        mTripBudget.setText(Utilities.formatPrice(mTrip.getBudget()));

        expenses = new ArrayList<>();
        mAdapter = new ExpensesAdapter(this, expenses, categories);
        mRecyclerView.setAdapter(mAdapter);

        setupExpenses(mTrip.getExpensesIds());

    }

    private void setupExpenses(List<Integer> expensesIds) {

        if(expensesIds.isEmpty()) {

            mTripEmpty.setVisibility(View.VISIBLE);

        } else {

            for(Integer integer : expensesIds) {

                downloadExpenseInfo(integer);

            }

        }

    }

    private void downloadExpenseInfo(int integer) {

        String url = BuildConfig.BASE_URL + "Expenses/" + integer;

        MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Trips Res:", response.toString());
                        parseExpenseResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);

    }

    private void parseExpenseResponse(JSONObject response) {

        Log.e("Response Expense", response.toString());
        Expense expense = new Expense();

        try {
            expense.setExpenseId(response.getInt("expenseId"));
            expense.setTripId(response.getInt("tripId"));
            expense.setNit(response.getString("nit"));
            expense.setDate(response.getString("date"));
            expense.setDescription(response.getString("description"));
            expense.setValue(response.getDouble("value"));
            expense.setApproved(response.getBoolean("approved"));
            expense.setPhotoURL(response.getString("photoURL"));
            expense.setCategoryId(response.getInt("categoryId"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        expenses.add(expense);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(ExpensesActivity.this, NewExpenseActivity.class);
            intent.putExtra("image", data.getExtras());
            startActivity(intent);
        }
    }
}
