package io.github.emcthye.fxrate.entity.mapper;

import android.util.Log;

import androidx.core.util.Pair;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;

@Singleton
public class CurrencyModelMapper {

    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Inject
    public CurrencyModelMapper() {}

    public CurrencyEntity transform(CurrencyModelAPI currencyObj) {
        CurrencyEntity entity = new CurrencyEntity();
        entity.base = currencyObj.base;
        entity.date = currencyObj.end_date;

        for (Map.Entry<String, Map<String, Float>> date : currencyObj.dates.entrySet()) {

            for (Map.Entry<String, Float> rate : date.getValue().entrySet()) {
                String historyRate =  entity.historyRate.containsKey(rate.getKey()) ?
                entity.historyRate.get(rate.getKey()) : "";

                historyRate = historyRate + rate.getValue().toString() + ',';
                entity.historyRate.put(rate.getKey(), historyRate);
            }
        }

        for (Map.Entry<String, Float> entry : currencyObj.dates.get(currencyObj.end_date).entrySet()) {
            Float targetCurrency = entry.getValue() < 1 ? 1/entry.getValue() : entry.getValue();
            if (entry.getValue() < 1) {

                entity.rates.add(new Pair<>(entry.getKey(),
                        String.valueOf(1) + '-' + df.format(1/entry.getValue())));

//                int indexOfDecimal = String.valueOf(entry.getValue().intValue()).length();
//                int multiplier = 1;
//                for (int i = 0 ; i < indexOfDecimal ; i++) multiplier *= 10;
//                entity.rates.add(new Pair<>(entry.getKey(),
//                        df.format(entry.getValue() * multiplier) + '-' + multiplier));
            } else {
                entity.rates.add(new Pair<>(entry.getKey(), df.format(entry.getValue()) + '-' + 1));
            }
        }

        Log.i("Mapper", entity.toString());
        return entity;
    }
}
