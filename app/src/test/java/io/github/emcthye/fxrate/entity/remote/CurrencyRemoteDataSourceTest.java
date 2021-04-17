package io.github.emcthye.fxrate.entity.remote;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyRemoteDataSourceTest {

    private CurrencyRemoteDataSource remoteDataSource;

    @Mock CurrencyAPI api;

    @Before
    public void setUp() throws Exception {
        remoteDataSource = new CurrencyRemoteDataSource(api);
    }

    @Test
    public void getRateList() {

        remoteDataSource.getRateList("2020-03-21", "MYR");

        verify(api).getCurrencyList("2020-03-21", "MYR");
    }
}