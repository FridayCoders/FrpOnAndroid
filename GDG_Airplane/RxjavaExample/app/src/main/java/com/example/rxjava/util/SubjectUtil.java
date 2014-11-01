package com.example.rxjava.util;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class SubjectUtil {

    private final Func1<Observable, Observable> additionalOption;

    public SubjectUtil(Func1<Observable, Observable> additionalOption) {
        this.additionalOption = additionalOption;
    }

    /**
     * publishSubjtect를 생성
     */
    public <T> Observable<T> publishSubject(Observable<T> observable) {
        PublishSubject<T> result = PublishSubject.create();
        additionalOption.call(observable).subscribe(result);
        return result;
    }

    /**
     * behaviorSubject를 생성
     */
    public <T> Observable<T> behaviorSubject(Observable<T> observable, T initialValue) {
        BehaviorSubject<T> result = BehaviorSubject.create(initialValue);
        additionalOption.call(observable).subscribe(result);
        return result;
    }
}
