package net.mootoh.TimeCard;

import java.sql.SQLException;
import java.text.ParseException;
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
    final static private String VOID_TAG_ID = "VOID";

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
        int deleted = db.delete("tags", "id is '" + tagId + "'", null);
        if (deleted != 1)
            throw new Exception("Failed in deleting a tag:" + tagId);
    }

    public boolean isBrandNewTag(String tagId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("tags", null, "id is '" + tagId + "'", null, null, null, null);
        boolean ret = cursor.getCount() == 0;
        cursor.close();
        return ret;
    }

    public Tag currentTag() {
        final String query = "SELECT * FROM touches t1 INNER JOIN tags t2 ON t1.tagId=t2.id ORDER BY t1.touchedAt DESC LIMIT 1";

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Tag returnTag = null;

        if (cursor.moveToFirst()) {
            String tagId = cursor.getString(cursor.getColumnIndex("tagId"));
            String touchedAt = cursor.getString(cursor.getColumnIndex("touchedAt"));
            Log.d(getClass().getSimpleName(), "------- id=" + tagId + ", time=" + touchedAt);

            try {
                Tag current = new Tag(
                        tagId,
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("color")),
                        touchedAt);
                returnTag = current;
            } catch (ParseException e) {
                Log.e(getClass().getSimpleName(), "cannot parse the date format:" + cursor.getString(cursor.getColumnIndex("touchedAt")));
            }
        }
        cursor.close();
        return returnTag;
    }

    public String getTagName(String tagId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String[] columns = { "name" };
        Cursor cursor = db.query("tags", columns, "id is '" + tagId + "'", null, null, null, null);
        String ret = cursor.moveToFirst() ? cursor.getString(cursor.getColumnIndex("name")) : null;
        cursor.close();
        return ret;
    }

    public void reset() throws SQLException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete("tags", null, null);
        db.delete("touches", null, null);

        addVoidTag(db);
        addVoidTouch(db);
}

    private void addVoidTag(SQLiteDatabase db) throws SQLException {
        // void tag is permanent
        ContentValues values = new ContentValues();
        values.put("id", VOID_TAG_ID);
        values.put("name", "void");
        values.put("color", "#999999");

        long rowId = db.insert("tags", null, values);
        if (rowId <= 0)
            throw new SQLException("Faild to insert void tag");
    }

    private void addVoidTouch(SQLiteDatabase db) throws SQLException {
        // void touch is permanent
        ContentValues values = new ContentValues();
        values.put("tagId", VOID_TAG_ID);

        long rowId = db.insert("touches", null, values);
        if (rowId <= 0)
            throw new SQLException("Faild to insert void touch");
    }

    public Tag[] getTags() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query("tags", null, null, null, null, null, null);

        ArrayList <Tag> tags = new ArrayList<Tag>();
        while (cursor.moveToNext()) {
            Log.d(getClass().getSimpleName(),
                     "name:" + cursor.getString(cursor.getColumnIndex("name"))
                    + " id:" + cursor.getString(cursor.getColumnIndex("id")));
            Tag tag = new Tag(cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("color")));
            tags.add(tag);
        }
        cursor.close();

        Tag[] ret = new Tag[tags.size()];
        tags.toArray(ret);
        return ret;
    }

    private void addTouch(String tagId) throws SQLException {
        ContentValues values = new ContentValues();
        values.put("tagId", tagId);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert("touches", null, values);
        if (rowId <= 0)
            throw new SQLException("Faild to insert row for " + tagId);
    }

    public void startTag(String tagId) throws SQLException {
        addTouch(tagId);
    }

    public void stopCurrentTag() throws SQLException {
        addTouch(VOID_TAG_ID);
    }
}

final class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION  = 1;
    private static final String DB_NAME  = "TimeCard";
    private static final String[] DB_CREATE = {
        "create TABLE tags ("
                + "id TEXT not null primary key, "
                + "name TEXT not null, "
                + "color TEXT"
                + ");",
        "create TABLE touches ("
                + "tagId text not null, "
                + "touchedAt DATETIME DEFAULT CURRENT_TIMESTAMP PRIMARY KEY"
                + ");",
        "insert into tags (id, name, color) values ('VOID_TAG_ID', 'void', '999999');",
        "insert into touches (tagId) values ('VOID_TAG_ID');"
    };

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            for (String sql : DB_CREATE)
                db.execSQL(sql);
        } catch (android.database.SQLException ex) {
            Log.e(getClass().getSimpleName(), "exception in creating db: " + ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tags; DROP TABLE IF EXISTS touches");
        onCreate(db);
    }
}