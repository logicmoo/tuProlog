package alice.tuprologx.android;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import alice.tuprolog.MalformedGoalException;
import alice.tuprolog.NoMoreSolutionException;
import alice.tuprolog.NoSolutionException;
import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.lib.UserContextInputStream;
import alice.tuprolog.Var;
import alice.tuprolog.event.ExceptionEvent;
import alice.tuprolog.event.ExceptionListener;
import alice.tuprolog.event.OutputEvent;
import alice.tuprolog.event.OutputListener;
import alice.tuprolog.event.ReadEvent;
import alice.tuprolog.event.ReadListener;
import alice.tuprolog.event.SpyEvent;
import alice.tuprolog.event.SpyListener;
import alice.tuprolog.event.WarningEvent;
import alice.tuprolog.event.WarningListener;
import alice.tuprolog.lib.IOLibrary;
import alice.util.Automaton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CUIConsole extends Automaton implements Serializable,
    WarningListener, OutputListener, SpyListener, ExceptionListener {

  private static final long serialVersionUID = 1L;
  public static Prolog engine;
  private SolveInfo info;
  
  /** 
   * @author Alessio Mercurio
   * 
   * Android LibraryManager
   */
  public static LibraryManager libraryManager;


  private static TextView textView;
  private static AutoCompleteTextView editText;
  private static Button button;
  private static Button btnext;
  private static TextView solution;
  private static TextView output;
  private ArrayList<String> arrayList = new ArrayList<String>();
  private Toast toast;
  
  private UserContextInputStream stream;
  private alice.tuprologx.android.tuPrologActivity tup;

  public CUIConsole(TextView tv, AutoCompleteTextView et, Button btn,
      TextView sol, TextView out, Button next, Toast t, tuPrologActivity tuP) {
    engine = new Prolog();

    engine.addWarningListener(this);
    engine.addOutputListener(this);
    engine.addSpyListener(this);
    engine.addExceptionListener(this);
    
    /**
     * @author Alessio Mercurio
     * 
     * LibraryManager initialization
     */
    libraryManager = new LibraryManager();
    libraryManager.setEngine(engine);

    textView = tv;
    editText = et;
    button = btn;
    btnext = next;
    btnext.setEnabled(false);
    solution = sol;
    output = out;
    toast = t;
    tup = tuP;
    
    IOLibrary IO = (IOLibrary) engine.getLibrary("alice.tuprolog.lib.IOLibrary");
    IO.setExecutionType(IOLibrary.graphicExecution); // changed from IO.graphicExecution to IOLibrary.graphicExecution
    stream = IO.getUserContextInputStream();
    
    setReadEventListener();

    button.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {
    	  
    	stream.setCounter();
        btnext.setEnabled(false);
        if (editText.getText().toString().equals("")) {
          toast.show();
        } else {
          ArrayAdapter<String> aa = new ArrayAdapter<String>(tuPrologActivity
              .getContext(), android.R.layout.simple_dropdown_item_1line,
              arrayList);
          if (!arrayList.contains(editText.getText().toString()))
            arrayList.add(editText.getText().toString());

          editText.setAdapter(aa);
          goalRequest();

          if ((engine.hasOpenAlternatives())) {
            btnext.setEnabled(true);
          }
        }
      }

    });

    btnext.setOnClickListener(new OnClickListener() {

      public void onClick(View v) {

        String choice = "";
        try {
          if ((info = engine.solveNext()) != null) {
            if (!info.isSuccess()) {
              solution.setText("no.\n");
              become("goalRequest");
            } else {
              choice = solveInfoToString(info) + "\n";
              solution.setText(choice);
            }
          }
        } catch (NoMoreSolutionException e) {

          Toast toast = Toast.makeText(tuPrologActivity.getContext(),
              "No more solutions", Toast.LENGTH_SHORT);
          toast.show();
          e.printStackTrace();
        }
      }

    });
  }

  @Override
  public void boot() {

    String theory = engine.getTheory().toString();
    if (theory.equals("")) {
      textView.setText("No Theory file selected.");
    } else {
      textView.setText("Selected Theory : " + theory);
    }
		try {
			solution.setText("tuProlog "
					+ tuPrologActivity
							.getContext()
							.getPackageManager()
							.getPackageInfo(
									tuPrologActivity.getContext()
											.getPackageName(), 0).versionName
					+ " " + new Date().toLocaleString() + "\n");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// become("goalRequest");
  }

  public void goalRequest() {

    String goal = "";
    while (goal.equals("")) {
      goal = editText.getText().toString();
    }
    solveGoal(goal);
  }

  void solveGoal(String goal) {
    output.setText("\n");
    try
    {
      SolveInfo localSolveInfo = engine.solve(goal);
      if (!localSolveInfo.isSuccess())
      {
        if (localSolveInfo.isHalted())
          solution.setText("halt.\n");
        else
          solution.setText("no.\n");
        become("goalRequest");
      }
      else if (!engine.hasOpenAlternatives())
      {
        String str = localSolveInfo.toString();
        if (str.equals(""))
          solution.setText("yes.");
        else
          solution.setText(solveInfoToString(localSolveInfo) + "\nyes.");
        become("goalRequest");
      }
      else
      {
        String str = solveInfoToString(localSolveInfo);
        if (str.equals("")) {
          str = "yes.\n";
        }
        solution.setText(str + " ? ");
        become("goalRequest");
      }
    }
    catch (MalformedGoalException localMalformedGoalException)
    {
      solution.setText("syntax error in goal:\n" + goal);
      become("goalRequest");
    }
  }
  
  private String solveInfoToString(SolveInfo paramSolveInfo)
  {
    String str = "";
    try
    {
      @SuppressWarnings("rawtypes")
      Iterator localIterator = paramSolveInfo.getBindingVars().iterator();
      while (localIterator.hasNext())
      {
        Var localVar = (Var)localIterator.next();
        if ((!localVar.isAnonymous()) && (localVar.isBound()) && ((!(localVar.getTerm() instanceof Var)) || (!((Var)(Var)localVar.getTerm()).getName().startsWith("_"))))
          str = str + localVar.getName() + " / " + localVar.getTerm() + "\n";
      }
      if (str.length() > 0)
        str.substring(0, str.length() - 1);
    }
    catch (NoSolutionException localNoSolutionException)
    {
    }
    return str;
  }

  private void setReadEventListener()
  {
	  stream.setReadListener(new ReadListener()
	  {
		public void readCalled(ReadEvent arg0) 
		{
			new Thread(new Runnable()
			{					
				public void run() 
				{
					Looper.prepare();

					AlertDialog.Builder alert = new AlertDialog.Builder(tup);
					alert.setTitle("Input Console");
					final EditText input = new EditText(tup);
					alert.setView(input);

					alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						stream.putInput(new ByteArrayInputStream(input.getText().toString().getBytes()));
					  }
					});
					alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					  public void onClick(DialogInterface dialog, int whichButton) {
						  stream.putInput(null);
					  }
					});

					alert.show();
					Looper.loop();
				}	
			}).start();

		}
	  });
  }
  
  public void onOutput(OutputEvent e) {
    output.setText(e.getMsg());
  }

  public void onSpy(SpyEvent e) {
    output.setText(e.getMsg());
  }

  public void onWarning(WarningEvent e) {
    output.setText(e.getMsg());
  }

  public void onException(ExceptionEvent e) {
    output.setText(e.getMsg());
  }

  public static void main(TextView tv, AutoCompleteTextView et, Button btn,
      TextView sol, TextView out, Button next, Toast t, tuPrologActivity tuP) {

    new Thread(new CUIConsole(tv, et, btn, sol, out, next, t, tuP)).start();

  }

}