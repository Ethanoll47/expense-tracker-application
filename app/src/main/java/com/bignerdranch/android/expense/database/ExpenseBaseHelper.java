package com.bignerdranch.android.expense.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.expense.database.ExpenseDbSchema.ExpenseTable;

public class ExpenseBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expenseBase.db";

    public ExpenseBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + ExpenseTable.NAME + "(" +
            "_id integer primary key autoincrement, " +
                ExpenseTable.Cols.UUID + ", " +
                ExpenseTable.Cols.AMOUNT + "," +
                ExpenseTable.Cols.CATEGORY + "," +
                ExpenseTable.Cols.DETAILS + "," +
                ExpenseTable.Cols.DATE + ", " +
                ExpenseTable.Cols.CONTACT +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

}
