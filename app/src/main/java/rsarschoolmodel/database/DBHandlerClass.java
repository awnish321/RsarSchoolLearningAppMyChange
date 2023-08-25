package rsarschoolmodel.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import rsarschoolmodel.com.rsarschoolmodel.ClassNew;


public class DBHandlerClass extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "rschoolCls.db";
    public static  String TABLE_NAME ;
    public static final String KEY_RSAR_CLASS_ID ="Str_Class_Id";
    public static final String KEY_RSAR_CLASS_NAME ="Str_Class_Name";
    public DBHandlerClass(ClassNew context) {
        super((Context) context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + KEY_RSAR_CLASS_NAME + " TEXT," + KEY_RSAR_CLASS_ID + " TEXT)";
        try{

            db.execSQL(CREATE_TABLE);
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }


}
