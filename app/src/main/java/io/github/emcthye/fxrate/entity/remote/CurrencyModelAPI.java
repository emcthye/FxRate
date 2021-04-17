
package io.github.emcthye.fxrate.entity.remote;

import com.squareup.moshi.Json;

import java.util.Map;

public class CurrencyModelAPI {

    @Json(name = "amount")
    public Float amount;
    @Json(name = "base")
    public String base;
    @Json(name = "end_date")
    public String end_date;
    @Json(name = "rates")
    public Map<String, Map<String, Float>> dates;
//    public Rates rates;

    public static class Rate {
        public Map<String, Float> rate;
    }

    @Override
    public String toString() {
        return amount.toString() + ' ' + base + ' ' + end_date + ' ' + dates.toString();
    }

}
