package io.github.emcthye.fxrate.entity.local;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;

import io.github.emcthye.fxrate.entity.remote.CurrencyModelAPI;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class CurrencyLocalDataSourceTest {

    private CurrencyLocalDataSource local;

    @Mock FileManager fileManager;
    @Mock Serializer serializer;
    @Mock ThreadExecutor threadExecutor;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        CurrencyLocalDataSource localDataSource = new CurrencyLocalDataSource(
                InstrumentationRegistry.getInstrumentation().getTargetContext(),
                serializer,
                fileManager,
                threadExecutor);
        local = Mockito.spy(localDataSource);
    }

    @Test
    public void isCached() {

        local.isCached("MYR");
        verify(fileManager).exists(any());
    }

    @Test
    public void saveRateList() {

        when(serializer.serialize(any())).thenReturn("test");

        doReturn(mock(File.class)).when(local).buildFile(any());

        CurrencyModelAPI currencyModelAPI = new CurrencyModelAPI();
        local.saveRateList(currencyModelAPI);

        ArgumentCaptor<CurrencyModelAPI> obj = ArgumentCaptor.forClass(CurrencyModelAPI.class);
        verify(serializer).serialize(obj.capture());
        assertEquals(currencyModelAPI, obj.getValue());

        verify(local).executeAsynchronously(any());
    }

    @Test
    public void getRateList() throws IOException {

        when(serializer.deserialize(any())).thenReturn("test");

        local.getRateList("2020-03-22", "MYR").subscribeWith(new Observer<CurrencyModelAPI>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull CurrencyModelAPI currencyModelAPI) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        verify(serializer).deserialize(any());
        verify(fileManager).readFileContent(any());

    }

    @Test
    public void getLastUpdated() {

        local.getLastUpdated().subscribeWith(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull String s) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        verify(local).getLastCacheUpdateTimeMillis();
    }

    @Test
    public void isExpired() {

        local.isExpired();

        verify(local).getLastCacheUpdateTimeMillis();

    }

    @Test
    public void evictAll() {

        local.evictAll();

        verify(local).executeAsynchronously(any());
    }
}