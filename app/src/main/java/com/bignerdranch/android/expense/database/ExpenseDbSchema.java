package com.bignerdranch.android.expense.database;

public class ExpenseDbSchema {
    public static final class ExpenseTable {
        public static final String NAME = "expenses";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String AMOUNT = "amount";
            public static final String CATEGORY = "category";
            public static final String DETAILS = "details";
            public static final String DATE = "date";
            public static final String CONTACT = "contact";
        }
    }

}
