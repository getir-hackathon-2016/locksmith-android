package te.com.locksmith;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import te.com.locksmith.tools.Tools;

/**
 * Created by enes on 20/02/16.
 */
public class SplashScreen extends Activity {
    /**
     * Check if the app is running.
     */
    private boolean isRunning;
    private Tools tools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        isRunning = true;

        tools = new Tools(SplashScreen.this);

        if (tools.isOnline()) {
            startSplash();
        } else {
            Toast.makeText(SplashScreen.this, "Uygulamanın çalışabilmesi için internet gereklidir", Toast.LENGTH_LONG).show();
            this.finish();
        }

    }

    private void startSplash() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            doFinish();
                        }
                    });
                }
            }
        }).start();
    }

    private synchronized void doFinish() {

        if (isRunning) {
            isRunning = false;

            boolean isMember = tools.getSharedPreference("isMember", false);

            Intent intent;

            if (!isMember) {
                intent = new Intent(SplashScreen.this, RegisterActivity.class);
            } else {
                intent = new Intent(SplashScreen.this, MainActivity.class);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isRunning = false;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
