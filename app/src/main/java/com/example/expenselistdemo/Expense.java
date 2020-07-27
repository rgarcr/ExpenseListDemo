package com.example.expenselistdemo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Expense implements Parcelable {
    private String name;
    private double value;

    public Expense(String name, double value) {
        this.name = name;
        this.value = value;
    }

    public Expense(Parcel input){
        name = input.readString();
        value = input.readDouble();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Expense, name: "
                + name + " value:" + value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(value);
    }

    public static final Parcelable.Creator<Expense> CREATOR
            = new Parcelable.Creator<Expense>() {
        public Expense createFromParcel(Parcel in) {
            Log.d("Parceable.Creator: ", "createFromParcel: Expense");
            return new Expense(in);
        }

        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };
}
