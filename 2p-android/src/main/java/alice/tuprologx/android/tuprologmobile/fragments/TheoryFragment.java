package alice.tuprologx.android.tuprologmobile.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import alice.tuprologx.android.tuprologmobile.DescriptionActivity;
import alice.tuprologx.android.tuprologmobile.R;
import alice.tuprologx.android.tuprologmobile.TheoryHistoryDbAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TheoryFragment extends android.support.v4.app.Fragment {

    private static final String ACTIVITY = "activity";

    private ListView theoriesListView;
    private TheoryHistoryDbAdapter mHDbHelper;


    public TheoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mHDbHelper = new TheoryHistoryDbAdapter(getActivity());
        mHDbHelper.open();
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_theory, container, false);


        theoriesListView = (ListView) view.findViewById(R.id.listViewTheories);

        fillData();

        theoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DescriptionActivity.class);
                i.putExtra(TheoryHistoryDbAdapter.KEY_ROWID, id);
                i.putExtra(ACTIVITY, "theory");
                startActivity(i);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void fillData() {

        Cursor notesCursor = mHDbHelper.fetchAllTheories();
        getActivity().startManagingCursor(notesCursor);

        String[] from = new String[] { TheoryHistoryDbAdapter.KEY_TITLE };

        int[] to = new int[] { R.id.text1 };

        SimpleCursorAdapter theories = new SimpleCursorAdapter(getActivity(), R.layout.tab_layout, notesCursor, from, to);
        theoriesListView.setAdapter(theories);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Implementing ActionBar Search inside a fragment
        MenuItem item = menu.add("Search");
        item.setIcon(R.drawable.ic_menu_search2); // sets icon
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());

        // modifying the text inside edittext component
        int id = sv.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) sv.findViewById(id);
        textView.setHint("Search Theory...");
        textView.setHintTextColor(Color.parseColor("#FFC9C9C9"));
        textView.setTextColor(Color.parseColor("#ffffff"));

        // implementing the listener
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                    doSearch(s);
                    return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                fillData();
                return false;
            }
        });
        item.setActionView(sv);
    }

    void doSearch(String query) {

            Cursor cursor = mHDbHelper.searchTheory(query);
            getActivity().startManagingCursor(cursor);
            String[] from = new String[] { TheoryHistoryDbAdapter.KEY_TITLE };
            int[] to = new int[] { R.id.text1 };

            theoriesListView.setAdapter(new SimpleCursorAdapter(getActivity(), R.layout.tab_layout, cursor, from, to));


    }

}
