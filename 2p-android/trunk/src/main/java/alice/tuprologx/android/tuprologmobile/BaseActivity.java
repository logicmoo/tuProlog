package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import java.util.ArrayList;

import alice.tuprologx.android.tuprologmobile.adapters.NavigationDrawerListAdapter;
import alice.tuprologx.android.tuprologmobile.models.Items;

public class BaseActivity extends Activity {


//    Frame layout: Which is going to be used as parent layout for child activity layout.
    protected FrameLayout frameLayout;

//    ListView to add navigation drawer item in it.
    protected ListView mDrawerList;
//    List item array for navigation drawer items.
    protected String[] listArray = {"tuProlog Mobile","Home", "Library", "Theory", "Query", "History", "Settings","Web", "Exit" };
    protected ArrayList<Items> items;

    protected static int position;
    private static boolean isLaunch = true;
    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer_base_layout);

        frameLayout = (FrameLayout)findViewById(R.id.content_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        items = new ArrayList<Items>();
        items.add(new Items("Home", R.drawable.logo_home));
        items.add(new Items("Library", R.drawable.logo_library));
        items.add(new Items("Theories", R.drawable.logo_theory));
        items.add(new Items("Query", R.drawable.logo_query));
        items.add(new Items("History", R.drawable.logo_history));
        items.add(new Items("Settings", R.drawable.logo_settings));
        items.add(new Items("Web", R.drawable.logo_web));
        items.add(new Items("Exit", R.drawable.logo_exit));

        View header = (View)getLayoutInflater().inflate(R.layout.list_view_header_layout, null);
        mDrawerList.addHeaderView(header);

        mDrawerList.setAdapter(new NavigationDrawerListAdapter(this, items));
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openActivity(position);
            }
        });

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions between the sliding drawer and the action bar app icon
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,      /* host Activity */
                mDrawerLayout,     /* DrawerLayout object */
                R.drawable.ic_menu_list2,     /* nav drawer image to replace 'Up' caret */
                R.string.open_drawer,       /* "open drawer" description for accessibility */
                R.string.close_drawer)      /* "close drawer" description for accessibility */
        {
            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(listArray[position]);
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(getString(R.string.app_name));
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);


        /**
         * As we are calling BaseActivity from manifest file and this base activity is intended just to add navigation drawer in our app.
         * We have to open some activity with layout on launch. So we are checking if this BaseActivity is called first time then we are opening our first activity.
         * */
        if(isLaunch){

            isLaunch = false;
            openActivity(0);
        }
    }



    protected void openActivity(int position) {

        mDrawerLayout.closeDrawer(mDrawerList);
        BaseActivity.position = position;

        switch (position) {
            case 0:
                break;
            case 1: //Home
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case 2: //Library
                startActivity(new Intent(this, LibraryManagerActivity.class));
                break;
            case 3: //Set a Theory
                startActivity(new Intent(this, TheoriesDatabaseActivity.class));
                break;
            case 4: //Solve a Query
                startActivity(new Intent(this, tuPrologActivity.class));
                break;
            case 5: //History
                startActivity(new Intent(this, HistoryActivity.class));
                break;
            case 6: //Settings
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case 7: //Web
                Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://tuprolog.alice.unibo.it"));
                startActivity(webIntent);
                break;
            case 8: //Exit
                Intent intent = new Intent(this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("finishApplication", true);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }


    /* We can override onBackPressed method to toggle navigation drawer*/
    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)){
            mDrawerLayout.closeDrawer(mDrawerList);
        }
//        else {
//            mDrawerLayout.openDrawer(mDrawerList);
//        }
    }
}
