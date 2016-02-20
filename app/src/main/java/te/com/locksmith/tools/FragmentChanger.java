package te.com.locksmith.tools;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.desarrollodroide.libraryfragmenttransactionextended.FragmentTransactionExtended;

import te.com.locksmith.R;
import te.com.locksmith.constants.ActiveFragmentConservative;
import te.com.locksmith.helpers.BackStackHelper;

/**
 * Created by enes on 20/02/16.
 */
public class FragmentChanger {
    Fragment fragment;
    Class whatIsYourClass;
    Bundle bundle;
    FragmentActivity context;

    public FragmentChanger(FragmentActivity context, Fragment fragmentClass, @Nullable Bundle bundle, boolean addActiveFragmentToBackStack,@Nullable Class whatIsYourClass) {
        this.context = context;
        this.fragment = fragmentClass;
        this.whatIsYourClass = whatIsYourClass;
        if (bundle == null) {
            this.bundle = new Bundle();
        } else {
            this.bundle = bundle;
        }
        if (addActiveFragmentToBackStack) {
            BackStackHelper.push(ActiveFragmentConservative.fragment.getClass().getName(), ActiveFragmentConservative.fragment);
        }
        this.change();
    }

    private void change() {
        if (fragment != null) {
            if(whatIsYourClass == null || ActiveFragmentConservative.fragment == null || ActiveFragmentConservative.fragment.getClass() == whatIsYourClass) {

                FragmentManager fm = context.getFragmentManager();
                final FragmentTransaction fragmentTransaction = fm.beginTransaction();
                final FragmentTransactionExtended fragmentTransactionExtended = new FragmentTransactionExtended(context, fragmentTransaction, null, fragment, R.id.fragment_container);
                fragmentTransactionExtended.addTransition(FragmentTransactionExtended.SLIDE_VERTICAL_PUSH_LEFT);
                fragmentTransactionExtended.commit();

                ActiveFragmentConservative.fragment = fragment;
            }
        }
    }
}
