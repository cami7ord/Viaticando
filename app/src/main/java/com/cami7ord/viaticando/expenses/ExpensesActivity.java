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
import android.widget.TextView;

import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.Constants;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Trip;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends BaseActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<String> expenses;
    private Trip mTrip;

    //UI
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

        expenses = new ArrayList<>();

        expenses.add("A");
        expenses.add("B");
        mAdapter = new ExpensesAdapter(this, expenses);
        mRecyclerView.setAdapter(mAdapter);

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
