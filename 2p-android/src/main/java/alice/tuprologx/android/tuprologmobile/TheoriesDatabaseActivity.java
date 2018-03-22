package alice.tuprologx.android.tuprologmobile;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import alice.tuprolog.Theory;

public class TheoriesDatabaseActivity extends BaseActivity {
  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_DESCRIPTION = 1;
  private static final int ACTIVITY_IMPORT = 2;

  private static final int SELECT_ID = Menu.FIRST + 1;
  private static final int DELETE_ID = Menu.FIRST + 2;


  private final static String MY_PREFERENCES = "MyPref";
  private final static String PATH_DATA_KEY = "pathData";

  private TheoryDbAdapter mDbHelper;
  private TheoryHistoryDbAdapter mHDbHelper;
  private File path;
  private SharedPreferences prefs;
  private ListView theoriesListView;
  private TextView tvEmpty;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getLayoutInflater().inflate(R.layout.theories_list, frameLayout);

    mDrawerList.setItemChecked(position, true);
    setTitle(listArray[position]);

    mDbHelper = new TheoryDbAdapter(this);
    mDbHelper.open();
    mHDbHelper = new TheoryHistoryDbAdapter(this);
    mHDbHelper.open();
    prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
    theoriesListView = (ListView) findViewById(R.id.theoriesListD);
    tvEmpty = (TextView) findViewById(R.id.tvEmpty);
    fillData();
    registerForContextMenu(theoriesListView);
    updatePreferencesData();

    theoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(TheoriesDatabaseActivity.this, TheoryDescriptionActivity.class);
        i.putExtra(TheoryDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_DESCRIPTION);
      }
    });
  }

  private void fillData() {
    Cursor notesCursor = mDbHelper.fetchAllTheories();
    startManagingCursor(notesCursor);

    String[] from = new String[] { TheoryDbAdapter.KEY_TITLE };

    int[] to = new int[] { R.id.text1 };

    SimpleCursorAdapter theories = new SimpleCursorAdapter(this,
        R.layout.theories_row, notesCursor, from, to);

    if(from.length==0)
      tvEmpty.setVisibility(View.VISIBLE);
    else
      theoriesListView.setAdapter(theories);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.list_page_menu, menu);
    return true;
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item) {
    switch (item.getItemId()) {
    case R.id.new_theory:
      createTheory();
      return true;
    case R.id.import_theory:
      Intent i = new Intent(this, TheoryImportActivity.class);
      startActivityForResult(i, ACTIVITY_IMPORT);
      return true;

    }

    return super.onMenuItemSelected(featureId, item);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, SELECT_ID, 0, R.string.menu_select);

  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
      case SELECT_ID:
        Bundle bundle = new Bundle();
        bundle.putLong(TheoryDbAdapter.KEY_ROWID, info.id);

        Intent mIntent = new Intent();
        mIntent.putExtras(bundle);
        setResult(RESULT_OK, mIntent);
        mDbHelper.close();
        mHDbHelper.close();
        finish();

        return true;

    }
    return super.onContextItemSelected(item);
  }



  private void createTheory() {
    Intent i = new Intent(this, TheoryEditActivity.class);
    startActivityForResult(i, ACTIVITY_CREATE);
  }

  @Override
  public void onBackPressed() {

    TheoriesDatabaseActivity.this.finish();
  }



  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (resultCode == RESULT_OK) {
      switch (requestCode) {
      case ACTIVITY_IMPORT:
        Bundle extras = intent.getExtras();
        String nomeFile = extras.getString("nomeFile");
        Theory t = null;
        try {
          nomeFile = nomeFile.toString();
          t = new Theory(new FileInputStream(nomeFile));
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        String st[] = nomeFile.split("/");
        mDbHelper.createTheory(st[st.length - 1], t.toString());
        if(prefs.getBoolean("swSaveT",false)==true)
          mHDbHelper.createTheory(st[st.length - 1], t.toString());
        break;
      }
    }
    fillData();
  }


  private void updatePreferencesData() {

    String s = prefs.getString(PATH_DATA_KEY, Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .getAbsolutePath());
    path = new File(s);
  }

}
