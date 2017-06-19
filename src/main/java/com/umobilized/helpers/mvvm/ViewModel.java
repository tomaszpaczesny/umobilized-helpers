package com.umobilized.helpers.mvvm;

/**
 * ViewModel interface. It is capable of listening to activity/fragment lifecycle events.
 * Rest of the ViewModel implementation depends on what is needed.
 *
 * Created by tpaczesny on 2017-05-11.
 */

public interface ViewModel {
    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();
}
