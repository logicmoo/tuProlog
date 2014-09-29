package alice.tuprologx.android;

import alice.tuprologx.android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import alice.tuprolog.Theory;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class TheoriesDatabaseActivity extends ListActivity {
  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int ACTIVITY_IMPORT = 2;

  private static final int DELETE_ID = Menu.FIRST + 1;
  private static final int EDIT_ID = Menu.FIRST + 2;
  private static final int EXPORT_ID = Menu.FIRST + 3;

  private final static String MY_PREFERENCES = "MyPref";
  private final static String PATH_DATA_KEY = "pathData";

  private TheoryDbAdapter mDbHelper;
  private File path;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.theories_list);
    mDbHelper = new TheoryDbAdapter(this);
    mDbHelper.open();
    fillData();
    registerForContextMenu(getListView());
    updatePreferencesData();
  }

  private void fillData() {
    Cursor notesCursor = mDbHelper.fetchAllTheories();
    startManagingCursor(notesCursor);

    String[] from = new String[] { TheoryDbAdapter.KEY_TITLE };

    int[] to = new int[] { R.id.text1 };

    SimpleCursorAdapter theories = new SimpleCursorAdapter(this,
        R.layout.theories_row, notesCursor, from, to);
    setListAdapter(theories);
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
    case R.id.edit_path:
      AlertDialog.Builder alert = new AlertDialog.Builder(this);

      alert.setTitle("Export Path");
      alert.setMessage("Edit the export path");

      final EditText input = new EditText(this);
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

    return super.onMenuItemSelected(featureId, item);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    menu.add(0, EDIT_ID, 0, R.string.edit_theory);
    menu.add(0, EXPORT_ID, 0, R.string.export_theory);
  }

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    switch (item.getItemId()) {
    case DELETE_ID:
      mDbHelper.deleteTheory(info.id);
      fillData();
      return true;
    case EDIT_ID:
      Intent i = new Intent(this, TheoryEditActivity.class);
      i.putExtra(TheoryDbAdapter.KEY_ROWID, info.id);
      startActivityForResult(i, ACTIVITY_EDIT);
      return true;
    case EXPORT_ID:
      Cursor theoryCursor = mDbHelper.fetchTheory(info.id);
      startManagingCursor(theoryCursor);
      String title = theoryCursor.getString(theoryCursor
          .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE));
      if (title.endsWith(".pl") == false)
        title = title + ".pl";
      String body = theoryCursor.getString(theoryCursor
          .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY));

      exportTheory(title.trim(), body.trim());

      return true;
    }
    return super.onContextItemSelected(item);
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

        MediaScannerConnection.scanFile(this, new String[] { file.toString() },
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

  private void createTheory() {
    Intent i = new Intent(this, TheoryEditActivity.class);
    startActivityForResult(i, ACTIVITY_CREATE);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Bundle bundle = new Bundle();
    bundle.putLong(TheoryDbAdapter.KEY_ROWID, id);

    Intent mIntent = new Intent();
    mIntent.putExtras(bundle);
    setResult(RESULT_OK, mIntent);
    mDbHelper.close();
    finish();

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
        break;
      }
    }
    fillData();
  }

  private void savePreferencesData(String s) {
    SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
        Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    if (s != null) {
      editor.putString(PATH_DATA_KEY, s);
      editor.commit();
    }
    updatePreferencesData();
  }

  private void updatePreferencesData() {
    SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
        Context.MODE_PRIVATE);
    String s = prefs.getString(PATH_DATA_KEY, Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .getAbsolutePath());
    path = new File(s);
  }

}
