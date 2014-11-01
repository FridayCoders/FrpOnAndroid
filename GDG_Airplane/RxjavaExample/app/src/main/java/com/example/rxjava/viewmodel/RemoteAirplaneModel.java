package com.example.rxjava.viewmodel;


import rx.Observable;
import rx.functions.Func1;

import java.util.concurrent.TimeUnit;

import com.example.rxjava.AirplaneStatusText;
import com.example.rxjava.RemoteEventType;
import com.example.rxjava.schedulers.SchedulerManager;
import com.example.rxjava.util.SubjectUtil;

public class RemoteAirplaneModel {

    // 리모컨으로 부터 신호를 수신하는 딜레이
    final static int REMOTE_DELAY_MS = 100;
    // Status 텍스트가 표시되기 까지의 딜레이
    final static int STATUS_TEXT_DELAY_MS = 1000;

    // Status Text
    public final Observable<String> statusText;

    public RemoteAirplaneModel(MainActivityModel activityModel,
                               Func1<Integer, Observable<Void>> setupEngine,
                               Func1<Integer, Observable<Void>> setupSpeed,
                               Func1<Integer, Observable<Void>> setupAltitude) {
        final SubjectUtil subjectUtil = activityModel.subjectUtil;

        Observable<RemoteEventType> receivedEvent = subjectUtil.publishSubject(activityModel.remoteEvent.delay(REMOTE_DELAY_MS, TimeUnit.MILLISECONDS, SchedulerManager.main()));

        statusText = subjectUtil.behaviorSubject(receivedEvent.switchMap(remoteEventType -> {
            switch (remoteEventType) {
                case LANDING:
                    return Observable.zip(setupAltitude.call(0), setupSpeed.call(0), (event1, event2) -> null)
                            .flatMap(event -> setupEngine.call(0).map(event2 -> AirplaneStatusText.RED))
                            .delay(STATUS_TEXT_DELAY_MS, TimeUnit.MILLISECONDS, SchedulerManager.main());
                case TAKE_OFF:
                    return setupEngine.call(100).flatMap(
                            event -> Observable.zip(
                                    setupAltitude.call(100), setupSpeed.call(100),
                                    (event1, event2) -> AirplaneStatusText.GREEN
                            )
                    ).delay(STATUS_TEXT_DELAY_MS, TimeUnit.MILLISECONDS, SchedulerManager.main());
                default:
                    return Observable.<String>never();
            }
        }), AirplaneStatusText.RED);
    }
}
