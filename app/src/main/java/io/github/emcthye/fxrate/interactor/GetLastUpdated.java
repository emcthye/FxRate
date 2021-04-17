package io.github.emcthye.fxrate.interactor;

import javax.inject.Inject;

import io.github.emcthye.fxrate.entity.CurrencyRepository;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.github.emcthye.fxrate.util.threading.UIThread;
import io.reactivex.Observable;

public class GetLastUpdated extends UseCase<String, Void>{

    private final CurrencyRepository repository;

    @Inject
    GetLastUpdated(ThreadExecutor threadExecutor, UIThread UIThread, CurrencyRepository repository) {
        super(threadExecutor, UIThread);
        this.repository = repository;
    }

    @Override
    Observable<String> buildUseCaseObservable(Void unused) {
        return repository.getLastUpdated();
    }
}
