package com.bignerdranch.android.expense;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ExpenseFragment extends Fragment {

    //Declare variables
    private static final String ARG_EXPENSE_ID = "expense_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;
    private static final int REQUEST_CONTACT = 2;
    private static final int REQUEST_PHOTO = 3;

    private Expense mExpense;
    private File mPhotoFile;
    private EditText mAmount;
    private Button mDateButton;
    private Button mTimeButton;
    private Button mContactButton;
    private Button mReportButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Button mDeleteButton;
    private AutoCompleteTextView mAutoCompleteTextView;
    private SimpleDateFormat mDateFormat;
    private SimpleDateFormat mTimeFormat;
    ArrayAdapter<String> mArrayItems;
    private TextInputEditText mDetails;
    private boolean status = false;

    public static ExpenseFragment newInstance(UUID expenseId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_EXPENSE_ID, expenseId);

        ExpenseFragment fragment = new ExpenseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID expenseId = (UUID) getArguments().getSerializable(ARG_EXPENSE_ID);
        mExpense = ExpenseLab.get(getActivity()).getExpense(expenseId);
        mPhotoFile = ExpenseLab.get(getActivity()).getPhotoFile(mExpense);
    }

    @Override
    public void onPause(){
        super.onPause();

        ExpenseLab.get(getActivity())
                .updateExpense(mExpense);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_expense, container, false);

        mAmount = (EditText) v.findViewById(R.id.expense_amount);
        mAmount.setText(mExpense.getAmount());
        //Set default ammount
        if (mExpense.getAmount() == null){
            mExpense.setAmount("0");
        }
        mAmount.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after){
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
                mExpense.setAmount(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                //This one too
            }
        });


        mDetails = (TextInputEditText) v.findViewById(R.id.expense_details);
        mDetails.setText(mExpense.getDetails());
        mDetails.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(
                    CharSequence s, int start, int count, int after){
                //This space intentionally left blank
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count){
                mExpense.setDetails(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s){
                //This one too
            }
        });

        mAutoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.auto_complete_text);
        mArrayItems = new ArrayAdapter<String>(requireContext(), R.layout.dropdown_items, getResources().getStringArray(R.array.expense_categories));
        mAutoCompleteTextView.setAdapter(mArrayItems);
        //Set default amount
        if (mExpense.getCategory() == null){
            mExpense.setCategory("Others");
        }
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //Get position of category list
                String category = parent.getItemAtPosition(position).toString();
                Toast.makeText(getContext().getApplicationContext(), "Category: " + category, Toast.LENGTH_SHORT).show();
                mExpense.setCategory(category);
            }
        });

        mDateButton = (Button) v.findViewById(R.id.expense_date);
        //Format date (e.g. FRIDAY, 15 JULY 2022)
        mDateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mExpense.getDate());
                dialog.setTargetFragment(ExpenseFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTimeButton = (Button) v.findViewById(R.id.expense_time);
        //Formart time (e.g. 12:00 AM)
        mTimeFormat = new SimpleDateFormat("h:mm a");
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mExpense.getDate());
                dialog.setTargetFragment(ExpenseFragment.this, REQUEST_TIME);
                dialog.show(manager, DIALOG_TIME);
            }
        });

        mReportButton = (Button) v.findViewById(R.id.expense_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getExpenseReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.expense_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mContactButton = (Button) v.findViewById(R.id.expense_contact);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mDeleteButton = (Button) v.findViewById(R.id.expense_delete);
        //Create delete confirmation message
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.setTitle("Delete Transaction");
                alert.setMessage("Delete this transaction?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Delete Expense from Database
                        ExpenseLab.get(getActivity()).deleteExpense(mExpense.getId());
                        //Generate Toast Message
                        Toast.makeText(getContext().getApplicationContext(), "Transaction deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog
                        Toast.makeText(getContext().getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });


        if (mExpense.getContact() != null) {
            mContactButton.setText(mExpense.getContact());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            mContactButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.expense_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.expense.fileprovider", mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.expense_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mExpense.setDate(date);
            updateDate();
        }

        else if (requestCode == REQUEST_TIME){
            Date time = (Date) data
                    .getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mExpense.setDate(time);
            updateTime();
        }

        else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            //Specify which fields you want your query to return values for
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            //Perform your query - the contactUri is like a "where" clause here
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);

            try {
                //Double-check that you actually got results
                if (c.getCount() == 0){
                    return;
                }

                //Pull out the first column of the first row of data - that is your suspect's name
                c.moveToFirst();
                String suspect = c.getString(0);
                mExpense.setContact(suspect);
                mContactButton.setText(suspect);
            }
            finally {
                c.close();
            }
        }
        else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(), "com.bignerdranch.android.expense.fileprovider", mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mDateFormat.format(mExpense.getDate()));
    }

    private void updateTime() {
        mTimeButton.setText(mTimeFormat.format(mExpense.getDate()));
    }

    private String getExpenseReport(){
        String dateFormat = "EEEE, MMMM dd";
        String dateString = DateFormat.format(dateFormat, mExpense.getDate()).toString();

        String contact = mExpense.getContact();
        if (contact == null){
            contact = getString(R.string.expense_report_no_contact);
        }
        else {
            contact = getString(R.string.expense_report_contact, contact);
        }

        String report = getString(R.string.expense_report, dateString, mExpense.getAmount(), contact);

        return report;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        }
        else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
