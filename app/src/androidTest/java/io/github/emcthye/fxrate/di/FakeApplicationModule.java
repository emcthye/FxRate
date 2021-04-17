package io.github.emcthye.fxrate.di;

import android.content.Context;

import javax.inject.Singleton;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import io.github.emcthye.fxrate.currencyList.CurrencyListPresenter;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.ThreadExecutorImpl;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.github.emcthye.fxrate.util.threading.UIThreadImpl;

@Module
public class FakeApplicationModule {

    private final FakeAndroidApplication application;

    public FakeApplicationModule(FakeAndroidApplication application) {
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
        return Mockito.mock(UIThread.class);
    }

    @Provides
    @Singleton
    ThreadExecutor provideExecutorThread(ThreadExecutorImpl threadExecutor) {
        return Mockito.mock(ThreadExecutor.class);
    }

    @Provides
    CurrencyListPresenter providePresenter() {
        return Mockito.mock(CurrencyListPresenter.class);
    }
}
