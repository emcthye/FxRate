package io.github.emcthye.fxrate.di;

import javax.inject.Singleton;

import dagger.Component;
import io.github.emcthye.fxrate.currencyList.CurrencyListActivity;
import io.github.emcthye.fxrate.currencyList.CurrencyListActivityTest;
import io.github.emcthye.fxrate.util.di.component.ApplicationComponent;

@Singleton
@Component(modules = {FakeApplicationModule.class, FakeNetworkModule.class})
public interface TestApplicationComponent extends ApplicationComponent {
    void inject(CurrencyListActivity currencyListActivity);
    void inject(CurrencyListActivityTest currencyListActivityTest);
}
