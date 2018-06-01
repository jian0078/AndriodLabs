package com.example.janej.andriodlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChatDatabaseHelper extends SQLiteOpenHelper{
    private static String DATABASE_NAME="message.db";
    private static final int VERSION_NUM=2;
    public static  final String KEY_ID="id";
    public static final String KEY_MESSAGE="message";
    public static final String  TABLE_NAME= "myTable";

    private static final String DATABASE_CREATE="create table "
+TABLE_NAME +"(" +KEY_ID+" integer primary key autoincrement, " + KEY_MESSAGE + " text not null);";

    private static final  String DATABASE_DELETE="drop table if exists "+ TABLE_NAME +";" ;

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);

    }
    public ChatDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);

        Log.i("ChatDatabaseHelper", "Calling onCreate");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     db.execSQL(DATABASE_DELETE);
    onCreate(db);
    Log.i("ChatDatebaseHelper", "Calling onUpgrade, oldVersion=" +oldVersion+ " newVersion=" +newVersion);
    }
}
