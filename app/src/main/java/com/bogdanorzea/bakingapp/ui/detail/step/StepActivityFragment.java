package com.bogdanorzea.bakingapp.ui.detail.step;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bogdanorzea.bakingapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment {

    public StepActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step, container, false);
    }
}