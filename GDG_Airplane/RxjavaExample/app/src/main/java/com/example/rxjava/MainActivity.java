package com.example.rxjava;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rxjava.util.TextBinder;
import com.example.rxjava.viewmodel.MainActivityModel;
import com.example.rxjava.viewmodel.RemoteAirplaneModel;
import com.example.rxjava.virtual.Hardware;

public class MainActivity extends Activity {

    // 테스트를 위한 가상의 하드웨어
    Hardware engine = new Hardware(10);
    Hardware speed = new Hardware(20);
    Hardware altitude = new Hardware(30);

    // ViewModel
    MainActivityModel mainActivityModel = new MainActivityModel();
    RemoteAirplaneModel remoteAirplaneModel = new RemoteAirplaneModel(mainActivityModel, engine.setupFunction, speed.setupFunction, altitude.setupFunction);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linkViewModelAndView();
    }

    /**
     * View Model과 View의 연결
     */
    private void linkViewModelAndView() {
        mainActivityModel.takeoffBtn.apply(findViewById(R.id.btn_take_off));
        mainActivityModel.landingBtn.apply(findViewById(R.id.btn_landing));

        TextBinder.apply((TextView) findViewById(R.id.text_engine), engine.valueForTest.map(String::valueOf));
        TextBinder.apply((TextView) findViewById(R.id.text_speed), speed.valueForTest.map(String::valueOf));
        TextBinder.apply((TextView) findViewById(R.id.text_altitude), altitude.valueForTest.map(String::valueOf));
        TextBinder.apply((TextView) findViewById(R.id.text_status), remoteAirplaneModel.statusText);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 메모리 해지를 위해 onDestroy 이벤트를 호출
        mainActivityModel.onDestroy.onNext(null);
        mainActivityModel.onDestroy.onCompleted();
    }
}
