package io.github.emcthye.fxrate.currencyTimeline;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emcthye.fxrate.R;
import io.github.emcthye.fxrate.util.base.BaseActivity;

public class CurrencyTimelineActivity extends BaseActivity implements CurrencyTimelineContract.View {

    private static final String INTENT_EXTRA_PARAM_CURRENCY_BASE = "io.github.emcthye.INTENT_EXTRA_PARAM_CURRENCY_BASE";
    private static final String INTENT_EXTRA_PARAM_CURRENCY_TARGET = "io.github.emcthye.INTENT_EXTRA_PARAM_CURRENCY_TARGET";

    private String baseCurrency;
    private String targetCurrency;

    @BindView(R.id.rlLoadingView) RelativeLayout loadingView;
    @BindView(R.id.rlEmptyView) RelativeLayout emptyView;
    @BindView(R.id.rlErrorView) RelativeLayout errorView;
    @BindView(R.id.llTimelineContainer) LinearLayout resultContainer;
    @BindView(R.id.tvRateValue) TextView targetCurrencyText;
    @BindView(R.id.tvRateValueBase) TextView baseCurrencyText;
    @BindView(R.id.chart) LineChart mChart;

    @Inject CurrencyTimelinePresenter presenter;

    public static Intent getCallingIntent(Context context,  Pair<String,String> currencyPair) {
        Intent callingIntent = new Intent(context, CurrencyTimelineActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_CURRENCY_TARGET, currencyPair.first);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_CURRENCY_BASE, currencyPair.second);

        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_timeline);
        getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        initializeActivity(savedInstanceState);
    }

    @Override
    protected void onResume(){
        super.onResume();
        renderLoading();
        presenter.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null) {
            outState.putString(INTENT_EXTRA_PARAM_CURRENCY_BASE, baseCurrency);
            outState.putString(INTENT_EXTRA_PARAM_CURRENCY_TARGET, targetCurrency);
        }
        super.onSaveInstanceState(outState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            baseCurrency = getIntent().getStringExtra(INTENT_EXTRA_PARAM_CURRENCY_BASE);
            targetCurrency = getIntent().getStringExtra(INTENT_EXTRA_PARAM_CURRENCY_TARGET);
        } else {
            baseCurrency = savedInstanceState.getString(INTENT_EXTRA_PARAM_CURRENCY_BASE);
            targetCurrency = savedInstanceState.getString(INTENT_EXTRA_PARAM_CURRENCY_TARGET);
        }
    }

    @Override
    public void render(Object result) {

        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        resultContainer.setVisibility(View.VISIBLE);

        Map<String, List<Entry>> currencyHistoryGraph = (Map<String, List<Entry>>) result;
        Drawable lineGraphGradient = ResourcesCompat.getDrawable(getResources(), R.drawable.gradient_drawable, null);

        Entry latestRate = currencyHistoryGraph.get(targetCurrency).get(currencyHistoryGraph.get(targetCurrency).size() - 1);
//        String rate = latestRate.getY() < 1 ?
//                1 + "-" + 1/latestRate.getY() :
//                latestRate.getY() + "-" + 1;
//        String[] currencyRate = rate.split("-");

        targetCurrencyText.setText(targetCurrency + " " + latestRate.getY());
        baseCurrencyText.setText(baseCurrency + " " + 1);

        mChart.getXAxis().setEnabled(false);
        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setEnabled(false);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setDrawBorders(false);
        mChart.setDrawGridBackground(false);

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setFillDrawable(lineGraphGradient);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setValues(currencyHistoryGraph.get(targetCurrency));
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(currencyHistoryGraph.get(targetCurrency), "");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setColor(Color.parseColor("#FFC272"));
            set1.setDrawValues(false);
            set1.setDrawCircles(false);
            set1.setLineWidth(3f);
            set1.setDrawFilled(true);
            set1.setFillDrawable(lineGraphGradient);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }
    }

    @Override
    public void renderLoading() {
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        resultContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderEmpty() {
        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        resultContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderError() {
        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        resultContainer.setVisibility(View.INVISIBLE);
    }
}