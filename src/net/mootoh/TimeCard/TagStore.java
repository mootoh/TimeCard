package net.mootoh.TimeCard;

import java.sql.SQLException;
import java.util.ArrayList;

import net.mootoh.TimeCard.Tag;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final class TagStore {
    DatabaseHelper databaseHelper;

    public TagStore(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void addTag(String tagId, String name, String color) throws SQLException {
        ContentValues values = new ContentValues();
        values.put("id", tagId);
        values.put("name", name);
        values.put("color", color);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert("tags", null, values);
        if (rowId <= 0)
            throw new SQLException("Faild to insert row for " + tagId);
    }

    public void deleteTag(String tagId) throws Exception {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int deleted = db.delete("tags", "id is " + tagId, null);
        if (deleted != 1)
            throw new Exception("Failed in deleting a tag:" + tagId);
    }

    public String currentTagId() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("touches", null, null, null, null, null, "touchedAt", "1");
        if (cursor.moveToFirst()) {
            int isOn = cursor.getInt(cursor.getColumnIndex("isOn"));
            if (isOn == 1)
                return cursor.getString(cursor.getColumnIndex("tagId"));
        }
        return null;
    }

    public void getTables() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query("tags", null, null, null, null, null, null, "1");
        Log.d(getClass().getSimpleName(), "cursor count: " + cursor.getCount());
    }

    public void reset() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("tags", null, null);
        db.delete("touches", null, null);
    }

    public Tag[] getTags() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("tags", null, null, null, null, null, null);
        ArrayList <Tag> tags = new ArrayList<Tag>();
        if (! cursor.moveToFirst()) {
            Tag[] nothing = {};
            return nothing;
        }

        do {
            Tag tag = new Tag(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("color")));
            cursor.moveToNext();
            tags.add(tag);
        } while (cursor.moveToNext());
        Log.d(getClass().getSimpleName(), "tag count = " + tags.size());
        Tag[] ret = new Tag[tags.size()];
        tags.toArray(ret);
        return ret;
    }
}

final class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION  = 1;
    private static final String DB_NAME  = "TimeCard";
    private static final String[] DB_CREATE = {
        "create TABLE "
                + "tags ("
                + "id TEXT not null primary key, "
                + "name TEXT not null, "
                + "color TEXT"
                + ");",
        "create TABLE "
                + "touches ("
                + "tagId text not null primary key, "
                + "touchedAt INTEGER not null, "
                + "isON INTEGER not null"
                + ");"};

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(getClass().getSimpleName(), "ctor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(getClass().getSimpleName(), "onCreate");
        try {
            for (String sql : DB_CREATE)
                db.execSQL(sql);
        } catch (android.database.SQLException ex) {
            Log.d(getClass().getSimpleName(), "exception in creating db: " + ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tags; DROP TABLE IF EXISTS touches");
        onCreate(db);
    }
}