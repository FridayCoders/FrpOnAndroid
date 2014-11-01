package com.example.rxjava.viewmodel;

import rx.Observable;
import rx.subjects.PublishSubject;

import com.example.rxjava.RemoteEventType;
import com.example.rxjava.util.ClickEventModel;
import com.example.rxjava.util.SubjectUtil;

public class MainActivityModel {

    public final PublishSubject<Void> onDestroy = PublishSubject.create();

    public final SubjectUtil subjectUtil = new SubjectUtil(observable -> observable.takeUntil(onDestroy));

    public final ClickEventModel takeoffBtn = new ClickEventModel();
    public final ClickEventModel landingBtn = new ClickEventModel();

    public final Observable<RemoteEventType> remoteEvent = subjectUtil.publishSubject(
        Observable.merge(
            takeoffBtn.event.map(event -> RemoteEventType.TAKE_OFF),
            landingBtn.event.map(event -> RemoteEventType.LANDING)
        )
    );
}
