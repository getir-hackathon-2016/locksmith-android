package te.com.locksmith.helpers;

import android.app.Fragment;

import java.util.List;

import te.com.locksmith.interfaces.OnBackPressed;

/**
 * Created by enes on 20/02/16.
 */
public class OnBackPressedHelper {

    public static List<Fragment> fragmentList;

    private static OnBackPressed onBackPressed = null;

    public static OnBackPressed getOnBackPressed() {
        return onBackPressed;
    }

    public static void setOnBackPressed(OnBackPressed onBackPressed) {
        OnBackPressedHelper.onBackPressed = onBackPressed;
    }
}
