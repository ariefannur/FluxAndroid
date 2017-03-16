package flux.sample.arief.com.sampleflux.repositori;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import flux.sample.arief.com.sampleflux.model.Todo;

/**
 * Copyright (C) PT. Sebangsa Bersama - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Originally written by Author Name sebangsa, 15/03/17
 */

public class LocalRepository extends SQLiteOpenHelper{

    private static LocalRepository instance;
    final Context context;

    private final String TB_TODO = "todo";
    private final String TB_SUBTODO = "subtodo";
    private final String COL_TODO_ID = "id";
    private final String COL_TODO_NAME = "name";
    private final String COL_TODO_DATE = "date";

    private final String COL_SUBTODO_ID = "id_sub";
    private final String COL_SUBTODO_NAME = "name_sub";
    private final String COL_SUBTODO_START = "start";
    private final String COL_SUBTODO_END = "end";

    public LocalRepository(Context context) {
        super(context, "db_sample_flux", null, 1);
        this.context = context;

    }

    public static LocalRepository create(Context context){
        if(instance == null){
            instance = new LocalRepository(context);
        }
        return instance;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TB_TODO+" ("
                        +COL_TODO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_TODO_NAME+" text, date VARCHAR(20))"
        );

        db.execSQL(
                "CREATE TABLE "+TB_SUBTODO+"(" +
                        COL_SUBTODO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_SUBTODO_NAME+" text, " +
                        COL_SUBTODO_START+" INTEGER, " +
                        COL_SUBTODO_END+" INTEGER  )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public long addTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_TODO_NAME, todo.name);
        cv.put(COL_TODO_DATE, todo.date);

        return db.insert(TB_TODO, null, cv);
    }

    public boolean deleteTodo(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.delete(TB_TODO, COL_TODO_ID+ "=" + String.valueOf(id), null) > 0;
    }

    public int updateTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Log.d("AF ", "name "+todo.name+" date : "+todo.date+" id "+todo.id);
        cv.put(COL_TODO_NAME, todo.name);
        cv.put(COL_TODO_DATE, todo.date);

        return db.update(TB_TODO, cv, COL_TODO_ID+"="+String.valueOf(todo.id), null);
    }

    public List<Todo> getAllTodo(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TB_TODO, null);
        cursor.moveToFirst();
        List<Todo> lsTodos = new ArrayList<>();
        while (cursor.isAfterLast() == false){
            Todo todo = new Todo();
            todo.id = cursor.getInt(cursor.getColumnIndex(COL_TODO_ID));
            todo.name = cursor.getString(cursor.getColumnIndex(COL_TODO_NAME));
            todo.date = cursor.getString(cursor.getColumnIndex(COL_TODO_DATE));
            Log.d("AF ", " ids : "+todo.id);
            lsTodos.add(todo);
            cursor.moveToNext();
        }

        return lsTodos;

    }
}
