package com.bignerdranch.android.expense;

import androidx.fragment.app.Fragment;

public class ExpenseListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new ExpenseListFragment();
    }
}
