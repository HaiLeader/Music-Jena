package vn.brine.haileader.exploratorysearch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import vn.brine.haileader.exploratorysearch.fragments.FavoriteFragment;
import vn.brine.haileader.exploratorysearch.fragments.FragmentDrawer;
import vn.brine.haileader.exploratorysearch.fragments.HomeFragment;
import vn.brine.haileader.exploratorysearch.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity
        implements FragmentDrawer.FragmentDrawerListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int HOME_FRAGMENT = 0;
    private static final int FAVORITE_FRAGMENT = 1;
    private static final int SETTING_FRAGMENT = 2;

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        displayView(HOME_FRAGMENT);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayView(int position){
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case HOME_FRAGMENT:
                fragment = new HomeFragment();
                title = getString(R.string.title_home);
                break;
            case FAVORITE_FRAGMENT:
                fragment = new FavoriteFragment();
                title = getString(R.string.title_favorites);
                break;
            case SETTING_FRAGMENT:
                fragment = new SettingFragment();
                title = getString(R.string.title_settings);
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            getSupportActionBar().setTitle(title);
        }
    }
}