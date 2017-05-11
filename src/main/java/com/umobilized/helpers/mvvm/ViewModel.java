package com.umobilized.helpers.mvvm;

/**
 * Created by tpaczesny on 2017-05-11.
 */

public interface ViewModel {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();
}
