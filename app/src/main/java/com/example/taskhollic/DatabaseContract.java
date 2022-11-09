package com.example.taskhollic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class DatabaseContract {
    private static final String DATABASE_TABLE = "Task_table";
    private final String DATABASE_NAME = "AppDataBase";
    private final int DATABASE_VERSION = 1;

    private SQLiteDatabase ourWritableDataBase, ourReadableDataBase;
    private DBHelper DBHelper;
    private final Context ourContext;

    DatabaseContract(Context context) {
        this.ourContext = context;
    }

    public static class TaskEntry implements BaseColumns {
        public static final String COLUMN_ID = "task_id";
        public static final String COLUMN_NAME = "task_name";
        public static final String COlUMN_DESCRIPTION = "task_description";
        public static final String COlUMN_FLAG_IMPORTANT = "task_flag_important";
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String SQLCreateTable = "CREATE TABLE " + DATABASE_TABLE
                    + " ( " + TaskEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TaskEntry.COLUMN_NAME + " TEXT NOT NULL, "
                    + TaskEntry.COlUMN_DESCRIPTION + " TEXT NOT NULl, "
                    + TaskEntry.COlUMN_FLAG_IMPORTANT + " TEXT NOT NULL)";
            db.execSQL(SQLCreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    public DatabaseContract Open() {
        DBHelper = new DBHelper(ourContext);
        ourWritableDataBase = DBHelper.getWritableDatabase();
        ourReadableDataBase = DBHelper.getReadableDatabase();
        return this;
    }

    public void Close() {
        DBHelper.close();
    }

    public void AddNewTask(TaskClass task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEntry.COLUMN_NAME, task.getName());
        contentValues.put(TaskEntry.COlUMN_DESCRIPTION, task.getDescription());
        contentValues.put(TaskEntry.COlUMN_FLAG_IMPORTANT, Boolean.toString(task.getImportant()));

        ourWritableDataBase.insert(DATABASE_TABLE, null, contentValues);
    }

    public void DeleteTask(TaskClass task) {
        ourWritableDataBase.delete(DATABASE_TABLE, TaskEntry.COLUMN_ID + "=?"
                , new String[]{String.valueOf(task.getId())});
    }

    public void UpdateTask(TaskClass task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEntry.COLUMN_NAME, task.getName());
        contentValues.put(TaskEntry.COlUMN_DESCRIPTION, task.getDescription());
        contentValues.put(TaskEntry.COlUMN_FLAG_IMPORTANT, Boolean.toString(task.getImportant()));

        ourWritableDataBase.update(DATABASE_TABLE, contentValues
                ,TaskEntry.COLUMN_ID + "=?", new String[]{String.valueOf(task.getId())});
    }

    public ArrayList<TaskClass> GetTaskList() {
        ArrayList<TaskClass> taskList = new ArrayList<>();
        String[] columns = {
                TaskEntry.COLUMN_ID,
                TaskEntry.COLUMN_NAME,
                TaskEntry.COlUMN_DESCRIPTION,
                TaskEntry.COlUMN_FLAG_IMPORTANT,
        };

        Cursor cursor = ourReadableDataBase.query(DATABASE_TABLE, columns, null
                , null, null, null, null);

        int indexId = cursor.getColumnIndex(TaskEntry.COLUMN_ID);
        int indexName = cursor.getColumnIndex(TaskEntry.COLUMN_NAME);
        int indexDescription = cursor.getColumnIndex(TaskEntry.COlUMN_DESCRIPTION);
        int indexFlag = cursor.getColumnIndex(TaskEntry.COlUMN_FLAG_IMPORTANT);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            TaskClass task = new TaskClass(cursor.getInt(indexId), cursor.getString(indexName), cursor.getString(indexDescription),
                    Boolean.parseBoolean(cursor.getString(indexFlag)));
            taskList.add(task);
        }

        cursor.close();
        return taskList;
    }

    public int getRowCount(){
        return (int) DatabaseUtils.queryNumEntries(ourReadableDataBase, DATABASE_TABLE);
    }
}
