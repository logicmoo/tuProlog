package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;

public class SettingsActivity extends BaseActivity {

    private final static String MY_PREFERENCES = "MyPref";
    private final static String PATH_DATA_KEY = "pathData";
    private final static String SAVE_THEORY = "swSaveT";
    private final static String SAVE_QUERY = "swSaveQ";
    private final static String ENABLE_SHARE = "swEnableShare";


    private Switch swTheories, swQueries, swShare;
    private Button btnDeleteT, btnDeleteQ, btnEditPath;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private File path;

    private TheoryHistoryDbAdapter mHtDbHelper;
    private QueriesHistoryDbAdapter mHqDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_settings, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        swTheories = (Switch) findViewById(R.id.switchTheories);
        swQueries = (Switch) findViewById(R.id.switchQueries);
        swShare = (Switch) findViewById(R.id.switchShare);
        btnDeleteT = (Button) findViewById(R.id.btnDeleteT);
        btnDeleteQ = (Button) findViewById(R.id.btnDeleteQ);
        btnEditPath = (Button) findViewById(R.id.btnEditPath);

        mHtDbHelper = new TheoryHistoryDbAdapter(this);
        mHtDbHelper.open();
        mHqDbHelper = new QueriesHistoryDbAdapter(this);
        mHqDbHelper.open();

        sharedPreferences = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        switchOnClick();
        buttonOnClick();


    }

    @Override
    protected void onResume() {
        super.onResume();

        String s = sharedPreferences.getString(PATH_DATA_KEY, Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath());
        path = new File(s);

        if (sharedPreferences.getBoolean(SAVE_THEORY, false))
            swTheories.setChecked(true);
        else
            swTheories.setChecked(false);

        if(sharedPreferences.getBoolean(SAVE_QUERY, false))
            swQueries.setChecked(true);
        else
            swQueries.setChecked(false);

        if(sharedPreferences.getBoolean(ENABLE_SHARE, false))
            swShare.setChecked(true);
        else
            swShare.setChecked(false);

    }

    private void switchOnClick ()
    {
        swTheories.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    editor.putBoolean(SAVE_THEORY, true);

                } else {

                    editor.putBoolean(SAVE_THEORY, false);
                }

                editor.commit();
            }
        });

        swQueries.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    editor.putBoolean(SAVE_QUERY, true);

                } else {

                    editor.putBoolean(SAVE_QUERY, false);
                }

                editor.commit();
            }
        });

        swShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {

                    editor.putBoolean(ENABLE_SHARE, true);

                } else {

                    editor.putBoolean(ENABLE_SHARE, false);
                }

                editor.commit();
            }
        });
    }

    private void buttonOnClick()
    {
        btnDeleteT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

                alert.setTitle("Delete Theories");
                alert.setMessage(R.string.delete_questionT);

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mHtDbHelper.deleteTheory(-1);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }
        });

        btnDeleteQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

                alert.setTitle("Delete Queries");
                alert.setMessage(R.string.delete_questionQ);

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mHqDbHelper.deleteQuery(-1);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            }
        });

        btnEditPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);

                alert.setTitle("Export Path");
                alert.setMessage("Edit the export path");

                final EditText input = new EditText(SettingsActivity.this);
                input.setText(path.getAbsolutePath());
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        try {
                            File newPath = new File(value);
                            if (newPath.exists() && newPath.isDirectory()) {
                                savePreferencesData(value);
                                Toast.makeText(getApplicationContext(),
                                        "New export path selected: " + path.getAbsolutePath(),
                                        Toast.LENGTH_SHORT).show();
                            } else if (!newPath.exists()) {
                                if (newPath.mkdirs()) {
                                    savePreferencesData(value);
                                    Toast.makeText(getApplicationContext(),
                                            "New export path selected: " + path.getAbsolutePath(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (!newPath.isDirectory()) {
                                Toast.makeText(getApplicationContext(), "Path not valid",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Path not valid",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                alert.show();

            }
        });

    }

    private void savePreferencesData(String s) {

        if (s != null) {
            editor.putString(PATH_DATA_KEY, s);
            editor.commit();
            path = new File(s);
        }
    }

    @Override
    public void onBackPressed() {

        SettingsActivity.this.finish();

    }



}
