package alice.tuprologx.android;

import java.io.File;

import alice.tuprolog.InvalidTheoryException;
import alice.tuprolog.Theory;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class tuPrologActivity extends Activity {

  private TextView textView;
  private AutoCompleteTextView editText;
  private Button execute;
  private Button next;
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
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_page_menu, menu);
    return true;
  }

  @SuppressWarnings("static-access")
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case R.id.theories_list:
      Intent i = new Intent(this, TheoriesDatabaseActivity.class);
      startActivityForResult(i, ACTIVITY_SELECT);
      return true;

    /**
     * @author Alessio Mercurio
     * LibraryManager menu item.
     */
    case R.id.library_manager:
    	Intent i2 = new Intent(this, LibraryManagerActivity.class);
    	startActivityForResult(i2, ACTIVITY_LIBRARY);
    	return true;
      
    case R.id.about:
      AlertDialog.Builder alert = new AlertDialog.Builder(this);

      alert.setTitle("About tuProlog");
		try {
				alert.setMessage(""
						+ "- tuProlog for Android - \n Version: "
						+ tuPrologActivity
								.getContext()
								.getPackageManager()
								.getPackageInfo(
										tuPrologActivity.getContext()
												.getPackageName(), 0).versionName
						+ "\n\nhttp://tuprolog.alice.unibo.it");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
			e.printStackTrace();
		}
      alert.show();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onBackPressed() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to exit?").setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            tuPrologActivity.this.finish();
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mDbHelper = new TheoryDbAdapter(this);
    mDbHelper.open();

    textView = (TextView) this.findViewById(R.id.textView);
    editText = (AutoCompleteTextView) this.findViewById(R.id.editText);
    execute = (Button) this.findViewById(R.id.btnExecute);
    next = (Button) this.findViewById(R.id.btnNext);
    tabHost = (TabHost) this.findViewById(R.id.tabhost);

    solutionView = (TextView) this.findViewById(R.id.solutionView);
    outputView = (TextView) this.findViewById(R.id.outputView);

    toast = Toast.makeText(tuPrologActivity.this, "Insert rule",
        Toast.LENGTH_LONG);

    tabHost.setup();
    tabHost.addTab(tabHost.newTabSpec("Solution").setIndicator("Solution")
        .setContent(R.id.solutionView));
    tabHost.addTab(tabHost.newTabSpec("Output").setIndicator("Output")
        .setContent(R.id.outputView));

    tabHost.getTabWidget();

    CUIConsole.main(textView, editText, execute, solutionView, outputView,
        next, toast, this);

    
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