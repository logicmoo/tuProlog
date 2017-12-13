package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.ShareActionProvider;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DescriptionActivity extends Activity {

    private static final int ACTIVITY_EDIT = 1;
    private final static String MY_PREFERENCES = "MyPref";
    private final static String PATH_DATA_KEY = "pathData";
    private static final String ACTIVITY = "activity";


    private EditText etTitle, etBody;
    private Long mRowId;
    private TheoryHistoryDbAdapter mHtDbHelper;
    private QueriesHistoryDbAdapter mHqDbHelper;
    private File path;
    private String shareLabel = "Nessuna selezione";
    private String activityLabel = "Nessuna selezione";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        mHtDbHelper = new TheoryHistoryDbAdapter(this);
        mHtDbHelper.open();
        mHqDbHelper = new QueriesHistoryDbAdapter(this);
        mHqDbHelper.open();

        prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        activityLabel = extras.getString(ACTIVITY);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);

        if(activityLabel.equals("theory")) {
            mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                    .getSerializable(TheoryHistoryDbAdapter.KEY_ROWID);
            if (mRowId == null) {
                mRowId = extras != null ? extras.getLong(TheoryHistoryDbAdapter.KEY_ROWID)
                        : null;
            }
        }
        else if (activityLabel.equals("query"))
        {
            mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                    .getSerializable(QueriesHistoryDbAdapter.KEY_ROWID);
            if (mRowId == null) {
                mRowId = extras != null ? extras.getLong(QueriesHistoryDbAdapter.KEY_ROWID)
                        : null;
            }
        }

        populateFields();

        //Menu Overflow in the upper right
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }


    }

    private void populateFields() {
        if (mRowId != null) {
            if(activityLabel.equals("theory"))
            {
                Cursor theory = mHtDbHelper.fetchTheory(mRowId);
                startManagingCursor(theory);
                etTitle.setText(theory.getString(theory
                        .getColumnIndexOrThrow(TheoryHistoryDbAdapter.KEY_TITLE)));
                etBody.setText(theory.getString(theory
                        .getColumnIndexOrThrow(TheoryHistoryDbAdapter.KEY_BODY)));
                shareLabel= "TITLE: " + theory.getString(theory.getColumnIndexOrThrow(TheoryHistoryDbAdapter.KEY_TITLE))
                        + " BODY: " + theory.getString(theory.getColumnIndexOrThrow(TheoryHistoryDbAdapter.KEY_BODY));
            }
            else if(activityLabel.equals("query"))
            {
                Cursor theory = mHqDbHelper.fetchQuery(mRowId);
                startManagingCursor(theory);
                etTitle.setText(theory.getString(theory
                        .getColumnIndexOrThrow(QueriesHistoryDbAdapter.KEY_TITLE)));
                etBody.setText(theory.getString(theory
                        .getColumnIndexOrThrow(QueriesHistoryDbAdapter.KEY_BODY)));
                shareLabel= "TITLE: " + theory.getString(theory.getColumnIndexOrThrow(QueriesHistoryDbAdapter.KEY_TITLE))
                        + " BODY: " + theory.getString(theory.getColumnIndexOrThrow(QueriesHistoryDbAdapter.KEY_BODY));
            }

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_description, menu);

        MenuItem item = menu.findItem(R.id.share_theory);

        if(!prefs.getBoolean("swEnableShare", false)) {
            item.setVisible(false);
        }

        ShareActionProvider myShareActionProvider = (ShareActionProvider) item.getActionProvider();
        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_SEND);
        myIntent.putExtra(Intent.EXTRA_TEXT, shareLabel);
        myIntent.setType("text/plain");

        myShareActionProvider.setShareIntent(myIntent);


        return true;
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(activityLabel.equals("theory"))
        {
            outState.putSerializable(TheoryHistoryDbAdapter.KEY_ROWID, mRowId);
        }
        else if(activityLabel.equals("query"))
        {
            outState.putSerializable(QueriesHistoryDbAdapter.KEY_ROWID, mRowId);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
        invalidateOptionsMenu();
    }

    //Set menu icon
    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try{
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                }
                catch(NoSuchMethodException e){
                    Log.e("tag", "onMenuOpened", e);
                }
                catch(Exception e){
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }





}
