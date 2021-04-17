package io.github.emcthye.fxrate.util.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.emcthye.fxrate.entity.remote.CurrencyAPI;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        return new Retrofit.Builder()
                .baseUrl(CurrencyAPI.CURRENCY_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    CurrencyAPI provideCurrencyAPI(Retrofit retrofit) {
        return retrofit.create(CurrencyAPI.class);
    }
}
