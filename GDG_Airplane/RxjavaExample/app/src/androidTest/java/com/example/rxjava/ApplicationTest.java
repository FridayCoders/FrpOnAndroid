package com.example.rxjava;

import android.app.Application;
import android.test.ApplicationTestCase;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.schedulers.TestScheduler;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.example.rxjava.schedulers.SchedulerManager;
import com.example.rxjava.viewmodel.MainActivityModel;
import com.example.rxjava.viewmodel.RemoteAirplaneModel;

public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    TestScheduler testScheduler;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // 테스트를 위한 스케쥴러를 설정한다.
        testScheduler = Schedulers.test();
        SchedulerManager.setMain(testScheduler);
        SchedulerManager.setIO(testScheduler);
    }

    public void testAirplaneStatus() {
        // 가상의 비동기 작업을 처리해주는 Mock Function
        Func1<Integer, Observable<Void>> justDoneFuction = event -> Observable.just(null);

        // 기본적인 ViewModel 설정
        MainActivityModel mainActivityModel = new MainActivityModel();
        RemoteAirplaneModel remoteAirplaneModel = new RemoteAirplaneModel(mainActivityModel, justDoneFuction, justDoneFuction, justDoneFuction);

        // 테스트는 Status Text의 변화를 비교
        ArrayList<String> testResults = new ArrayList<>();
        remoteAirplaneModel.statusText.distinctUntilChanged().subscribe(testResults::add);

        // 처음에는 기본값만 존재함
        assertEquals(1, testResults.size());

        // 사용자가 이륙 버튼을 클릭함
        mainActivityModel.takeoffBtn.emit();

        // 하지만 상태 메시지는 변하지 않음
        assertEquals(1, testResults.size());

        // 시간을 가상으로 2초 경과시킴
        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS);

        // 새로운 상태 메시지가 추가됨
        assertEquals(2, testResults.size());
        assertEquals(AirplaneStatusText.RED, testResults.get(0));
        assertEquals(AirplaneStatusText.GREEN, testResults.get(1));
    }
}