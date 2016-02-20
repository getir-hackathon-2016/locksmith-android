package te.com.locksmith.tools;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by enes on 20/02/16.
 */
public class Tools {
    private static Context context;
    private static Tools tools = new Tools(context);
    ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    boolean connected = false;

    public Tools(Context ctx) {
        this.context = ctx;
    }


    public static Tools getInstance(Context ctx) {
        context = ctx;
        return tools;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
        }
        return connected;
    }

    //SharedPreference Getter
    public String getSharedPreference(String key, String def) {
        SharedPreferences mSharedPrefs = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE);
        return mSharedPrefs.getString(key, def);
    }

    public Boolean getSharedPreference(String key, Boolean def) {
        SharedPreferences mSharedPrefs = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE);
        return mSharedPrefs.getBoolean(key, def);
    }

    public Integer getSharedPreference(String key, Integer def) {
        SharedPreferences mSharedPrefs = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE);
        return mSharedPrefs.getInt(key, def);
    }
    //SharedPreference Getter

    //SharedPreference Setter
    public boolean setSharedPreference(String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean setSharedPreference(String key, Boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean setSharedPreference(String key, Integer value) {
        SharedPreferences.Editor editor = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        return editor.commit();
    }
    //SharedPreference Setter

    //SharedPreference Remove
    public boolean removeSharedPreference(String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE).edit();
        editor.remove(key);
        return editor.commit();
    }
    //SharedPreference Remove

    //SharedPreference Clear
    public boolean clearSharedPreferences() {
        SharedPreferences.Editor editor = context.getSharedPreferences("xmlFile", Context.MODE_PRIVATE).edit();
        editor.clear();
        return editor.commit();
    }
    //SharedPreference Clear

    public String storeImage(Bitmap imageData) {
        //get path to external storage (SD card)
        Random r = new Random();
        String filename = Long.toString(System.currentTimeMillis()) + r.nextInt() + ".jpg";
        String iconsStoragePath = context.getExternalFilesDir(null) + "/Images/";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();

        String filePath = null;

        try {
            filePath = sdIconStorageDir.toString() + "/" + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);

            bos.flush();
            bos.close();


        } catch (FileNotFoundException e) {
            Log.w("----Tool----", "Error saving image file: " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.w("----Tool----", "Error saving image file: " + e.getMessage());
            return null;
        }

        return filePath;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public String ConvertJSONTimeToDate(String JSONTime, @Nullable String format) {

        if (format == null) {
            format = "dd.MM.yyyy";
        }

        long datetimestamp = Long.parseLong(JSONTime.replaceAll("\\D", ""));

        try {
            DateFormat sdf = new SimpleDateFormat(format);
            Date netDate = (new Date(datetimestamp));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }


    public String getDeviceID() {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidID;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidID = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        UUID deviceUid = new UUID(androidID.hashCode(), ((long) tmDevice.hashCode() << 32 | tmSerial.hashCode()));
        return deviceUid.toString();
    }
}
