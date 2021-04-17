package io.github.emcthye.fxrate.entity.remote;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CurrencyAPI {

    String CURRENCY_API_URL = "https://api.frankfurter.app/";

    @GET("{startDate}..")
    Observable<CurrencyModelAPI> getCurrencyList(@Path("startDate") String startDate, @Query("from") String base);
}
