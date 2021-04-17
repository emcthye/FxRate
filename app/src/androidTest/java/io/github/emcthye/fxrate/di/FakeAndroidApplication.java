package io.github.emcthye.fxrate.di;

import io.github.emcthye.fxrate.AndroidApplication;

public class FakeAndroidApplication extends AndroidApplication {

    @Override
    protected void initializeInjector() {
        this.applicationComponent = DaggerTestApplicationComponent.builder()
                .fakeApplicationModule(new FakeApplicationModule(this))
                .build();
    }
}