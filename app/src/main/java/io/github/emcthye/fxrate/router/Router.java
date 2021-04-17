package io.github.emcthye.fxrate.router;

import android.content.Context;
import android.content.Intent;

import androidx.core.util.Pair;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.currencyList.CurrencyListContract;
import io.github.emcthye.fxrate.currencyTimeline.CurrencyTimelineActivity;

@Singleton
public class Router implements CurrencyListContract.Router {

    @Inject
    public Router() {
    }

    @Override
    public void navigateToCurrencyTimeline(Context context, Pair<String, String> currencyPair) {
        if (context != null) {
            Intent intentToLaunch = CurrencyTimelineActivity.getCallingIntent(context, currencyPair);
            context.startActivity(intentToLaunch);
        }
    }
}
