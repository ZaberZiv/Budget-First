package com.budgetfirst.financialapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.budgetfirst.financialapp.R;
import com.budgetfirst.financialapp.model.database.FinancialContract;
import com.budgetfirst.financialapp.presenter.panel.PanelPresenter;
import com.budgetfirst.financialapp.utils.UtilConverter;

import java.text.SimpleDateFormat;

public class ExpenceAdapter extends RecyclerView.Adapter<ExpenceAdapter.ExpenceViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private PanelPresenter mPanelPresenter;

    public ExpenceAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mPanelPresenter = new PanelPresenter();
    }

    public static class ExpenceViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextViewDate;
        public ImageView mDeleteImage;

        public ExpenceViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.txtView1);
            mTextView2 = itemView.findViewById(R.id.txtView2);
            mTextView3 = itemView.findViewById(R.id.txtView3);

            mTextViewDate = itemView.findViewById(R.id.textViewDate);
            mDeleteImage = itemView.findViewById(R.id.imageDelete);
        }
    }

    @NonNull
    @Override
    public ExpenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_items, parent, false);
        return new ExpenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenceViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String title = mCursor.getString(mCursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_TITLE));
        double expence = mCursor.getDouble(mCursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_EXPENCE));
        double income = mCursor.getDouble(mCursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_INCOME));
        long id = mCursor.getLong(mCursor.getColumnIndex(FinancialContract.FinancialEntry._ID));
        long date = mCursor.getLong(mCursor.getColumnIndex(FinancialContract.FinancialEntry.COLUMN_TIMESTAMP));

        holder.mTextView1.setTextColor(ContextCompat.getColor(mContext, R.color.colorExpence));
        holder.mTextView3.setTextColor(ContextCompat.getColor(mContext, R.color.colorIncome));

        if (expence == 0.0) {
            holder.mTextView1.setText("");
        } else {
            holder.mTextView1.setText(mPanelPresenter.customFormat(expence));
        }
        if (income == 0.0) {
            holder.mTextView3.setText("");
        } else {
            holder.mTextView3.setText(mPanelPresenter.customFormat( income));
        }

        holder.mTextView2.setText(title);
        holder.mTextViewDate.setText(UtilConverter.dateFormatDayMonthYear(date));
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}