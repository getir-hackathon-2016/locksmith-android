package te.com.locksmith.customComponets;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.squareup.otto.Subscribe;

import te.com.locksmith.dao.ActivityResultEvent;
import te.com.locksmith.helpers.ActivityResultBus;

/**
 * Created by enes on 21/02/16.
 */
public class CustomFragment extends Fragment {
    protected FragmentActivity context;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.context = (FragmentActivity) activity;
    }

    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };
}
