package com.bignerdranch.android.expense;

import java.util.Date;
import java.util.UUID;

public class Expense {

    private UUID mId;
    private String mAmount;
    private String mCategory;
    private String mDetails;
    private Date mDate;
    private String mContact;

    public Expense(){
        this(UUID.randomUUID());
    }

    public Expense(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getAmount() {
        return mAmount;
    }

    public void setAmount(String amount) {
        mAmount = amount;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getContact(){
        return mContact;
    }

    public void setContact(String contact){
        mContact = contact;
    }

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}
