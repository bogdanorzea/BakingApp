package com.bogdanorzea.bakingapp;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.bogdanorzea.bakingapp.ui.detail.StepDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class StepDetailActivityTest {

    @Rule
    public ActivityTestRule<StepDetailActivity> activityActivityTestRule =
            new ActivityTestRule<StepDetailActivity>(StepDetailActivity.class) {
                static final String RECIPE_ID = "recipe_id";
                static final String STEP_ID = "step_id";

                @Override
                protected Intent getActivityIntent() {
                    Intent intent = new Intent();
                    intent.putExtra(RECIPE_ID, 1);
                    intent.putExtra(STEP_ID, 1);

                    return intent;
                }
            };


    @Test
    public void checkStepTitle() {
        onView(withId(R.id.step_title_text)).check(matches(withText("Starting prep")));

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("Step 1")));
    }

    @Test
    public void checkNavigationButtons() {
        onView(withId(R.id.previous_button)).check(matches(isEnabled()));
        onView(withId(R.id.previous_button)).perform(click());
        onView(withId(R.id.previous_button)).check(matches(not(isEnabled())));

        onView(withId(R.id.next_button)).check(matches(isEnabled()));
    }
}
