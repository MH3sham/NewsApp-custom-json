package app.com.example.vip.koranewsuserapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///////FullScreen Activity
        //This is must be called before content and super.
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //////FullScreen Activity

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        splash();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void splash(){

        Handler friend = new Handler();

        friend.postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this, ViewAllPosts.class));
                finish();
            }
        }, 3000);

    }
}
