package alice.tuprologx.android.tuprologmobile;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Lucrezia on 27/06/2016.
 */
public class TheoryCursorAdapter extends CursorAdapter {

    public TheoryCursorAdapter(Context context, Cursor c)
    {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.theories_row, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        ((TextView) view.findViewById(android.R.id.text1)).setText(
                cursor.getString(cursor.getColumnIndex(TheoryHistoryDbAdapter.KEY_TITLE)));
    }
}
