package com.bogdanorzea.bakingapp.ui.detail;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.R;
import com.bogdanorzea.bakingapp.data.database.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.bogdanorzea.bakingapp.ui.detail.StepFragmentUtils.replaceFragmentInActivity;

class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {
    private final AppCompatActivity parentActivity;
    private final boolean isTwoPane;

    private List<Step> steps;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Step step = (Step) v.getTag();
            Timber.d("Step at position %s was clicked", step.getId());

            if (isTwoPane) {
                Timber.d("Replacing fragment to show details for Step#%s", step.getId());

                replaceFragmentInActivity(parentActivity, step.getRecipeId(), step.getId());
            } else {
                Timber.d("Starting activity to show details for Step#%s", step.getId());

                Intent intent = new Intent(parentActivity, StepDetailActivity.class);
                intent.putExtra(StepDetailActivity.RECIPE_ID, step.getRecipeId());
                intent.putExtra(StepDetailActivity.STEP_ID, step.getId());

                parentActivity.startActivity(intent);
            }
        }
    };

    StepAdapter(AppCompatActivity parentActivity, boolean isTwoPane) {
        this.parentActivity = parentActivity;
        this.isTwoPane = isTwoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parentActivity)
                .inflate(R.layout.activity_step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step currentStep = steps.get(position);

        holder.stepName.setText(currentStep.getShortDescription());
        holder.stepId.setText(currentStep.getId().toString());

        holder.itemView.setTag(currentStep);
        holder.itemView.setOnClickListener(onClickListener);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.step_id) TextView stepId;
        @BindView(R.id.step_name) TextView stepName;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
