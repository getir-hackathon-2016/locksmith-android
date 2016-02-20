package te.com.locksmith.interfaces;

/**
 * Created by enes on 20/02/16.
 */
public interface OnTaskFailure {
    void onTaskFailure(int statusCode, String result);
}
