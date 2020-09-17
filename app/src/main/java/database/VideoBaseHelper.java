package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import database.VideoDbSchema.VideoEntry;

public class VideoBaseHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "videosList.db";
    public static final int DATABASE_VERSION = 1;

    public VideoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_VIDEOSLIST_TABLE = "CREATE TABLE " +
                VideoEntry.TABLE_NAME + " (" +
                VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                VideoEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_LINK + " TEXT NOT NULL, " +
                VideoEntry.COLUMN_LIKES + " INTEGER NOT NULL, " +
                VideoEntry.COLUMN_GOAL + ", " +
                VideoEntry.COLUMN_THUMBNAIL + ", " +
                VideoEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_VIDEOSLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + VideoEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
