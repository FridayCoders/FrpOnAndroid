package com.example.rxjava.virtual;

import android.os.SystemClock;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

import com.example.rxjava.schedulers.SchedulerManager;

public class Hardware {

    // 현재 값
    private int currentValue = 0;

    // 갱신 속도
    private int updateSpeedMs;

    // 요청을 순차적으로 처리하기 위한 subject
    private final PublishSubject<Action0> request = PublishSubject.create();
    // 결과 값을 표시하기 위한 Subject
    private final BehaviorSubject<Integer> valueSubject = BehaviorSubject.create(currentValue);
    public final Observable<Integer> valueForTest = valueSubject.observeOn(SchedulerManager.main());

    public Hardware(int updateSpeedMs) {
        this.updateSpeedMs = updateSpeedMs;
        request.observeOn(SchedulerManager.io()).subscribe(Action0::call);
    }

    public final Func1<Integer, Observable<Void>> setupFunction = target -> Observable.create(new Observable.OnSubscribe<Void>() {
        @Override
        public void call(Subscriber<? super Void> subscriber) {
            request.onNext(() -> {
                if (subscriber.isUnsubscribed()) {
                    return ;
                }
                int from = currentValue;
                int diff = target > from ? 1 : -1;
                for (int i = from; i != target; i += diff) {
                    SystemClock.sleep(updateSpeedMs);
                    currentValue = i;
                    valueSubject.onNext(currentValue);
                }
                currentValue = target;
                valueSubject.onNext(currentValue);

                subscriber.onNext(null);
                subscriber.onCompleted();
            });
        }
    });
}
