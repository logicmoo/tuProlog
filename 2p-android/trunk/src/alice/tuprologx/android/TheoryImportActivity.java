package alice.tuprologx.android;

import alice.tuprologx.android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TheoryImportActivity extends ListActivity {

  private final static String MY_PREFERENCES = "MyPref";
  private final static String PATH_DATA_KEY = "lastData";

  private enum DISPLAYMODE {
    ABSOLUTE, RELATIVE;
  }

  private final DISPLAYMODE displayMode = DISPLAYMODE.ABSOLUTE;
  private List<String> directoryEntries = new ArrayList<String>();
  private File currentDirectory = new File("/");

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    browseToPref();
  }

  private void browseToPref() {
    SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
        Context.MODE_PRIVATE);
    String s = prefs.getString(PATH_DATA_KEY, "/");
    browseTo(new File(s));
  }

  private void upOneLevel() {
    if (this.currentDirectory.getParent() != null)
      this.browseTo(this.currentDirectory.getParentFile());
  }

  private void browseTo(final File aDirectory) {
    if (aDirectory.isDirectory()) {
      this.currentDirectory = aDirectory;
      fill(aDirectory.listFiles());
    } else {
      Bundle bundle = new Bundle();
      String st = aDirectory.getAbsolutePath();

      saveLastData(aDirectory.getParent());

      bundle.putString("nomeFile", st);
      Intent mIntent = new Intent();
      mIntent.putExtras(bundle);
      setResult(RESULT_OK, mIntent);
      finish();
    }
  }

  private void fill(File[] files) {
    this.directoryEntries.clear();

    try {
      Thread.sleep(10);
    } catch (InterruptedException e1) {
      e1.printStackTrace();
    }
    this.directoryEntries.add(".");

    if (this.currentDirectory.getParent() != null)
      this.directoryEntries.add("..");

    switch (this.displayMode) {
    case ABSOLUTE:
      for (File file : files) {
        if (file.canRead()) {
          if (file.getAbsolutePath().endsWith(".pl")
              || file.getAbsolutePath().endsWith(".txt")
              || file.getAbsolutePath().endsWith(".doc") || file.isDirectory()) {
            this.directoryEntries.add(file.getPath());
          }

        }
      }
      break;
    case RELATIVE:
      int currentPathStringLenght = this.currentDirectory.getAbsolutePath()
          .length();
      for (File file : files) {
        this.directoryEntries.add(file.getAbsolutePath().substring(
            currentPathStringLenght));
      }
      break;
    }

    ArrayAdapter<String> directoryList = new ArrayAdapter<String>(this,
        R.layout.file_row, this.directoryEntries);

    this.setListAdapter(directoryList);
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    String selectedFileString = this.directoryEntries.get(position);
    if (selectedFileString.equals(".")) {
      this.browseTo(this.currentDirectory);
    } else if (selectedFileString.equals("..")) {
      this.upOneLevel();
    } else {
      File clickedFile = null;
      switch (this.displayMode) {
      case RELATIVE:
        clickedFile = new File(this.currentDirectory.getAbsolutePath()
            + this.directoryEntries.get(position));
        break;
      case ABSOLUTE:
        clickedFile = new File(this.directoryEntries.get(position));
        break;
      }
      if (clickedFile != null)
        this.browseTo(clickedFile);
    }
  }

  private void saveLastData(String s) {
    SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES,
        Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    if (s != null) {
      editor.putString(PATH_DATA_KEY, s);
      editor.commit();
    }
  }
}
