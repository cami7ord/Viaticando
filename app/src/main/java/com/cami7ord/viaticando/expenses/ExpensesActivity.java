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
import com.cami7ord.viaticando.MyRequestQueue;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Trip;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.List;

public class ExpensesActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<String> expenses;
    private Trip mTrip;

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

        setupTrip(getIntent().getStringExtra(Constants.EXTRA_TRIP_OBJ));

    }

    private void setupTrip(String stringExtra) {

        mTrip = new Gson().fromJson(stringExtra, Trip.class);
        Log.e("Trip", mTrip.toString());

        setTitle(mTrip.getDestiny());

        mTripDates.setText("Desde " + Utilities.simpleServerDateFormat(mTrip.getStartDate()) +
                " - Hasta " + Utilities.simpleServerDateFormat(mTrip.getEndDate()));

        mTripBudget.setText(Utilities.formatPrice(mTrip.getBudget()));

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

        String url = BuildConfig.BASE_URL + "Trips/" + integer;

        MyJsonArrayRequest jsonRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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

    private void parseExpenseResponse(JSONArray response) {

        Log.e("Response Expense", response.toString());
        /*
        expenses.add("A");
        expenses.add("B");
        mAdapter = new ExpensesAdapter(this, expenses);
        mRecyclerView.setAdapter(mAdapter);*/

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
