package io.github.emcthye.fxrate;

import android.app.Application;

import io.github.emcthye.fxrate.util.di.component.DaggerApplicationComponent;
import io.github.emcthye.fxrate.util.di.module.ApplicationModule;
import io.github.emcthye.fxrate.util.di.component.ApplicationComponent;

public class AndroidApplication extends Application {

    protected ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        this.initializeInjector();
    }

    protected void initializeInjector() {
        this.applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.applicationComponent;
    }
}
