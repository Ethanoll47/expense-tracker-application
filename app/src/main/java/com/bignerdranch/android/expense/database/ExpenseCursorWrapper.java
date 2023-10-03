package com.bignerdranch.android.expense.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.expense.Expense;

import java.util.Date;
import java.util.UUID;

public class ExpenseCursorWrapper extends CursorWrapper{
    public ExpenseCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Expense getExpense(){
        String uuidString = getString(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.UUID));
        String amount = getString(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.AMOUNT));
        String category = getString(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.CATEGORY));
        String details = getString(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.DETAILS));
        long date = getLong(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.DATE));
        String contact = getString(getColumnIndex(ExpenseDbSchema.ExpenseTable.Cols.CONTACT));

        Expense expense = new Expense(UUID.fromString(uuidString));
        expense.setAmount(amount);
        expense.setCategory(category);
        expense.setDetails(details);
        expense.setDate(new Date(date));
        expense.setContact(contact);

        return expense;
    }
}
