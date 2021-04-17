package io.github.emcthye.fxrate.util.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.emcthye.fxrate.AndroidApplication;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.ThreadExecutorImpl;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.github.emcthye.fxrate.util.threading.UIThreadImpl;

@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    UIThread provideUIThread(UIThreadImpl uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    ThreadExecutor provideExecutorThread(ThreadExecutorImpl threadExecutor) {
        return threadExecutor;
    }
}
