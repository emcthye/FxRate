package io.github.emcthye.fxrate.currencyList;

import androidx.core.util.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.interactor.GetCurrencyList;
import io.github.emcthye.fxrate.interactor.GetLastUpdated;
import io.github.emcthye.fxrate.util.threading.UIThreadImpl;
import io.reactivex.observers.DisposableObserver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyListPresenterTest {

    private CurrencyListPresenter presenter;

    @Mock private CurrencyListContract.View currencyListView;
    @Mock private GetCurrencyList getCurrencyList;
    @Mock private GetLastUpdated getLastUpdated;

    @Before
    public void setUp() {
        presenter = new CurrencyListPresenter(getCurrencyList, getLastUpdated);
        presenter.setView(currencyListView);
    }

    @Test
    public void setView() {
        presenter.setView(currencyListView);
        assertNotNull(presenter.currencyListView);
    }

    @Test
    public void refreshList() {

        Mockito.mock(UIThreadImpl.class);

        ArgumentCaptor<GetCurrencyList.Params> valueCapture = ArgumentCaptor.forClass(GetCurrencyList.Params.class);

        presenter.refreshList();
        verify(getCurrencyList).execute(any(DisposableObserver.class), valueCapture.capture());
        assertTrue(valueCapture.getValue().forceUpdate);
    }

    @Test
    public void getLastUpdated() {

        doAnswer((Answer<Void>) invocation -> {
            DisposableObserver<String> observer = invocation.getArgument(0);
            observer.onNext("Last Updated");
            verify(currencyListView).setLastUpdate("Last Updated");
            return null;
        }).when(getLastUpdated).execute(any(),any());

        presenter.getLastUpdated();

        verify(getLastUpdated).execute(any(DisposableObserver.class), any());
    }

    @Test
    public void start() {

        ArgumentCaptor<DisposableObserver> valueCapture = ArgumentCaptor.forClass(DisposableObserver.class);
        ArgumentCaptor<GetCurrencyList.Params> forceUpdateCapture = ArgumentCaptor.forClass(GetCurrencyList.Params.class);

        presenter.start();

        verify(getCurrencyList).execute(valueCapture.capture(), forceUpdateCapture.capture());
        CurrencyEntity obj = new CurrencyEntity();
        List<Pair<String,String>> rate = new ArrayList<>();
        rate.add(new Pair<>("USD", "1-0.25"));
        obj.rates = rate;
        DisposableObserver<CurrencyEntity> callback = valueCapture.getValue();
        callback.onNext(obj);

        verify(currencyListView).render(obj);
        assertFalse(forceUpdateCapture.getValue().forceUpdate);
    }

    @Test
    public void testGetCurrencyListHappyCase() {

        doAnswer((Answer<Void>) invocation -> {
            DisposableObserver<CurrencyEntity> callback = invocation.getArgument(0);
            CurrencyEntity obj = new CurrencyEntity();
            List<Pair<String,String>> rate = new ArrayList<>();
            rate.add(new Pair<>("USD", "1-0.25"));
            obj.rates = rate;
            callback.onNext(obj);
            verify(currencyListView).render(obj);
            return null;
        }).when(getCurrencyList).execute(any(), any());

        presenter.start();
    }

    @Test
    public void destroy() {

        presenter.destroy();

        verify(getCurrencyList).dispose();
        verify(getLastUpdated).dispose();
        assertNull(presenter.currencyListView);
    }
}