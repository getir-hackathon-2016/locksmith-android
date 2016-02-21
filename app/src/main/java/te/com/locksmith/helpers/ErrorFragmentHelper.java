package te.com.locksmith.helpers;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import te.com.locksmith.R;
import te.com.locksmith.customComponets.TextAwesome;
import te.com.locksmith.fragments.ErrorFragment;
import te.com.locksmith.tools.FragmentChanger;

/**
 * Created by enes on 21/02/16.
 */
public class ErrorFragmentHelper {
    private FragmentActivity context;
    private String errorMessage;
    private int errorIconID;

    public ErrorFragmentHelper(FragmentActivity context,@Nullable Integer errorIconID, String errorMessage) {
        this.context = context;
        this.errorMessage = errorMessage;
        if(errorIconID == null) {
            this.errorIconID = R.string.fa_frown_o;
        }else{
            this.errorIconID = errorIconID;
        }
    }

    public void show() {
        Fragment fragment = new ErrorFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("errorMessage", errorMessage);
        mBundle.putInt("errorIconID", errorIconID);
        fragment.setArguments(mBundle);
        BackStackHelper.clearStack();
        new FragmentChanger(context, fragment, mBundle, false, null);
    }

    public View change(RelativeLayout relativeContainer, View HideElemet) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View errorView = inflater.inflate(R.layout.error_page, null);

        TextView errorText = (TextView) errorView.findViewById(R.id.errorText);
        errorText.setText(errorMessage);

        TextAwesome errorIcon = (TextAwesome) errorView.findViewById(R.id.errorIcon);
        errorIcon.setText(context.getResources().getString(errorIconID));

        HideElemet.setVisibility(View.GONE);

        relativeContainer.addView(errorView, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return errorView;

    }
}
