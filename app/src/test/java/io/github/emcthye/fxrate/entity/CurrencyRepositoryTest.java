package io.github.emcthye.fxrate.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.github.emcthye.fxrate.entity.local.CurrencyLocalDataSource;
import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.entity.remote.CurrencyRemoteDataSource;
import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRepositoryTest {

    CurrencyRepository repository;

    @Mock CurrencyLocalDataSource localDataSource;
    @Mock CurrencyRemoteDataSource remoteDataSource;

    @Before
    public void setUp() throws Exception {
        repository = new CurrencyRepository(localDataSource, remoteDataSource);
    }

    @Test
    public void testRemoteRateList() {

        when(localDataSource.isExpired()).thenReturn(true);
        when(remoteDataSource.getRateList(any(), any())).thenReturn(Observable.just(new CurrencyModelAPI()));

        repository.getRateList("2021-03-20", "MYR");

        assertNull(repository.cachedRateList);
        verify(localDataSource, never()).getRateList("2021-03-20", "MYR");
        verify(remoteDataSource).getRateList("2021-03-20", "MYR");
    }

    @Test
    public void testLocalFileRateList() {

        when(localDataSource.isExpired()).thenReturn(false);
        when(localDataSource.isCached(any())).thenReturn(true);
        when(localDataSource.getRateList(any(), any())).thenReturn(Observable.just(new CurrencyModelAPI()));

        repository.getRateList("2021-03-20", "MYR");

        assertNull(repository.cachedRateList);
        verify(localDataSource).getRateList("2021-03-20", "MYR");
        verify(remoteDataSource, never()).getRateList("2021-03-20", "MYR");
    }

    @Test
    public void testInMemoryRateList() {

        repository.cachedRateList = new CurrencyModelAPI();
        assertNotNull(repository.cachedRateList);

        repository.getRateList("2021-03-20", "MYR");

        verify(localDataSource, never()).getRateList("2021-03-20", "MYR");
        verify(remoteDataSource, never()).getRateList("2021-03-20", "MYR");

    }

    @Test
    public void testSaveRateList() {

        CurrencyModelAPI obj = new CurrencyModelAPI();
        repository.saveRateList(obj);

        assertEquals(obj, repository.cachedRateList);
        verify(localDataSource).saveRateList(obj);
        verify(remoteDataSource).saveRateList(obj);
    }

    @Test
    public void testGetLastUpdated() {

        Observable<String> obj = Observable.just("33m ago");
        when(localDataSource.getLastUpdated()).thenReturn(obj);

        Observable<String> result = repository.getLastUpdated();

        verify(localDataSource).getLastUpdated();
        verify(remoteDataSource, never()).getLastUpdated();
        assertEquals(obj, result);
    }

    @Test
    public void testRefreshCurrencyRate() {

        repository.refreshCurrencyRate();

        verify(localDataSource).evictAll();
    }
}