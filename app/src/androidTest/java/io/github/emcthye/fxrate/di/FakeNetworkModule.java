package io.github.emcthye.fxrate.di;

import androidx.core.util.Pair;

import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.emcthye.fxrate.entity.CurrencyDataSource;
import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.entity.CurrencyRepository;
import io.github.emcthye.fxrate.entity.remote.CurrencyAPI;
import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.interactor.GetCurrencyList;
import io.github.emcthye.fxrate.util.threading.UIThreadImpl;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Module
public class FakeNetworkModule {

    @Provides
    @Singleton
    GetCurrencyList provideCurrencyList() {
        return Mockito.mock(GetCurrencyList.class);
    }

    @Provides
    @Singleton
    CurrencyAPI provideCurrencyAPI() {
        return Mockito.mock(CurrencyAPI.class);
    }
}
