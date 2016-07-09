package Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by user on 2016-07-09.
 */
public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "log_Database";

    public static final String tableName_keys = "keys";
    public static final String tableName_selected = "selected";

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tableName_keys + "(value text, date text)";
        db.execSQL(sql);

        sql = "CREATE TABLE " + tableName_selected + "(deviceAddr text,   date text, PRIMARY KEY (deviceAddr))";
        db.execSQL(sql);

    }

    public void removeAllTuple(SQLiteDatabase db, String tableName) {
        String sql = "DELETE FROM " + tableName + ";";
        Log.i(TAG, "sql=" + sql);

        db.execSQL(sql);
    }
    public void insertIntoSelected(SQLiteDatabase db, String deviceAddr) {
        db.beginTransaction();
        try {
            String sql = "insert IGNORE into " + tableName_keys + " values('" + deviceAddr +"', '"+getTime() +"')";
            Log.i(TAG,"sql="+sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
    public void insertIntoKeys(SQLiteDatabase db, String KeyValue) {
        db.beginTransaction();
        try {
            String sql = "insert into " + tableName_selected + "(value, date)" + " values('" + KeyValue + "', '"+getTime()+"')";
            Log.i(TAG,"sql="+sql);
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

    }

    public void selectAll(SQLiteDatabase db, String tableName) {
        db.beginTransaction();

        String sql = "SELECT * from "+ tableName +";";
        Log.i(TAG, "sql=" + sql);

        Cursor results = db.rawQuery(sql, null);

        results.moveToFirst();
        while(!results.isAfterLast()){
            String value = results.getString(0);
            String date = results.getString(1);

            Log.i(TAG, tableName + " val=" + value);
            Log.i(TAG,tableName+" date="+date);
            results.moveToNext();
        }
        results.close();
    }

    public String getTime() {
        String result="";
        Calendar c = Calendar.getInstance();

        result+=c.get(Calendar.YEAR);
        result+="-";
        if( c.get(Calendar.MONTH)+1 < 10)
            result+="0";
        result+=c.get(Calendar.MONTH)+1; // 월 (0부터 시작하는듯 +1 해야한다)
        result+="-";
        if( c.get(Calendar.DAY_OF_MONTH) < 10)
            result+="0";
        result+=c.get(Calendar.DAY_OF_MONTH); // 일
        result+=" ";
        result+=c.get(Calendar.HOUR_OF_DAY); // 시 (24)
        result+=":";
        result+=c.get(Calendar.MINUTE); // 분
        result+=":";
        result+=c.get(Calendar.SECOND); // 초

        return result;
    }
}
