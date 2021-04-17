package io.github.emcthye.fxrate.entity.remote;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.entity.CurrencyDataSource;
import io.reactivex.Observable;

@Singleton
public class CurrencyRemoteDataSource implements CurrencyDataSource {

    private final CurrencyAPI API;


    @Inject
    public CurrencyRemoteDataSource(CurrencyAPI API) {
        this.API = API;
    }

    @Override
    public Observable<CurrencyModelAPI> getRateList(String startDate, String baseCurrency) {

        return API.getCurrencyList(startDate, baseCurrency);
    }

    @Override
    public void saveRateList(CurrencyModelAPI rateList) {

    }

    @Override
    public Observable<String> getLastUpdated() {
        return Observable.just("");
    }
}
