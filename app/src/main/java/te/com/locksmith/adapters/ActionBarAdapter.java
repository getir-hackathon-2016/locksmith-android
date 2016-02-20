package te.com.locksmith.adapters;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;

import te.com.locksmith.R;
import te.com.locksmith.customComponets.ButtonAwesome;
import te.com.locksmith.helpers.ActionBarHelper;

/**
 * Created by enes on 20/02/16.
 */
public class ActionBarAdapter {
    private static ActionBar actionBar;
    private static Context context;
    private static LayoutInflater inflater;
    private static TextView textView;
    private static ButtonAwesome rightButton;

    public ActionBarAdapter(Context context,ActionBar actionBar) {
        this.actionBar = actionBar;
        this.context = context;
        inflater = LayoutInflater.from(context);
        build();

    }

    private void build(){
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = inflater.inflate(R.layout.custom_actionbar, null);

        textView = (TextView) mCustomView.findViewById(R.id.actionBarTitle);

        rightButton = (ButtonAwesome) mCustomView.findViewById(R.id.actionBarRightButton);
        rightButton.setVisibility(View.GONE);

        int color = context.getResources().getColor(R.color.accent);

        View v = MaterialRippleLayout.on(rightButton)
                .rippleOverlay(true)
                .rippleAlpha(0.2f)
                .rippleColor(color)
                .rippleHover(false)
                .create();

        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);

        ActionBarHelper.setActionBarAdapter(this);
        ActionBarHelper.setRightButton(this.rightButton);
        ActionBarHelper.setTextView(this.textView);
        ActionBarHelper.setContext(context);
    }
}
