package te.com.locksmith.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

import te.com.locksmith.R;
import te.com.locksmith.fragments.DetectLocation;
import te.com.locksmith.fragments.HelpFragment;
import te.com.locksmith.fragments.SelectServiceType;
import te.com.locksmith.helpers.ActionBarHelper;
import te.com.locksmith.helpers.BackStackHelper;
import te.com.locksmith.tools.FragmentChanger;

/**
 * Created by enes on 20/02/16.
 */
public class LeftNavAdapter {
    Context context;
    ActionBarAdapter actionBarAdapter;
    Toolbar toolbar;
    boolean withFireOnInitialOnClick = true;

    public LeftNavAdapter(Context context, Toolbar toolbar, ActionBarAdapter actionBarAdapter, Boolean withFireOnInitialOnClick) {
        this.context = context;
        this.toolbar = toolbar;
        this.actionBarAdapter = actionBarAdapter;
        this.withFireOnInitialOnClick = withFireOnInitialOnClick;
    }

    public Drawer build() {
        Drawer result = new DrawerBuilder()
                .withActivity((Activity) context)
                .withHeader(R.layout.left_nav_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Çilingir çağır").withDescription("Kapıda kalma").withIcon(FontAwesome.Icon.faw_question).withIdentifier(1).withCheckable(true),
                        new PrimaryDrawerItem().withName("Yardım mı Lazım ?").withDescription("SSS, iletişim ve daha fazlası").withIcon(FontAwesome.Icon.faw_question).withIdentifier(2).withCheckable(true)
                        //new PrimaryDrawerItem().withName("Paylaş").withDescription("Paylaş ki yayılsın").withIcon(FontAwesome.Icon.faw_share_alt).withIdentifier(4).withCheckable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        switch (position) {
                            case 0:
                                ActionBarHelper.setTitle("Locksmith");
                                ActionBarHelper.hideRightButton();
                                BackStackHelper.clearStack();
                                new FragmentChanger((FragmentActivity) context, new DetectLocation(), null, false, null);
                            case 1:
                                ActionBarHelper.setTitle("Yardım");
                                ActionBarHelper.hideRightButton();
                                BackStackHelper.clearStack();
                                new FragmentChanger((FragmentActivity) context, new HelpFragment(), null, false, null);
                                break;
                        }

                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        KeyboardUtil.hideKeyboard((FragmentActivity) context);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withFireOnInitialOnClick(withFireOnInitialOnClick)
                .withShowDrawerOnFirstLaunch(false)
                .withTranslucentStatusBarShadow(true)
                .build();

        result.keyboardSupportEnabled((FragmentActivity) context, true);

        RightNavAdapter.setLeftNavDrawer(result);
        RightNavAdapter.setContext(context);

        return result;
    }
}
