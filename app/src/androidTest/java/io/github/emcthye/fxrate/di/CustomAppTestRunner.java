package io.github.emcthye.fxrate.di;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

public class CustomAppTestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(
            ClassLoader cl, String className, Context context)
            throws InstantiationException,
            IllegalAccessException,
            ClassNotFoundException {
        return super.newApplication(
                cl, FakeAndroidApplication.class.getName(), context);
    }
}
