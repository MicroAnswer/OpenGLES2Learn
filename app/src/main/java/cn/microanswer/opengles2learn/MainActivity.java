package cn.microanswer.opengles2learn;

import androidx.appcompat.app.AppCompatActivity;
import cn.microanswer.opengles2learn.view.MyTDView;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MyTDView myTDView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        myTDView = new MyTDView(this);
        myTDView.requestFocus();
        myTDView.setFocusableInTouchMode(true);

        setContentView(myTDView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myTDView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myTDView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myTDView.rThread.flag = false;
    }
}
