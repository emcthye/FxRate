package io.github.emcthye.fxrate.util.di.component;

import android.content.Context;
import javax.inject.Singleton;

import dagger.Component;
import io.github.emcthye.fxrate.util.di.module.ApplicationModule;
import io.github.emcthye.fxrate.util.di.module.NetworkModule;
import io.github.emcthye.fxrate.currencyList.CurrencyListActivity;
import io.github.emcthye.fxrate.currencyTimeline.CurrencyTimelineActivity;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = {ApplicationModule.class, NetworkModule.class})
public interface ApplicationComponent {
  void inject(CurrencyListActivity baseActivity);
  void inject(CurrencyTimelineActivity baseActivity);

  //Exposed to sub-graphs.
  Context context();
}
