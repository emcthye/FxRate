package io.github.emcthye.fxrate.util.threading;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;

@Singleton
public class UIThreadImpl implements UIThread {

  @Inject
  UIThreadImpl() {}

  @Override
  public Scheduler getScheduler() {
    return AndroidSchedulers.mainThread();
  }
}
