package io.github.emcthye.fxrate.entity;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.entity.local.CurrencyLocalDataSource;
import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.entity.remote.CurrencyRemoteDataSource;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class CurrencyRepository implements CurrencyDataSource {

    private final CurrencyLocalDataSource currencyLocalDataSource;
    private final CurrencyRemoteDataSource currencyRemoteDataSource;

    CurrencyModelAPI cachedRateList;
    private boolean cacheIsDirty = false;

    @Inject
    ThreadExecutor threadExecutor;
    @Inject UIThread UIThread;

    @Inject
    public CurrencyRepository(CurrencyLocalDataSource currencyLocalDataSource, CurrencyRemoteDataSource currencyRemoteDataSource) {
        this.currencyLocalDataSource = currencyLocalDataSource;
        this.currencyRemoteDataSource = currencyRemoteDataSource;
    }

    @Override
    public Observable<CurrencyModelAPI> getRateList(String startDate, String baseCurrency) {

        if (cachedRateList != null && !cacheIsDirty) {
//            Log.i("getRateList", "Cache");
            return  Observable.create(emitter -> {
                emitter.onNext(cachedRateList);
                emitter.onComplete();
            });
        }
        else if (!currencyLocalDataSource.isExpired() && currencyLocalDataSource.isCached(baseCurrency)) {
//            Log.i("getRateList", "File Cache");
            return getTasksFromLocalDataSource(startDate, baseCurrency);
        }
        else {
//            Log.i("getRateList", "Remote");
            return getTasksFromRemoteDataSource(startDate, baseCurrency);
        }
    }

    @Override
    public void saveRateList(CurrencyModelAPI rateList) {
        cachedRateList = rateList;
        currencyLocalDataSource.saveRateList(rateList);
        currencyRemoteDataSource.saveRateList(rateList);
    }

    @Override
    public Observable<String> getLastUpdated() {
        return currencyLocalDataSource.getLastUpdated();
    }

    public void refreshCurrencyRate() {
        cacheIsDirty = true;
        currencyLocalDataSource.evictAll();
    }

    private Observable<CurrencyModelAPI> getTasksFromLocalDataSource(String startDate, String baseCurrency) {

        return currencyLocalDataSource.getRateList(startDate, baseCurrency).map(result -> {
            refreshCache(result);
            return result;
        });
    }
    @SuppressLint("CheckResult")
    private Observable<CurrencyModelAPI> getTasksFromRemoteDataSource(String startDate, String baseCurrency) {

        return currencyRemoteDataSource.getRateList(startDate, baseCurrency)
                .map(result -> {
            refreshLocalDataSource(result);
            refreshCache(result);
            return result;
        });
    }

    private void refreshCache(CurrencyModelAPI currencyList) {
        this.cachedRateList = currencyList;
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(CurrencyModelAPI currencyList) {
        currencyLocalDataSource.saveRateList(currencyList);
    }
}
