package te.com.locksmith.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import te.com.locksmith.R;
import te.com.locksmith.customComponets.CustomFragment;
import te.com.locksmith.customComponets.TextAwesome;
import te.com.locksmith.helpers.ActionBarHelper;

/**
 * Created by ucenmi on 28.06.2015.
 */
public class ErrorFragment extends CustomFragment {

    private String errorMessage;
    private TextView errorText;
    private TextAwesome errorIcon;
    private int errorIconID;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.errorMessage = this.getArguments().getString("errorMessage", "");
        this.errorIconID = this.getArguments().getInt("errorIconID", 1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.error_page, null);
        errorText = (TextView)v.findViewById(R.id.errorText);
        errorIcon = (TextAwesome)v.findViewById(R.id.errorIcon);
        if(!errorMessage.equals("")) {
            errorText.setText(errorMessage);
        }

        errorIcon.setText(context.getResources().getString(errorIconID));

        ActionBarHelper.setTitle("Hata");
        ActionBarHelper.hideRightButton();
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}