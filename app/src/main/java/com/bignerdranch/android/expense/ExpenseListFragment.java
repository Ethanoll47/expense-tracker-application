package com.bignerdranch.android.expense;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExpenseListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    private RecyclerView mExpenseRecyclerView;
    private ExpenseAdapter mAdapter;
    private boolean mSubtitleVisible;

    //Create list of category icons
    int[] category_icons = {R.drawable.ic_food,
                            R.drawable.ic_transportation,
                            R.drawable.ic_entertainment,
                            R.drawable.ic_others,
                            R.drawable.ic_withdrawal,
                            R.drawable.ic_fees,
                            R.drawable.ic_insurance,
                            R.drawable.ic_business,
                            R.drawable.ic_investment,
                            R.drawable.ic_education,
                            R.drawable.ic_family,
                            R.drawable.ic_gift,
                            R.drawable.ic_health,
                            R.drawable.ic_shopping,
                            R.drawable.ic_bills};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);

        mExpenseRecyclerView = (RecyclerView) view
                .findViewById(R.id.expense_recycler_view);
        mExpenseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_expense_list, menu);

        //Add search bar
        SearchView searchView = (SearchView) menu.findItem(R.id.search_expense).getActionView();
        searchView.setQueryHint("Search by Category...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter text based on search
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_expense:
                Expense expense = new Expense();
                ExpenseLab.get(getActivity()).addExpense(expense);
                Intent intent = ExpensePagerActivity
                        .newIntent(getActivity(), expense.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        ExpenseLab expenseLab = ExpenseLab.get(getActivity());
        int expenseCount = expenseLab.getExpenses().size();
        String subtitle =  getString(R.string.subtitle_format, expenseCount);

        if (!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI(){
        ExpenseLab expenseLab = ExpenseLab.get(getActivity());
        List<Expense> expenses = expenseLab.getExpenses();
        //Sort items according to newest to oldest date
        Collections.sort(expenses, ComparatorDescendingDate);
        if (mAdapter == null){
            mAdapter = new ExpenseAdapter(expenses);
            mExpenseRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    //Method to compare dates from newest to oldest
    public static Comparator<Expense> ComparatorDescendingDate = new Comparator<Expense>() {
        @Override
        public int compare(Expense expense1, Expense expense2) {
            return expense2.getDate().compareTo(expense1.getDate());
        }
    };

    private class ExpenseHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mAmountTextView;
        private TextView mCategoryTextView;
        private TextView mDetailsTextView;
        private TextView mDateTextView;
        private ImageView mIconView;

        private Expense mExpense;

        public ExpenseHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_expense, parent, false));
            itemView.setOnClickListener(this);

            mAmountTextView = (TextView) itemView.findViewById(R.id.expense_amount_title);
            mCategoryTextView = (TextView) itemView.findViewById(R.id.expense_category);
            mDetailsTextView = (TextView) itemView.findViewById(R.id.expense_details);
            mDateTextView = (TextView) itemView.findViewById(R.id.expense_date);
            mIconView = (ImageView) itemView.findViewById(R.id.expense_icon);

        }

        @Override
        public void onClick(View view){
            Intent intent = ExpensePagerActivity.newIntent(getActivity(), mExpense.getId());
            startActivity(intent);
        }

        public void bind(Expense expense){
            mExpense = expense;
            mAmountTextView.setText(mExpense.getAmount());
            mCategoryTextView.setText(mExpense.getCategory());
            mDetailsTextView.setText(mExpense.getDetails());
            mDateTextView.setText(mExpense.getDate().toString());

            String[] array = getResources().getStringArray(R.array.expense_categories);
            int index = Arrays.asList(array).indexOf(mExpense.getCategory());
            mIconView.setImageResource(category_icons[index]);
        }
    }

    //Method to filter list items
    private class ExpenseAdapter extends RecyclerView.Adapter<ExpenseHolder> implements Filterable {
        private List<Expense> mExpenses;
        private List<Expense> mExpensesAll;

        public ExpenseAdapter(List<Expense> expenses){
            mExpenses = expenses;
            mExpensesAll = new ArrayList<>(expenses);
        }

        @Override
        public ExpenseHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return  new ExpenseHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ExpenseHolder holder, int position){
            Expense expense = mExpenses.get(position);
            holder.bind(expense);
        }

        @Override
        public int getItemCount(){
            return mExpenses.size();
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        Filter filter = new Filter() {
            //Run on background thread
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Expense> filteredList = new ArrayList<>();
                //condition for when search return null
                if (constraint.toString().isEmpty()){
                    //add all list items to filtered list
                    filteredList.addAll(mExpensesAll);
                }
                else{
                    for (Expense expense : mExpensesAll){
                        if (expense.getCategory().toLowerCase().contains(constraint.toString().toLowerCase())){
                            //add matching items to filtered list
                            filteredList.add(expense);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            //Run on UI thread
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mExpenses.clear();
                mExpenses.addAll((Collection<? extends Expense>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
