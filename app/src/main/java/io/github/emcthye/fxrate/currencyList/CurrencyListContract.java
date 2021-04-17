package io.github.emcthye.fxrate.currencyList;

import android.content.Context;

import androidx.core.util.Pair;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import io.github.emcthye.fxrate.util.base.BasePresenter;
import io.github.emcthye.fxrate.util.base.BaseView;

public interface CurrencyListContract {

    interface View extends BaseView<Presenter, Object> {

        interface OnItemClickListener {
            void onCurrencyItemClicked(Pair<String, String> currencyPair);
        }

        void setLastUpdate(String lastUpdate);
    }

    interface Presenter extends BasePresenter {

    }

    interface Router {
        void navigateToCurrencyTimeline(Context context, Pair<String, String> currencyPair);
    }
}
