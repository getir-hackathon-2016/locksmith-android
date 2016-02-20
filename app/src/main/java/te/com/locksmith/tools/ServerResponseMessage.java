package te.com.locksmith.tools;

import android.app.Activity;
import android.content.Context;

import com.devspark.appmsg.AppMsg;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by enes on 20/02/16.
 */
public class ServerResponseMessage {
    private static Activity activity;
    private static Context context;
    private static ServerResponseMessage serverResponseMessage = new ServerResponseMessage();

    public static ServerResponseMessage getInstance(Context cxt) {
        context = cxt;
        try {
            activity = (Activity)cxt;
        }catch (Exception ex) {
            activity = null;
            Logger.e(ex, ex.toString());
        }
        return serverResponseMessage;
    }

    public void execute(int statusCode, byte[] responseBody){
        if (statusCode == 400 && activity != null){
            try {
                String json = new String(responseBody);
                JSONObject jObj = new JSONObject(json);
                AppMsg.cancelAll();
                AppMsg appMsg = AppMsg.makeText(activity,jObj.getString("responseDesc"),AppMsg.STYLE_ALERT);
                appMsg.setAnimation(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                appMsg.setDuration(4000);
                appMsg.show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
