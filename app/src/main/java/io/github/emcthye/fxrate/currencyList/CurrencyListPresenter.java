package io.github.emcthye.fxrate.currencyList;

import android.util.Log;

import javax.inject.Inject;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.interactor.GetCurrencyList;
import io.github.emcthye.fxrate.interactor.GetLastUpdated;
import io.github.emcthye.fxrate.util.base.BaseView;
import io.github.emcthye.fxrate.currencyList.CurrencyListContract;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class CurrencyListPresenter implements CurrencyListContract.Presenter {

    public CurrencyListContract.View currencyListView;

    final GetCurrencyList getCurrencyListUseCase;
    final GetLastUpdated getLastUpdatedUseCase;

    @Inject
    public CurrencyListPresenter(GetCurrencyList getCurrencyListUseCase, GetLastUpdated getLastUpdatedUseCase) {
        this.getCurrencyListUseCase = getCurrencyListUseCase;
        this.getLastUpdatedUseCase = getLastUpdatedUseCase;
    }

    @Override
    public void setView(BaseView v) {
        currencyListView =  (CurrencyListContract.View) v;
    }

    private void getCurrencyList(String baseCurrency, boolean forceUpdate) {
        getCurrencyListUseCase.execute(new DisposableObserver<CurrencyEntity>() {
            @Override
            public void onNext(@NonNull CurrencyEntity currencyEntity) {
                if (currencyEntity.rates.isEmpty()) {
                    currencyListView.renderEmpty();
                } else {
                    currencyListView.render(currencyEntity);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("CurrencyListPresenter", e.toString());
                currencyListView.renderError();
            }

            @Override
            public void onComplete() {
                getLastUpdated();
            }
        }, GetCurrencyList.Params.create(baseCurrency, forceUpdate));
    }

    public void refreshList() {
        getCurrencyList("MYR", true);
    }

    public void getLastUpdated() {
        getLastUpdatedUseCase.execute(new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                currencyListView.setLastUpdate(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }, null);
    }

    @Override
    public void start() {
        getCurrencyList("MYR", false);
    }

    @Override
    public void destroy() {
        currencyListView = null;
        getCurrencyListUseCase.dispose();
        getLastUpdatedUseCase.dispose();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void result(int requestCode, int resultCode) {

    }
}
