package com.bignerdranch.android.expense;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class ExpensePagerActivity extends AppCompatActivity {
    private static final String EXTRA_EXPENSE_ID = "com.bignerdranch.android.expense.expense_id";

    private ViewPager mViewPager;
    private List<Expense> mExpenses;

    public static Intent newIntent(Context packageContext, UUID expenseId){
        Intent intent = new Intent(packageContext, ExpensePagerActivity.class);
        intent.putExtra(EXTRA_EXPENSE_ID, expenseId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_pager);

        UUID expenseId = (UUID) getIntent().getSerializableExtra(EXTRA_EXPENSE_ID);

        mViewPager = (ViewPager) findViewById(R.id.expense_view_pager);

        mExpenses = ExpenseLab.get(this).getExpenses();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager){

            @Override
            public Fragment getItem(int position){
                Expense expense = mExpenses.get(position);
                return ExpenseFragment.newInstance(expense.getId());
            }
            @Override
            public int getCount(){
                return mExpenses.size();
            }
        });

        for (int i = 0; i < mExpenses.size(); i++){
            if(mExpenses.get(i).getId().equals(expenseId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }
}
