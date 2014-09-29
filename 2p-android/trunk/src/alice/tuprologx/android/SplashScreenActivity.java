package alice.tuprologx.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends Activity {

  private final int SPLASH_DISPLAY_LENGHT = 1000;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.splashscreen);

    new Handler().postDelayed(new Runnable() {

      public void run() {

        Intent mainIntent = new Intent(SplashScreenActivity.this,
            tuPrologActivity.class);

        SplashScreenActivity.this.startActivity(mainIntent);

        SplashScreenActivity.this.finish();

      }
    }, SPLASH_DISPLAY_LENGHT);

  }

}
