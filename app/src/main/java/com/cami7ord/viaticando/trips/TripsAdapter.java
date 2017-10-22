package com.cami7ord.viaticando.trips;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cami7ord.viaticando.R;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private List mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTripName;
        TextView mTripDates;
        TextView mTripStatus;
        TextView mTripAmount;

        ViewHolder(View v) {
            super(v);
            mTripName = v.findViewById(R.id.trip_name);
            mTripDates = v.findViewById(R.id.trip_date);
            mTripStatus = v.findViewById(R.id.trip_status);
            mTripAmount = v.findViewById(R.id.trip_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TripsAdapter(List myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTripName
        //holder.mTextView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
