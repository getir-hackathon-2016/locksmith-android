package te.com.locksmith.tools;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import te.com.locksmith.constants.Globals;
import te.com.locksmith.interfaces.OnTaskFailure;
import te.com.locksmith.interfaces.OnTaskFinish;
import te.com.locksmith.interfaces.OnTaskStart;
import te.com.locksmith.interfaces.OnTaskSuccess;

/**
 * Created by enes on 20/02/16.
 */
public class Api {
    private Context context;
    private Map<String, String> HeaderParameters;
    private JsonObject Parameters;
    private AsyncHttpClient AsyncClient;
    private SyncHttpClient SyncClient;
    private HttpEntity httpEntity;

    private int retryTime = 0;
    private boolean tokenRequired = true;
    private OnTaskStart onTaskStartListener = null;
    private OnTaskSuccess onTaskSuccessListener = null;
    private OnTaskFailure onTaskFailureListener = null;
    private OnTaskFinish onTaskFinishListener = null;
    private String url;
    private boolean showDialog = false;
    private ProgressDialog dialog;
    private boolean Async = true;

    public Api(Context context) {

        this.context = context;
        this.HeaderParameters = new HashMap<String, String>();
        this.Parameters = new JsonObject();
        this.AsyncClient = new AsyncHttpClient();
        this.SyncClient = new SyncHttpClient();
    }

    public boolean isAsync() {
        return Async;
    }

    public void setAsync(boolean async) {
        Async = async;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public void setOnTaskStart(OnTaskStart onTaskStartListener) {
        this.onTaskStartListener = onTaskStartListener;
    }

    public void setOnTaskFailure(OnTaskFailure onTaskFailureListener) {
        this.onTaskFailureListener = onTaskFailureListener;
    }

    public void setOnTaskFinish(OnTaskFinish onTaskFinishListener) {
        this.onTaskFinishListener = onTaskFinishListener;
    }

    public void addHeaderParameter(String Key, String Value) {
        this.HeaderParameters.put(Key, Value);
    }

    public void addParameter(String Key, String Value) {
        this.Parameters.addProperty(Key, Value);
    }

    public void addParameter(String Key, int Value) {
        this.Parameters.addProperty(Key, Value);
    }

    private void buildHttpEntity() {

        String jsonParameter = Parameters.toString();
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(jsonParameter.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        httpEntity = entity;
    }

    private void buildClient() {

        for (Map.Entry<String, String> entry : HeaderParameters.entrySet()) {
            AsyncClient.addHeader(entry.getKey().toString(), entry.getValue().toString());
            SyncClient.addHeader(entry.getKey().toString(), entry.getValue().toString());
        }

        AsyncClient.setTimeout(20000);
        SyncClient.setTimeout(2000);
    }

    private void buildAll(String url, OnTaskSuccess onTaskSuccessListener) {
        this.url = url;
        this.onTaskSuccessListener = onTaskSuccessListener;
        buildHttpEntity();
        buildClient();
    }

    private void post() {
        post(url, onTaskSuccessListener);
    }

    public void post(String urlLocal, final OnTaskSuccess onTaskSuccessListenerLocal) {
        if (retryTime == 0) {
            buildAll(urlLocal, onTaskSuccessListenerLocal);
            if (showDialog) {
                dialog = new ProgressDialog(context);
                dialog.setCancelable(false);
                dialog.setMessage("Yükleniyor");
                dialog.show();
            }
        }

        if (Async) {
            AsyncClient.post(context, Globals.server + url, httpEntity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    super.onStart();
                    if (retryTime == 0 && onTaskStartListener != null) {
                        onTaskStartListener.onTaskStart();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody);
                    Logger.d("retryTime : " + retryTime + " statusCode : " + statusCode);
                    Logger.json(response);
                    if (responseBody != null && onTaskSuccessListener != null) {
                        onTaskSuccessListener.onTaskSuccess(statusCode, new String(responseBody));
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    if (responseBody != null) {
                        String response = new String(responseBody);
                        Logger.e("retryTime : " + retryTime + " statusCode : " + statusCode);

                        try {
                            JSONObject jObj = new JSONObject(response);
                            Logger.json(jObj.getString("responseDesc"));
                        } catch (JSONException e) {
                            Logger.e(e, e.toString());
                        }
                    }

                    ServerResponseMessage.getInstance(context).execute(statusCode, responseBody);

                    if (onTaskFailureListener != null) {
                        String result;
                        if (responseBody == null) {
                            result = "";
                        } else {
                            result = new String(responseBody);
                        }
                        onTaskFailureListener.onTaskFailure(statusCode, result);
                    }
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (dialog != null && dialog.isShowing() && showDialog) {
                        dialog.dismiss();
                    }
                    if (onTaskFinishListener != null) {
                        onTaskFinishListener.onTaskFinish();
                    }
                }
            });
        }
    }

}
