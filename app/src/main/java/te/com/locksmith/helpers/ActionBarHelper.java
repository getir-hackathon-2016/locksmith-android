package te.com.locksmith.helpers;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import te.com.locksmith.adapters.ActionBarAdapter;
import te.com.locksmith.customComponets.ButtonAwesome;

/**
 * Created by enes on 20/02/16.
 */
public class ActionBarHelper {
    private static ActionBarAdapter actionBarAdapter = null;
    private static TextView textView = null;
    private static ButtonAwesome rightButton = null;
    private static Context context = null;


    public static ActionBarAdapter getActionBarAdapter() {
        return actionBarAdapter;
    }

    public static void setActionBarAdapter(ActionBarAdapter actionBarAdapter) {
        ActionBarHelper.actionBarAdapter = actionBarAdapter;
    }

    public static ButtonAwesome getRightButton() {
        return rightButton;
    }

    public static void setRightButton(ButtonAwesome rightButton) {
        ActionBarHelper.rightButton = rightButton;
    }

    public static TextView getTextView() {
        return textView;
    }

    public static void setTextView(TextView testView) {
        ActionBarHelper.textView = testView;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        ActionBarHelper.context = context;
    }

    public static void setTitle(String title){
        if(actionBarAdapter==null){
            return;
        }
        textView.setText(title);
    }

    public static void setRightButtonIcon(int favIcon){
        if(rightButton==null){
            return;
        }
        rightButton.setText(context.getResources().getString(favIcon));
        showRightButton();
    }

    public static void showRightButton(){
        if(rightButton==null){
            return;
        }
        rightButton.setVisibility(View.VISIBLE);
    }

    public static void hideRightButton(){
        if(rightButton==null){
            return;
        }
        rightButton.setVisibility(View.GONE);
    }

    public static void setRightButtonOnClickListener(View.OnClickListener onClickListener){
        if(rightButton==null){
            return;
        }
        rightButton.setOnClickListener(onClickListener);
    }
}
