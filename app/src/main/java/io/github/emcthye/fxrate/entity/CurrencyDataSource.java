package io.github.emcthye.fxrate.entity;

import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.reactivex.Observable;

public interface CurrencyDataSource {

    Observable<CurrencyModelAPI> getRateList(String startDate, String baseCurrency);

    void saveRateList(CurrencyModelAPI rateList);

    Observable<String> getLastUpdated();
}
