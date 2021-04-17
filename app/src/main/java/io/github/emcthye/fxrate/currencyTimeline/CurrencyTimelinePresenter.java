package io.github.emcthye.fxrate.currencyTimeline;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.interactor.GetCurrencyList;
import io.github.emcthye.fxrate.util.base.BaseView;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

public class CurrencyTimelinePresenter implements CurrencyTimelineContract.Presenter {

    private CurrencyTimelineContract.View view;
    private GetCurrencyList getCurrencyListUseCase;

    @Inject
    public CurrencyTimelinePresenter(GetCurrencyList getCurrencyListUseCase) {
        this.getCurrencyListUseCase = getCurrencyListUseCase;
    }

    @Override
    public void setView(BaseView v) {
        view = (CurrencyTimelineContract.View) v;
    }

    @Override
    public void start() {

        getCurrencyListUseCase.execute(new DisposableObserver<CurrencyEntity>() {
            @Override
            public void onNext(@NonNull CurrencyEntity data) {

                Map<String, List<Entry>> currencyHistoryGraph = new HashMap<>();

                for (Map.Entry<String,String> rateHistory : data.historyRate.entrySet()){
                    ArrayList<Entry> values = new ArrayList<>();
                    String[] dataPoint = rateHistory.getValue().split(",");

                    for (int idx = 0; idx < dataPoint.length; idx++)
                        values.add(new Entry(idx, Float.parseFloat(dataPoint[idx])));

                    currencyHistoryGraph.put(rateHistory.getKey(), values);
                }

                view.render(currencyHistoryGraph);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                view.renderError();
            }

            @Override
            public void onComplete() {

            }
        }, GetCurrencyList.Params.create("MYR", false));
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        view = null;
        getCurrencyListUseCase.dispose();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }
}
