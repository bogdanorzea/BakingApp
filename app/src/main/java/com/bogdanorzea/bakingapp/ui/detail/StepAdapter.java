package com.bogdanorzea.bakingapp.ui.detail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.data.database.Step;

import java.util.List;

class StepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private List<Step> steps;
    private OnStepItemClickHandler handler;

    StepAdapter(Context context, OnStepItemClickHandler handler) {
        this.context = context;
        this.handler = handler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).mText.setText(steps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (steps == null) return 0;

        return steps.size();
    }

    public void swapSteps(List<Step> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public interface OnStepItemClickHandler {
        void onStepItemClick(int stepId);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handler.onStepItemClick(steps.get(getAdapterPosition()).getId());
        }
    }
}
