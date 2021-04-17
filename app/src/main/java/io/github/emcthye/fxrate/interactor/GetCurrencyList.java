package io.github.emcthye.fxrate.interactor;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import io.github.emcthye.fxrate.entity.CurrencyEntity;
import io.github.emcthye.fxrate.entity.CurrencyRepository;
import io.github.emcthye.fxrate.entity.mapper.CurrencyModelMapper;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.reactivex.Observable;

public class GetCurrencyList extends UseCase<CurrencyEntity, GetCurrencyList.Params> {

    private final CurrencyRepository currencyRepository;
    private final CurrencyModelMapper mapper;

    @Inject
    public GetCurrencyList(CurrencyRepository currencyRepository, CurrencyModelMapper mapper, ThreadExecutor threadExecutor, UIThread UIThread) {
        super(threadExecutor, UIThread);
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    @Override
    Observable<CurrencyEntity> buildUseCaseObservable(Params params) {
        if (params.forceUpdate) currencyRepository.refreshCurrencyRate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -10);
        String dateString = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
        return currencyRepository.getRateList(dateString, params.baseCurrency).map(this.mapper::transform);
    }

    public static class Params {

        final String baseCurrency;
        public final boolean forceUpdate;

        private Params(String baseCurrency, boolean forceUpdate) {
            this.baseCurrency = baseCurrency;
            this.forceUpdate = forceUpdate;
        }

        public static Params create(String baseCurrency, boolean forceUpdate) {
            return new Params(baseCurrency, forceUpdate);
        }
    }
}
