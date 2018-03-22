package alice.tuprologx.android.tuprologmobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;

public class tuPrologActivity extends BaseActivity {

  private TextView textView;
  private EditText titleText;
  private AutoCompleteTextView editText;
  private Button execute;
  private Button next;
  private Button clear;
  private TabHost tabHost;
  private TextView solutionView;
  private TextView outputView;
  private Toast toast;
  private static final int ACTIVITY_SELECT = 2;
  private static final int ACTIVITY_LIBRARY = 3;
  
  private TheoryDbAdapter mDbHelper;

  private static tuPrologActivity context;
  
  /**
   * @author Alessio Mercurio
   * 
   * Directory used by DexClassLoader. 
   */
  private File dexOutputDir ;

  public tuPrologActivity() {
    context = this;
  }

  public static Context getContext() {
    return context;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getLayoutInflater().inflate(R.layout.main, frameLayout);

    mDrawerList.setItemChecked(position, true);
    setTitle(listArray[position]);

    mDbHelper = new TheoryDbAdapter(this);
    mDbHelper.open();

    textView = (TextView) this.findViewById(R.id.textView);
    titleText = (EditText) this.findViewById(R.id.titleQuery);
    editText = (AutoCompleteTextView) this.findViewById(R.id.editText);
    execute = (Button) this.findViewById(R.id.btnExecute);
    next = (Button) this.findViewById(R.id.btnNext);
    clear = (Button) this.findViewById(R.id.btnClear);
    tabHost = (TabHost) this.findViewById(R.id.tabhost);

    solutionView = (TextView) this.findViewById(R.id.solutionView);
    outputView = (TextView) this.findViewById(R.id.outputView);

    solutionView.setMovementMethod(new ScrollingMovementMethod());
    outputView.setMovementMethod(new ScrollingMovementMethod());

    toast = Toast.makeText(tuPrologActivity.this, "Insert rule (Title + Body)",
            Toast.LENGTH_LONG);

    tabHost.setup();
    tabHost.addTab(tabHost.newTabSpec("Solution").setIndicator("Solution")
            .setContent(R.id.solutionView));
    tabHost.addTab(tabHost.newTabSpec("Output").setIndicator("Output")
            .setContent(R.id.outputView));

    tabHost.getTabWidget();

    CUIConsole.main(textView, titleText, editText, execute, solutionView, outputView,
            next, toast, this, clear);

   Bundle bundle = getIntent().getExtras();
    Theory theory = (Theory) bundle.getSerializable("theory");
    try {
      CUIConsole.engine.setTheory(theory);
      setBoolTheory();
      textView.setText("Selected Theory : " + bundle.getString("nameTheory"));
      Toast.makeText(context, "Set theory" + bundle.getString("nameTheory") , Toast.LENGTH_SHORT).show();
    }catch (InvalidTheoryException e) {
      e.printStackTrace();
      Toast.makeText(context, "Invalid theory ", Toast.LENGTH_SHORT).show();
      CUIConsole.engine.clearTheory();

    }



      /**
       *  @author Alessio Mercurio
       *
       *  Create and set the optimized files directory into the Android LibraryManager and Java LibraryManager.
       *  This is the directory where optimized dex files should be written; must not be null.
       *
       */

      dexOutputDir = this.getApplicationContext().getDir("dex", 0);
      CUIConsole.libraryManager.setOptimizedDirectory(dexOutputDir.getAbsolutePath());
      CUIConsole.engine.getLibraryManager().setOptimizedDirectory(dexOutputDir.getAbsolutePath());


  }


  @Override
  public void onBackPressed() {

    tuPrologActivity.this.finish();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    super.onActivityResult(requestCode, resultCode, intent);
    if (resultCode == RESULT_OK) {
      Bundle extras = intent.getExtras();

      switch (requestCode) {
      case ACTIVITY_SELECT:

        Long id = extras.getLong(TheoryDbAdapter.KEY_ROWID);
        if (id != null) {
          Cursor theoryCursor = mDbHelper.fetchTheory(id);
          startManagingCursor(theoryCursor);
          Theory oldTheory = CUIConsole.engine.getTheory();

          try {
            Theory t;
            t = new Theory(theoryCursor.getString(theoryCursor
                .getColumnIndexOrThrow(TheoryDbAdapter.KEY_BODY))
                + System.getProperty("line.separator"));
            CUIConsole.engine.setTheory(t);
            setBoolTheory();
            textView.setText("Selected Theory : "
                + theoryCursor.getString(theoryCursor
                    .getColumnIndexOrThrow(TheoryDbAdapter.KEY_TITLE)));
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
}