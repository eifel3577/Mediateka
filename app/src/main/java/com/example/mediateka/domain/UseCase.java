package com.example.mediateka.domain;

import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.DisposableSubscriber;

/**общий класс для юзкейсов */
public abstract class UseCase<T> {

    protected int pageIndex = 1;

    /**поток выполнения */
    private final Scheduler executorThread;
    /**главный поток */
    private final Scheduler uiThread;
    /**хранилище подписки */
    private final CompositeDisposable compositeDisposable;

    public UseCase(Scheduler executorThread,
                   Scheduler uiThread) {
        this.executorThread = executorThread;
        this.uiThread = uiThread;
        compositeDisposable = new CompositeDisposable();
    }

    /**Создает {@link Flowable<T>}.Непосредственно {@link Flowable<T>} создается наследниками в переопределенном
     * методе createUseCase().К получившимуся {@link Flowable<T>} прикрепляется подписчик {@link UseCaseSubscriber<T>}
     * Подписчик кладется в хранилище подписки {@link CompositeDisposable}
     *
     * @param disposableSubscriber
     *  */
    public void subscribe(DisposableSubscriber<T> disposableSubscriber){
        if (disposableSubscriber == null){
            throw new IllegalArgumentException("subscriber must not be null");
        }
        Flowable<T> flowable = createUseCase()
                .subscribeOn(executorThread)
                .observeOn(uiThread);

        DisposableSubscriber<T> subscriber = flowable.subscribeWith(disposableSubscriber);
        compositeDisposable.add(subscriber);
    }

    public void setCurrentPage(int pageIndex){
        this.pageIndex = pageIndex;
    }

    public void dispose(){
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    protected CompletableTransformer applyCompletableSchedulers(){
        return upstream -> upstream.subscribeOn(executorThread)
                .observeOn(uiThread);
    }

    protected abstract Flowable<T> createUseCase();
}
