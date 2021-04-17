package io.github.emcthye.fxrate.interactor;

import io.github.emcthye.fxrate.util.threading.UIThread;
import io.github.emcthye.fxrate.util.threading.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 *
 * By convention each UseCase implementation will return the result using a {@link DisposableObserver}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase<T, Params> {

  final ThreadExecutor threadExecutor;
  final UIThread UIThread;
  final CompositeDisposable disposables;

  UseCase(ThreadExecutor threadExecutor, UIThread UIThread) {
    this.threadExecutor = threadExecutor;
    this.UIThread = UIThread;
    this.disposables = new CompositeDisposable();
  }

  abstract Observable<T> buildUseCaseObservable(Params params);

  public void execute(DisposableObserver<T> observer, Params params) {
    final Observable<T> observable = this.buildUseCaseObservable(params)
        .subscribeOn(Schedulers.from(threadExecutor))
        .observeOn(UIThread.getScheduler());
    addDisposable(observable.subscribeWith(observer));
  }

  /**
   * Dispose from current {@link CompositeDisposable}.
   */
  public void dispose() {
    if (!disposables.isDisposed()) {
      disposables.dispose();
    }
  }

  /**
   * Dispose from current {@link CompositeDisposable}.
   */
  private void addDisposable(Disposable disposable) {
    disposables.add(disposable);
  }

}
