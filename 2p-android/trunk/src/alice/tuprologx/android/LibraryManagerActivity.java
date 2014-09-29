package alice.tuprologx.android;

import alice.tuprolog.InvalidLibraryException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Alessio Mercurio
 * 
 * Activity used for the library management.
 *
 */

public class LibraryManagerActivity extends Activity
{
	private static final int ACTIVITY_IMPORT = 2;

	private LibraryManager libraryManager;

	private String fileAbsolutePath, libraryClassname;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_manager);
		
		this.libraryManager = CUIConsole.libraryManager;
		
		ListView lwLoaded = (ListView) findViewById(R.id.librariesList);
		lwLoaded.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> listView, View view,
					int position, long itemId)
			{
				setClassname(listView.getItemAtPosition(position).toString());
			}
		});

		displayLibraryManagerStatus();
	}

	/*
	 * displays the Library Manager status
	 */

	public void displayLibraryManagerStatus()
	{

		ListView lwLoaded = (ListView) findViewById(R.id.librariesList);

		Object[] libraries = libraryManager.getLibraries();
		String[] array = new String[libraries.length];

		for (int i = 0; i < libraries.length; i++)
		{
			array[i] = libraries[i].toString();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.list_libraries, array);
		lwLoaded.setAdapter(adapter);
	}

	/*
	 * Active LibraryImportActivity, used to locate library files.
	 */
	
	public void browseOnClickListener(View view)
	{
		Intent i = new Intent(this, LibraryImportActivity.class);
		startActivityForResult(i, ACTIVITY_IMPORT);
	}

	/*
	 * Load a library and updates the Library Manager status.
	 */
	
	public void addOnClickListener(View view) throws InvalidLibraryException,
			InstantiationException, IllegalAccessException
	{
		libraryClassname = getClassname();

		if (libraryClassname.equals(""))
		{
			Toast t = Toast.makeText(this, "Please enter a library name!", Toast.LENGTH_SHORT);
			t.show();
		} 
		else
		{
			try
			{
				if (libraryManager.contains(libraryClassname))
					setStatusMessage(libraryClassname
							+ ": Library already loaded");
				else
				{
					libraryManager.addLibrary(libraryClassname,
							fileAbsolutePath);
					libraryManager.loadLibrary(libraryClassname,
							fileAbsolutePath);

					displayLibraryManagerStatus();
					setStatusMessage(libraryClassname + ": successfully loaded");
				}
			} catch (ClassNotFoundException e)
			{
				setStatusMessage(libraryClassname + ": Class Not Found");
			} catch (InvalidLibraryException e)
			{
				setStatusMessage(libraryClassname + ": Not a Library");
			}
		}
	}

	/*
	 * Remove a library and updates the Library Manager status.
	 */
	
	public void removeOnClickListener(View view) throws InvalidLibraryException
	{
		libraryClassname = getClassname();

		if (libraryClassname.equals(""))
		{
			Toast t = Toast.makeText(this, "Please enter a library name!", Toast.LENGTH_SHORT);
			t.show();
		} 
		else
		{
			try
			{
				if (!libraryManager.contains(libraryClassname))
				{
					setStatusMessage(libraryClassname + ": is not loaded");
				} else
				{
					if (libraryManager.isExternalLibrary(libraryClassname))
						libraryManager.unloadExternalLibrary(libraryClassname);
					if (libraryManager.isLibraryLoaded(libraryClassname))
						libraryManager.unloadLibrary(libraryClassname);
					libraryManager.removeLibrary(libraryClassname);

					displayLibraryManagerStatus();
					setStatusMessage(libraryClassname
							+ ": successfully removed");
				}
			} catch (InvalidLibraryException e)
			{
				setStatusMessage(libraryClassname + ": Not a Library");
			}
		}
	}

	private void setClassname(String classname)
	{
		EditText etLibraryName = (EditText) findViewById(R.id.libraryNameEditText);
		etLibraryName.setText(classname);
	}

	private String getClassname()
	{
		EditText etLibraryName = (EditText) findViewById(R.id.libraryNameEditText);
		return etLibraryName.getText().toString();
	}

	private void setStatusMessage(String string)
	{
		TextView twStatus = (TextView) findViewById(R.id.statusView);
		twStatus.setText(string);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);

		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case ACTIVITY_IMPORT:
					Bundle extras = intent.getExtras();
					fileAbsolutePath = extras.getString("fileAbsolutePath");

					break;
			}
		}
	}
}
