package com.cami7ord.viaticando.expenses;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cami7ord.viaticando.R;
import com.cami7ord.viaticando.Utilities;
import com.cami7ord.viaticando.data.Category;
import com.cami7ord.viaticando.data.Expense;

import java.util.List;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ViewHolder> {

    private Context mContext;
    private List<Expense> mDataset;
    private Category[] mCategories;

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
    ExpensesAdapter(Context context, List myDataset, Category[] categories) {
        mContext = context;
        mDataset = myDataset;
        mCategories = categories;
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

        Expense expense = mDataset.get(position);

        for(int i=0 ; i<mCategories.length ; i++) {
            if(mCategories[i].getCategoryId() == expense.getCategoryId()) {
                holder.mExpenseCategory.setText(mCategories[i].getName());
            }
        }

        holder.mExpenseName.setText(expense.getDescription());
        holder.mExpenseAmount.setText(Utilities.formatPrice(expense.getValue()));

        if(!expense.isApproved()) {
            holder.mExpenseStatus.setText("Pendiente");
            holder.mExpenseStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_error_24dp, 0, 0, 0);
        }

        holder.mExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Expense", "Clicked");
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