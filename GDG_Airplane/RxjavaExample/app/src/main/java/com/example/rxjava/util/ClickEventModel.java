package com.example.rxjava.util;

import android.view.View;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ClickEventModel {

    private final BehaviorSubject<Void> eventSubject = BehaviorSubject.create();
    public final Observable<Void> event = eventSubject;

    /**
     * View 를 주면 View의 Click Event를 Observable<Void>로 변경하는 코드
     */
    public void apply(View v) {
        v.setOnClickListener(event -> emit());
    }

    public void emit() {
        eventSubject.onNext(null);
    }
}
