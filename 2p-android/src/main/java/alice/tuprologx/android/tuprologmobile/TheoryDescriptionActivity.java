package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TheoryDescriptionActivity extends Activity {

    private static final int ACTIVITY_EDIT = 1;
    private final static String MY_PREFERENCES = "MyPref";
    private final static String PATH_DATA_KEY = "pathData";

    private EditText etTitle, etBody;
    private Long mRowId;
    private TheoryDbAdapter mDbHelper;
    private File path;
    private String shareLabel = "Nessuna selezione";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        mDbHelper = new TheoryDbAdapter(this);
        mDbHelper.open();

        prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);

        mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
                .getSerializable(TheoryDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(TheoryDbAdapter.KEY_ROWID)
                    : null;
        }

        populateFields();
        updatePreferencesData();

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
            Cursor theory = mDbHelper.fetchTheory(mRowId);
            startManagingCursor(theory);
            etTitle.setText(theory.getString(theory
                    .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE)));
            etBody.setText(theory.getString(theory
                    .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY)));
            shareLabel= "TITLE: " + theory.getString(theory.getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE))
                    + " BODY: " + theory.getString(theory.getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_desc_theory, menu);

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
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {

            case R.id.edit_theory:
                Intent i = new Intent(this, TheoryEditActivity.class);
                i.putExtra(TheoryDbAdapter.KEY_ROWID, mRowId);
                startActivityForResult(i, ACTIVITY_EDIT);
                return true;

            case R.id.export_theory:
                Cursor theoryCursor = mDbHelper.fetchTheory(mRowId);
                  startManagingCursor(theoryCursor);
                  String title = theoryCursor.getString(theoryCursor
                      .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE));
                  if (title.endsWith(".pl") == false)
                    title = title + ".pl";
                  String body = theoryCursor.getString(theoryCursor
                      .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY));

                  exportTheory(title.trim(), body.trim());

                return true;

            case R.id.delete_theory:
                mDbHelper.deleteTheory(mRowId);
                finish();
                return true;


        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TheoryDbAdapter.KEY_ROWID, mRowId);
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

    private void exportTheory(String title, String body) {
        boolean writeable = isExternalStorageWriteable();

        if (writeable == true) {
            try {
                path.mkdirs();
                File file = new File(path, title);
                OutputStream os = new FileOutputStream(file);
                os.write(body.toString().getBytes());
                os.close();
                Toast.makeText(getApplicationContext(),
                        ("Exported to " + path.getAbsolutePath()), Toast.LENGTH_SHORT)
                        .show();

                MediaScannerConnection.scanFile(this, new String[]{file.toString()},
                        null, new MediaScannerConnection.OnScanCompletedListener() {

                            public void onScanCompleted(String d, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + d + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }

                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "External Storage not writeable",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return false;
        } else {
            return false;
        }
    }

    private void updatePreferencesData() {

        String s = prefs.getString(PATH_DATA_KEY, Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath());
        path = new File(s);
    }
}
