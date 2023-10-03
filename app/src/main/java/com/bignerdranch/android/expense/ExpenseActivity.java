package com.bignerdranch.android.expense;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import java.util.UUID;

public class ExpenseActivity extends SingleFragmentActivity {

    private static final String EXTRA_EXPENSE_ID =
            "com.bignerdranch.android.expense.expense_id";

    public static Intent newIntent(Context packageContext, UUID expenseId){
        Intent intent = new Intent(packageContext, ExpenseActivity.class);
        intent.putExtra(EXTRA_EXPENSE_ID, expenseId);
        return intent;
    }

    @Override
    protected Fragment createFragment(){
        UUID expenseId = (UUID) getIntent().getSerializableExtra(EXTRA_EXPENSE_ID);
        return ExpenseFragment.newInstance(expenseId);
    }
}

