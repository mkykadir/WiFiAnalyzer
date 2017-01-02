package tryucelmuh.edu.itu.web.wifianalyzer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by USER on 2.01.2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "wifiAnalytics.db";
    public static final String MAIN_TB_NAME = "wifiinfo";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SSID = "ssid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_BSSID = "bssid";
    public static final String COLUMN_STRENGTH = "stregth";
    public static final String COLUMN_DATE = "date";

    public DataBaseHelper(Context context){
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + MAIN_TB_NAME + "(" +
                COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_SSID + " text, " +
                COLUMN_TITLE + " text, " +
                COLUMN_BSSID + " text, " +
                COLUMN_STRENGTH + " text, " +
                COLUMN_DATE + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MAIN_TB_NAME);
        onCreate(db);
    }

    public void insertValue(HashMap.Entry<mainSsid, List<mainBssid>> value){
        SQLiteDatabase db = this.getWritableDatabase();
        mainSsid keyToInsert = value.getKey();
        List<mainBssid> dataToInsert = value.getValue();

        for(int i = 0;i < dataToInsert.size(); i++){
            ContentValues content = new ContentValues();
            content.put(COLUMN_SSID, keyToInsert.returnSsid());
            content.put(COLUMN_TITLE, keyToInsert.returnId());
            content.put(COLUMN_BSSID, dataToInsert.get(i).returnBssid());
            content.put(COLUMN_STRENGTH, dataToInsert.get(i).returnStrength());
            content.put(COLUMN_DATE, keyToInsert.returnCurrDate());

            db.insert(MAIN_TB_NAME, null, content);
        }
    }

    public void clearTable(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + MAIN_TB_NAME);
    }
}
