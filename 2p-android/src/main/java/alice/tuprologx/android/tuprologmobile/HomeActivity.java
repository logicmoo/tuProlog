package alice.tuprologx.android.tuprologmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import alice.util.VersionInfo;

public class HomeActivity extends  BaseActivity {

    private static HomeActivity context;

    private ImageButton bLibrary, bHistory, bSettings, bWeb, arrowTheory, arrowQuery, infoTheory, infoQuery;
    private TextView tvTheory, tvQuery, tvVersion, tvTheorySelected;
    private static final int ACTIVITY_LIBRARY = 3;
    private static final int THEORY_SELECT = 2;

    private File dexOutputDir ;

    private TheoryDbAdapter mDbHelper;


    public HomeActivity () {
        context=this;
    }
    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_home, frameLayout);

        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        mDbHelper = new TheoryDbAdapter(this);
        mDbHelper.open();

        Typeface font = Typeface.createFromAsset(getAssets(), "calibrii.ttf");
        tvTheory=(TextView) findViewById(R.id.tvTheory);
        tvTheory.setTypeface(font);
        tvQuery=(TextView) findViewById(R.id.tvQuery);
        tvQuery.setTypeface(font);
        tvTheorySelected = (TextView) findViewById(R.id.tvTheorySelected);

        bLibrary = (ImageButton) findViewById(R.id.libraryButton);
        bHistory = (ImageButton) findViewById(R.id.historyButton);
        bSettings = (ImageButton) findViewById(R.id.settingsButton);
        bWeb = (ImageButton) findViewById(R.id.webButton);
        arrowTheory = (ImageButton) findViewById(R.id.arrowTheory);
        arrowQuery = (ImageButton) findViewById(R.id.arrowQuery);
        infoTheory = (ImageButton) findViewById(R.id.infoTheory);
        infoQuery = (ImageButton) findViewById(R.id.infoQuery);

        tvVersion = (TextView) findViewById(R.id.tvVersion);

        try {
            tvVersion.setText(""
                    + "- tuPrologMobile for Android - \n " +
                    "Version: "
                    + HomeActivity.getContext().getPackageManager().getPackageInfo(HomeActivity.getContext().getPackageName(), 0).versionName
                    + "\nCore: " + VersionInfo.getEngineVersion()
                    + "\n\nhttp://tuprolog.alice.unibo.it");

        }catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        registOnClickListener();

        if(getIntent().getBooleanExtra("finishApplication", false)){
            closeApplication();
        }
        if(getIntent().getBooleanExtra("setTheory", false)) {
            Toast.makeText(context, "Select a theory first! ",
                    Toast.LENGTH_SHORT).show();
        }

        CUIConsole.main2();

        dexOutputDir = this.getApplicationContext().getDir("dex", 0);
        CUIConsole.libraryManager.setOptimizedDirectory(dexOutputDir.getAbsolutePath());
        CUIConsole.engine.getLibraryManager().setOptimizedDirectory(dexOutputDir.getAbsolutePath());
    }

    private void registOnClickListener(){

        bLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent libraryIntent = new Intent(HomeActivity.this, LibraryManagerActivity.class);
                startActivity(libraryIntent);

            }
        });

        bHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent historyIntent = new Intent(HomeActivity.this, HistoryActivity.class);
                startActivity(historyIntent);

            }
        });

        bSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent settingIntent = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settingIntent);
            }
        });

        bWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://tuprolog.alice.unibo.it"));
                startActivity(webIntent);

            }
        });

        arrowTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HomeActivity.this , TheoriesDatabaseActivity.class);
                startActivityForResult(i, THEORY_SELECT);

            }
        });

        arrowQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(theorySelect){

                    Intent libraryIntent = new Intent(HomeActivity.this, tuPrologActivity.class);
                    libraryIntent.putExtra("theory", t);
                    libraryIntent.putExtra("nameTheory", nameTheory);
                    startActivity(libraryIntent);
                } else {
                    Toast.makeText(context, "Select a theory first! ",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });

        infoTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);

                alert.setTitle("Info Theory");
                alert.setMessage("In this section you can customize the mobile engine" + "\n\n" +
                                    "Example Prolog Theory:\n" +
                                        "man(Andrea).\n" +
                                        "man(Marco).\n" +
                                        "woman(Elisa).\n" +
                                        "parent(Andrea, Marco).\n" +
                                        "parent(Elisa, Marco). \n" +
                                        "father(F,C) :- man(F), parent(F,C).\n" +
                                        "mother(M,C) :- woman(M), parent(M,C).\n" );

                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });

        infoQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);

                alert.setTitle("Info Query");
                alert.setMessage("In this section you can solve queries" + "\n\n" +
                        "Example Prolog Query:\n" +
                        "?- father(F, Marco) \n" +
                        "?- mother(M, Marco) \n");

                alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            }
        });



    }

    @Override
    public void onBackPressed() {

        closeApplication();
    }

    private Bundle extras = null;
    private boolean theorySelect = false;
    private Theory t;
    private String nameTheory = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            extras = intent.getExtras();
            theorySelect = true;
            setBoolTheory();
            switch (requestCode) {
                case THEORY_SELECT:
                    Long id = extras.getLong(TheoryDbAdapter.KEY_ROWID);
                    if (id != null) {
                        Cursor theoryCursor = mDbHelper.fetchTheory(id);
                        startManagingCursor(theoryCursor);
                        Theory oldTheory = CUIConsole.engine.getTheory();

                        try {
//                            Theory t;
                            t = new Theory(theoryCursor.getString(theoryCursor
                                    .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY))
                                    + System.getProperty("line.separator"));
                            CUIConsole.engine.setTheory(t);
                            nameTheory = theoryCursor.getString(theoryCursor.getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE));
                            tvTheorySelected.setText("Selected Theory : "
                                    + nameTheory );

                            Toast.makeText(
                                    context,
                                    "Theory selected: "
                                            + theoryCursor.getString(theoryCursor
                                            .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE)),
                                    Toast.LENGTH_SHORT).show();
                        } catch (InvalidTheoryException e) {
                            Toast.makeText(context, "Invalid Theory! " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            CUIConsole.engine.clearTheory();
                            try {
                                CUIConsole.engine.setTheory(oldTheory);
                            } catch (InvalidTheoryException e1) {
                                e1.printStackTrace();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private void closeApplication()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        HomeActivity.this.finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
