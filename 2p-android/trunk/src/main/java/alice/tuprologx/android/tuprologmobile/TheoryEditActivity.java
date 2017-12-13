package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TheoryEditActivity extends Activity {

  private final static String MY_PREFERENCES = "MyPref";
  private EditText mTitleText;
  private EditText mBodyText;
  private Long mRowId;
  private TheoryDbAdapter mDbHelper;
  private TheoryHistoryDbAdapter mHDbHelper;
  private SharedPreferences prefs;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    mDbHelper = new TheoryDbAdapter(this);
    mDbHelper.open();
    mHDbHelper = new TheoryHistoryDbAdapter(this);
    mHDbHelper.open();

    setContentView(R.layout.theory_edit);

    prefs = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

    mTitleText = (EditText) findViewById(R.id.title);
    mBodyText = (EditText) findViewById(R.id.body);

    Button confirmButton = (Button) findViewById(R.id.confirm);

    mRowId = (savedInstanceState == null) ? null : (Long) savedInstanceState
        .getSerializable(TheoryDbAdapter.KEY_ROWID);
    if (mRowId == null) {
      Bundle extras = getIntent().getExtras();
      mRowId = extras != null ? extras.getLong(TheoryDbAdapter.KEY_ROWID)
          : null;
    }

    populateFields();

    mTitleText.setSelection(mTitleText.getText().length());

    confirmButton.setOnClickListener(new View.OnClickListener() {

      public void onClick(View view) {
        setResult(RESULT_OK);
        saveState();
        mDbHelper.close();
        mHDbHelper.close();
        finish();
      }

    });
  }

  private void populateFields() {
    if (mRowId != null) {
      Cursor theory = mDbHelper.fetchTheory(mRowId);
      startManagingCursor(theory);
      mTitleText.setText(theory.getString(theory
          .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE)));
      mBodyText.setText(theory.getString(theory
          .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY)));
    }
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
    mTitleText.setSelection(mTitleText.getText().length());
  }

  private void saveState() {
    String title = mTitleText.getText().toString();
    String body = mBodyText.getText().toString();

    if (mRowId == null && !title.equals("")) {
      long id = mDbHelper.createTheory(title, body);
      if(prefs.getBoolean("swSaveT",false)==true)
        mHDbHelper.createTheory(title, body);
      if (id > 0) {
        mRowId = id;
      }
    } else if (mRowId != null) {
      mDbHelper.updateTheory(mRowId, title, body);
      if(prefs.getBoolean("swSaveT",false)==true)
        mHDbHelper.createTheory(title, body);
    } else if (title.equals("")) {
      Toast.makeText(getApplicationContext(), "Invalid Title",
          Toast.LENGTH_SHORT).show();
    }
  }
}