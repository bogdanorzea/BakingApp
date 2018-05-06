package com.bogdanorzea.bakingapp.util;

import com.bogdanorzea.bakingapp.data.database.Step;

import junit.framework.Assert;

public class StepTestUtil {
    public static void assertEquals(Step oldStep, Step newStep) {
        Assert.assertEquals(oldStep.getId(), newStep.getId());
        Assert.assertEquals(oldStep.getDescription(), newStep.getDescription());
        Assert.assertEquals(oldStep.getShortDescription(), newStep.getShortDescription());
    }
}
