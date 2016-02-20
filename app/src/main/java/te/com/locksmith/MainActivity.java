package te.com.locksmith;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

import te.com.locksmith.adapters.ActionBarAdapter;
import te.com.locksmith.adapters.LeftNavAdapter;
import te.com.locksmith.constants.ActiveFragmentConservative;
import te.com.locksmith.dao.ActivityResultEvent;
import te.com.locksmith.helpers.ActivityResultBus;
import te.com.locksmith.helpers.BackStackHelper;
import te.com.locksmith.helpers.OnBackPressedHelper;
import te.com.locksmith.tools.FragmentChanger;

public class MainActivity extends AppCompatActivity {

    private Drawer leftNavDrawer = null;
    private boolean doubleBackToExitPressedOnce = false;
    public View activityRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.activityRootView = findViewById(R.id.activityRoot);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ActionBarAdapter
        ActionBarAdapter actionBarAdapter = new ActionBarAdapter(MainActivity.this, getSupportActionBar());

        Bundle extras = getIntent().getExtras();

        //Check for notification click
        if (extras != null) {
            //Create the drawer
            leftNavDrawer = new LeftNavAdapter(MainActivity.this, toolbar, actionBarAdapter, false).build();

        } else {
            //Create the drawer
            leftNavDrawer = new LeftNavAdapter(MainActivity.this, toolbar, actionBarAdapter, true).build();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResultBus.getInstance().postQueue(
                new ActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (leftNavDrawer != null && leftNavDrawer.isDrawerOpen()) {
            leftNavDrawer.closeDrawer();
        } else if (OnBackPressedHelper.getOnBackPressed() != null) {
            OnBackPressedHelper.getOnBackPressed().Run();
        } else if (!BackStackHelper.isEmty()) {
            new FragmentChanger(MainActivity.this, BackStackHelper.pop(), null, false, ActiveFragmentConservative.fragment.getClass());
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Çıkmak için tekrar basın", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Uygulama açıldığında daha önceden verilmiş olan notifleri siliyorum
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
