package com.bignerdranch.android.expense;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.expense.database.ExpenseBaseHelper;
import com.bignerdranch.android.expense.database.ExpenseCursorWrapper;
import com.bignerdranch.android.expense.database.ExpenseDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpenseLab {
    private static ExpenseLab sExpenseLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ExpenseLab get(Context context){
        if(sExpenseLab == null){
            sExpenseLab =  new ExpenseLab(context);
        }
        return sExpenseLab;
    }

    private ExpenseLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ExpenseBaseHelper(mContext)
                .getWritableDatabase();

    }

    public void addExpense(Expense c){
        ContentValues values = getContentValues(c);

        mDatabase.insert(ExpenseDbSchema.ExpenseTable.NAME, null, values);

    }

    public void deleteExpense(UUID id){
        String[] whereArgs = {"" + id};
        mDatabase.delete(ExpenseDbSchema.ExpenseTable.NAME, ExpenseDbSchema.ExpenseTable.Cols.UUID + " = ?", whereArgs);
    }

    public List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();

        ExpenseCursorWrapper cursor = queryExpenses(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return expenses;
    }

     public Expense getExpense(UUID id){
        ExpenseCursorWrapper cursor = queryExpenses(
                ExpenseDbSchema.ExpenseTable.Cols.UUID + " = ?",
                new String[] { id.toString()}
        );

        try {
            if (cursor.getCount()==0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getExpense();
        } finally {
            cursor.close();
        }
     }

    public File getPhotoFile(Expense expense) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, expense.getPhotoFilename());
    }

     public void updateExpense(Expense expense){
        String uuidString = expense.getId().toString();
        ContentValues values = getContentValues(expense);

        mDatabase.update(ExpenseDbSchema.ExpenseTable.NAME, values,
                ExpenseDbSchema.ExpenseTable.Cols.UUID + " = ?",
                new String[]{ uuidString});
     }


     private ExpenseCursorWrapper queryExpenses(String whereClause, String[] whereArgs){
         Cursor cursor = mDatabase.query(
             ExpenseDbSchema.ExpenseTable.NAME,
             null, // columns - null selects all columns
             whereClause,
             whereArgs,
             null, // groupBy
             null, // having
             null // orderBy
         );

         return new ExpenseCursorWrapper(cursor);
     }

     private static ContentValues getContentValues(Expense expense){
        ContentValues values = new ContentValues();
        values.put(ExpenseDbSchema.ExpenseTable.Cols.UUID, expense.getId().toString());
        values.put(ExpenseDbSchema.ExpenseTable.Cols.AMOUNT, expense.getAmount());
        values.put(ExpenseDbSchema.ExpenseTable.Cols.CATEGORY, expense.getCategory());
        values.put(ExpenseDbSchema.ExpenseTable.Cols.DETAILS, expense.getDetails());
        values.put(ExpenseDbSchema.ExpenseTable.Cols.DATE, expense.getDate().getTime());
        values.put(ExpenseDbSchema.ExpenseTable.Cols.CONTACT, expense.getContact());

        return values;
     }

}
