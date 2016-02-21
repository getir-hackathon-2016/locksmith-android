package te.com.locksmith.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.orhanobut.logger.Logger;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.skyfishjy.library.RippleBackground;

import net.frakbot.jumpingbeans.JumpingBeans;

import te.com.locksmith.R;
import te.com.locksmith.constants.Globals;
import te.com.locksmith.customComponets.CustomFragment;
import te.com.locksmith.customComponets.TextAwesome;
import te.com.locksmith.helpers.ActionBarHelper;
import te.com.locksmith.helpers.ErrorFragmentHelper;
import te.com.locksmith.interfaces.OnGPSConnected;
import te.com.locksmith.interfaces.OnGPSTimeOut;
import te.com.locksmith.interfaces.OnGPSWhenClose;
import te.com.locksmith.tools.FragmentChanger;
import te.com.locksmith.tools.GPSTracker;
import te.com.locksmith.tools.Tools;

/**
 * Created by enes on 21/02/16.
 */
public class DetectLocation extends CustomFragment {
    private GPSTracker gpsTracker;
    private String aramaSonuc = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTracker = new GPSTracker(context, true, null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detect_location, null);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        ActionBarHelper.setTitle("Locksmith");
        ActionBarHelper.hideRightButton();

        int resp = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resp == ConnectionResult.SUCCESS) {
            gpsTracker.getLocation(onGPSWhenClose, onGPSConnected, onGPSTimeOut);
        } else {
            Toast.makeText(getActivity(), "Google Play Service Error " + resp, Toast.LENGTH_LONG).show();
        }
    }


    OnGPSWhenClose onGPSWhenClose = new OnGPSWhenClose() {
        @Override
        public void OnGPSWhenClose() {

            Dialog.Builder DialogBuilder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {

                @Override
                public void onPositiveActionClicked(DialogFragment fragment) {
                    super.onPositiveActionClicked(fragment);
                    fragment.dismiss();
                    getActivity().startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), gpsTracker.getGPSIntentRequestCode());
                }

                @Override
                public void onNegativeActionClicked(DialogFragment fragment) {
                    super.onNegativeActionClicked(fragment);
                    fragment.dismiss();
                    Tools tools = new Tools(context);
                    String gecmisSonuclar = tools.getSharedPreference("CafeWithCampaignResp", "");

                    if (gecmisSonuclar.equals("")) {
                        new ErrorFragmentHelper(context, null, "Üzgünüm, \n mekanları görebilmek için \n konum bilgine  \n ihtiyacım var").show();
                    } else {
                        showSelectScreen();
                    }
                }
            };

            ((SimpleDialog.Builder) DialogBuilder).message("Uygulamayı kullanabilmek için konum bilgisini açmanız gerekir. \n \n Açmak İster misiniz ? ")
                    .title("Konum bilgisi kapalı")
                    .positiveAction("Ayarlara Git")
                    .negativeAction("İptal");

            DialogFragment dialogFragment = DialogFragment.newInstance(DialogBuilder);
            dialogFragment.setCancelable(false);
            dialogFragment.show(context.getSupportFragmentManager(), "DialogBuilder");

        }
    };

    OnGPSConnected onGPSConnected = new OnGPSConnected() {
        @Override
        public void OnGPSConnected(Location location) {
            Globals.myLocation = location;
            showSelectScreen();
        }
    };

    OnGPSTimeOut onGPSTimeOut = new OnGPSTimeOut() {
        @Override
        public void OnGPSTimeOut() {
            new ErrorFragmentHelper(context, R.string.fa_globe, "Lokasyon Bilginiz Alınamadı").show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Don't forget to check requestCode before continuing your job
        if (requestCode == gpsTracker.getGPSIntentRequestCode()) {
            gpsTracker.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showSelectScreen() {
        new FragmentChanger(context, new SelectServiceType(), null, false, DetectLocation.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Globals.myLocation != null) {
            try {
                showSelectScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
