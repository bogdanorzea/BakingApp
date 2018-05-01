package com.bogdanorzea.bakingapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.data.database.Receipt;

import java.util.List;

class ReceiptsAdapter extends RecyclerView.Adapter<ReceiptsAdapter.ViewHolder> {
    private final Context mContext;
    private final OnItemClickHandler mClickHandler;
    private List<Receipt> mReceipts;

    public ReceiptsAdapter(Context context, OnItemClickHandler onItemClickHandler) {
        mContext = context;
        mClickHandler = onItemClickHandler;
    }

    @NonNull
    @Override
    public ReceiptsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptsAdapter.ViewHolder holder, int position) {
        Receipt currentReceipt = mReceipts.get(position);

        holder.mText.setText(currentReceipt.getName());
    }

    @Override
    public int getItemCount() {
        if (mReceipts == null) return 0;

        return mReceipts.size();
    }

    public void swapReceipts(List<Receipt> receipts) {
        if (mReceipts == null) {
            mReceipts = receipts;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickHandler {
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mText;

        ViewHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onItemClick(getAdapterPosition());
        }
    }
}
