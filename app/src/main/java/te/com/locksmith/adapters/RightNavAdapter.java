package te.com.locksmith.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;

import te.com.locksmith.R;

/**
 * Created by enes on 20/02/16.
 */
public class RightNavAdapter {
    private static Drawer leftNavDrawer = null;
    private static Context context = null;

    public static Drawer getLeftNavDrawer() {
        return leftNavDrawer;
    }

    public static void setLeftNavDrawer(Drawer leftNavDrawer) {
        RightNavAdapter.leftNavDrawer = leftNavDrawer;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        RightNavAdapter.context = context;
    }

    public static void buildEmty(){
        if(context == null){
            return;
        }
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new DrawerBuilder()
                .withActivity((FragmentActivity)context)
                .withCustomView(inflater.inflate(R.layout.right_nav_empty, null))
                .withDrawerGravity(Gravity.END)
                .append(leftNavDrawer);
    }
}
