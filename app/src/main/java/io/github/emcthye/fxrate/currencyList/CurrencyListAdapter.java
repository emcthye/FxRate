package io.github.emcthye.fxrate.currencyList;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.emcthye.fxrate.R;
import io.github.emcthye.fxrate.entity.CurrencyEntity;

public class CurrencyListAdapter extends RecyclerView.Adapter<CurrencyListAdapter.ViewHolder> {

    private CurrencyEntity data = new CurrencyEntity();
    private String lastUpdated = "";
    private Drawable lineGraphGradient;
    private final Map<String, List<Entry>> currencyHistoryGraph = new HashMap<>();

    private CurrencyListContract.View.OnItemClickListener onItemClickListener;

    @Inject
    public CurrencyListAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (lineGraphGradient == null) {
            lineGraphGradient = ResourcesCompat.getDrawable(parent.getContext().getResources(), R.drawable.gradient_drawable, null);
        }
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_currency_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pair<String, String> row = data.rates.get(position);
        String[] rate = row.second.split("-");
        String leftCurrency = rate[0].equalsIgnoreCase("1") ? row.first + " " + rate[0] : data.base + ' ' + rate[1];
        String rightCurrency = rate[0].equalsIgnoreCase("1") ? data.base + ' ' + rate[1] : row.first + " " + rate[0];
        holder.targetCurrency.setText(leftCurrency);
        holder.baseCurrency.setText(rightCurrency);
        holder.currencyName.setText(CurrencyEntity.currencyName.get(row.first));

        LineDataSet set1;
        if (holder.mChart.getData() != null &&
                holder.mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) holder.mChart.getData().getDataSetByIndex(0);
            set1.setFillDrawable(lineGraphGradient);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setValues(currencyHistoryGraph.get(row.first));
            holder.mChart.getData().notifyDataChanged();
            holder.mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(currencyHistoryGraph.get(row.first), "");
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
            holder.mChart.setData(data);
        }
        holder.itemView.setOnClickListener(v -> {
            if(onItemClickListener != null) onItemClickListener.onCurrencyItemClicked(new Pair<>(row.first, data.base));
        });
    }

    @Override
    public int getItemCount() {
        return data.rates.size();
    }

    public void setData(CurrencyEntity data) {
        this.data = data;

        for (Map.Entry<String,String> rateHistory : data.historyRate.entrySet()){
            ArrayList<Entry> values = new ArrayList<>();
            String[] dataPoint = rateHistory.getValue().split(",");

            for (int idx = 0; idx < dataPoint.length; idx++)
                values.add(new Entry(idx, Float.parseFloat(dataPoint[idx])));

            currencyHistoryGraph.put(rateHistory.getKey(), values);
        }
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setOnItemClickListener(CurrencyListContract.View.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvRateCountry) TextView currencyName;
        @BindView(R.id.tvRateValue) TextView targetCurrency;
        @BindView(R.id.tvRateValueBase) TextView baseCurrency;
        @BindView(R.id.chart) LineChart mChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mChart.getXAxis().setEnabled(false);
            mChart.getAxisLeft().setEnabled(false);
            mChart.getAxisRight().setEnabled(false);
            mChart.getDescription().setEnabled(false);
            mChart.getLegend().setEnabled(false);
            mChart.setTouchEnabled(false);
            mChart.setDragEnabled(false);
            mChart.setScaleEnabled(false);
            mChart.setPinchZoom(false);
            mChart.setDrawBorders(false);
            mChart.setDrawGridBackground(false);
        }
    }
}
