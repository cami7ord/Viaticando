package com.cami7ord.viaticando.trips;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.BuildConfig;
import com.cami7ord.viaticando.MyJsonArrayRequest;
import com.cami7ord.viaticando.MyRequestQueue;
import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.data.Trip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TripsActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        mRecyclerView = findViewById(R.id.trips_recycler);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        downloadTrips(1);

    }

    private void downloadTrips(int userId) {

        showProgressDialog();

        String url = BuildConfig.BASE_URL + "Trips";

        MyJsonArrayRequest jsonRequest = new MyJsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Trips Res:", response.toString());
                        parseResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        MyRequestQueue.getInstance(this).getRequestQueue().add(jsonRequest);
    }

    private void parseResponse(JSONArray response) {

        List<Trip> trips = new ArrayList<>();
        JSONObject tripObject;
        JSONArray expensesIdsArray;

        try {

            for(int i=0 ; i<response.length() ; i++) {

                tripObject = response.getJSONObject(i);
                List<Integer> expensesIds = new ArrayList<>();

                if(!tripObject.isNull("expensesIds")) {
                    expensesIdsArray = tripObject.getJSONArray("expensesIds");
                    for(int j=0 ; j<expensesIdsArray.length() ; j++) {
                        expensesIds.add(expensesIdsArray.getInt(j));
                    }
                }

                Trip trip = new Trip();
                trip.setTripId(tripObject.getInt("tripId"));
                trip.setExpensesIds(expensesIds);
                trip.setBudget(tripObject.getDouble("budget"));
                trip.setStartDate(tripObject.getString("startDate"));
                trip.setEndDate(tripObject.getString("endDate"));
                trip.setDestiny(tripObject.getString("destiny"));
                trip.setDescription(tripObject.getString("description"));
                trip.setStatusId(tripObject.getInt("statusId"));
                trip.setEmployeeId(tripObject.getInt("employeeId"));

                trips.add(trip);

            }

            mAdapter = new TripsAdapter(this, trips);
            mRecyclerView.setAdapter(mAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        hideProgressDialog();

    }

}
