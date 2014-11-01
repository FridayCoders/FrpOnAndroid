package com.example.rxjava.util;

import android.widget.TextView;
import rx.Observable;

public class TextBinder {
    /**
     * TextView 를 주면 Observable<String>의 Text를 적용해 주는 코드
     */
    public static void apply(TextView textView, Observable<String> text) {
        text.subscribe(textView::setText);
    }
}
