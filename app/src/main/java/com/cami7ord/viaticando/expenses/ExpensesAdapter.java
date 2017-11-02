package com.cami7ord.viaticando.expenses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cami7ord.viaticando.R;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private Context mContext;
    private List mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout mExpenseLayout;
        TextView mExpenseName;
        TextView mExpenseCategory;
        TextView mExpenseStatus;
        TextView mExpenseAmount;

        ViewHolder(View v) {
            super(v);
            mExpenseLayout = v.findViewById(R.id.expense_layout);
            mExpenseName = v.findViewById(R.id.expense_name);
            mExpenseCategory = v.findViewById(R.id.expense_category);
            mExpenseStatus = v.findViewById(R.id.expense_status);
            mExpenseAmount = v.findViewById(R.id.expense_amount);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    ExpensesAdapter(Context context, List myDataset) {
        mContext = context;
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ExpensesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trip_expenses, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ExpensesAdapter.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ExpensesAdapter.ViewHolder holder, int position) {

        if(position > 2) {
            holder.mExpenseName.setText("Taxi al aeropuerto");
            holder.mExpenseCategory.setText("Transporte");
            holder.mExpenseAmount.setText("$ 15.000");
            holder.mExpenseStatus.setText("Pendiente");
            holder.mExpenseStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_24dp, 0, 0, 0);
        }
        //holder.mTripName
        //holder.mTextView.setText(mDataset[position]);
        holder.mExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent expenses = new Intent(mContext, ExpensesActivity.class);
                //mContext.startActivity(expenses);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}