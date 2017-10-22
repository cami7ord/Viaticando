package com.cami7ord.viaticando.trips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cami7ord.viaticando.BaseActivity;
import com.cami7ord.viaticando.R;

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

        List<String> trips = new ArrayList<>();
        trips.add("A");
        trips.add("B");
        trips.add("C");
        mAdapter = new TripsAdapter(this, trips);
        mRecyclerView.setAdapter(mAdapter);

    }
}
