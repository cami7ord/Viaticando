package com.cami7ord.viaticando.trips;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Trip;
import com.cami7ord.viaticando.expenses.ExpensesActivity;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private Context mContext;
    private List<Trip> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mTripLayout;
        TextView mTripName;
        TextView mTripDates;
        TextView mTripStatus;
        TextView mTripAmount;

        ViewHolder(View v) {
            super(v);
            mTripLayout = v.findViewById(R.id.trip_layout);
            mTripName = v.findViewById(R.id.trip_name);
            mTripDates = v.findViewById(R.id.trip_date);
            mTripStatus = v.findViewById(R.id.trip_status);
            mTripAmount = v.findViewById(R.id.trip_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    TripsAdapter(Context context, List<Trip> myDataset) {
        mContext = context;
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
        Trip trip = mDataset.get(position);

        holder.mTripName.setText(trip.getDestiny());
        holder.mTripDates.setText("Desde " + Utilities.simpleServerDateFormat(trip.getStartDate()) +
                                    "- Hasta " + Utilities.simpleServerDateFormat(trip.getEndDate()));
        holder.mTripAmount.setText(Utilities.formatPrice(trip.getBudget()));

        if(trip.getStatusId() == 1) {
            holder.mTripStatus.setText("Aprobado");
        } else {
            holder.mTripStatus.setText("Pendiente");
            holder.mTripStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_24dp, 0, 0, 0);
        }

        holder.mTripLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent expenses = new Intent(mContext, ExpensesActivity.class);
                mContext.startActivity(expenses);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
