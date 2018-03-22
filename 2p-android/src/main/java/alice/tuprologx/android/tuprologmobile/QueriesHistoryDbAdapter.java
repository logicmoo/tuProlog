package alice.tuprologx.android.tuprologmobile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Lucrezia on 27/06/2016.
 */
public class QueriesHistoryDbAdapter {

    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "TheoryHistoryDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_CREATE = "create table theoriesHistory (_id integer primary key autoincrement, "
            + "title text not null, body text not null);";

    private static final String DATABASE_NAME = "data2";
    private static final String DATABASE_TABLE = "theoriesHistory";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS theories");
            onCreate(db);
        }
    }

    public QueriesHistoryDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public QueriesHistoryDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createQuery(String title, String body) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_BODY, body);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

public boolean deleteQuery(long rowId) {

    if (rowId < 0)
        return mDb.delete(DATABASE_TABLE, null , null) > 0;

    return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllQueries() {

        return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
                KEY_BODY }, null, null, null, null, null);
    }

    public Cursor fetchQuery(long rowId) throws SQLException {

        Cursor mCursor =

                mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
                        KEY_BODY }, KEY_ROWID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor searchQuery(String titleSearch) throws  SQLException {

        Cursor cursor = mDb.rawQuery("SELECT " + KEY_ROWID + "," + KEY_TITLE + ", " +
                KEY_BODY + " FROM " + DATABASE_TABLE +
                " WHERE upper(" + KEY_TITLE + ") like '%" + titleSearch.toUpperCase() + "%'", null);
        return cursor;
    }

    public boolean updateQuery(long rowId, String title, String body) {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_BODY, body);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
