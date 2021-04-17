package io.github.emcthye.fxrate.interactor;

import androidx.core.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.entity.CurrencyRepository;
import io.github.emcthye.fxrate.entity.mapper.CurrencyModelMapper;
import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GetCurrencyListTest {

    private GetCurrencyList usecase;

    @Mock CurrencyRepository repository;
    @Mock CurrencyModelMapper mapper;
    @Mock UIThread uiThread;
    @Mock ThreadExecutor threadExecutor;

    @Before
    public void setup() {

        when(uiThread.getScheduler()).thenReturn(mock(Scheduler.class));
        usecase = new GetCurrencyList(repository, mapper, threadExecutor, uiThread);
        usecase = spy(usecase);
    }

    @Test public void testBuildObservable() {

        Observable<CurrencyModelAPI> observable = Observable.just(new CurrencyModelAPI());
        when(repository.getRateList(anyString(), any())).thenReturn(observable);

        usecase.execute(new DisposableObserver<CurrencyEntity>() {
            @Override
            public void onNext(@NonNull CurrencyEntity currencyEntity) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, GetCurrencyList.Params.create("MYR", false));

        verify(repository, never()).refreshCurrencyRate();
        verify(repository).getRateList(anyString(), eq("MYR"));
        assertEquals(usecase.disposables.size(), 1);
    }

    @Test public void testClearDispose() {

        usecase.disposables.add(mock(Disposable.class));
        usecase.dispose();
        assertTrue(usecase.disposables.isDisposed());
    }
}