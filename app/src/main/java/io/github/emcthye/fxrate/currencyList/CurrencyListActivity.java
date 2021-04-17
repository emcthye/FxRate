package io.github.emcthye.fxrate.currencyList;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.emcthye.fxrate.R;
import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.router.Router;
import io.github.emcthye.fxrate.util.base.BaseActivity;

public class CurrencyListActivity extends BaseActivity implements CurrencyListContract.View {

    @BindView(R.id.rlLoadingView) RelativeLayout loadingView;
    @BindView(R.id.rlEmptyView) RelativeLayout emptyView;
    @BindView(R.id.rlErrorView) RelativeLayout errorView;
    @BindView(R.id.tvLastUpdated) TextView lastUpdated;
    @BindView(R.id.srCurrencyList) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.rvCurrencyList) RecyclerView rvCurrencyList;


    @Inject CurrencyListPresenter presenter;
    @Inject CurrencyListAdapter adapter;
    @Inject Router router;

    private final OnItemClickListener onCurrencyClickListener = new OnItemClickListener() {
        @Override
        public void onCurrencyItemClicked(Pair<String, String> currencyPair) {
            router.navigateToCurrencyTimeline(CurrencyListActivity.this, currencyPair);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getApplicationComponent().inject(this);
        presenter.setView(this);
        ButterKnife.bind(this);

        swipeRefresh.setOnRefreshListener(() -> presenter.refreshList());
        rvCurrencyList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL,false));
        rvCurrencyList.setAdapter(adapter);
        adapter.setOnItemClickListener(onCurrencyClickListener);

    }

    @Override
    public void onResume() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.result(requestCode, resultCode);
    }

    @Override
    public void render(Object result) {

        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        swipeRefresh.setRefreshing(false);
        swipeRefresh.setVisibility(View.VISIBLE);
        adapter.setData((CurrencyEntity) result);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderLoading() {
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        swipeRefresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderEmpty() {
        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.INVISIBLE);
        swipeRefresh.setVisibility(View.INVISIBLE);
    }

    @Override
    public void renderError() {
        loadingView.setVisibility(View.INVISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        errorView.setVisibility(View.VISIBLE);
        swipeRefresh.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.btnErrorRetry)
    void errorRetryClicked() {
        renderLoading();
        presenter.refreshList();
    }

    @Override
    public void setLastUpdate(String lastUpdateText) {
        lastUpdated.setText("Last Updated " + lastUpdateText);
    }
}