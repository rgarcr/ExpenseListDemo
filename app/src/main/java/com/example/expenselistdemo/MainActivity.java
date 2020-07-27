package com.example.expenselistdemo;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView total_txt, total_lbl;
    private EditText name_txt, value_txt;
    private ArrayList<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name_txt = (EditText)findViewById(R.id.edit_name);
        value_txt = (EditText)findViewById(R.id.edit_value);
        total_lbl = (TextView)findViewById(R.id.total_lbl);
        total_txt = (TextView)findViewById(R.id.total_txt);
        ListView listView = (ListView) findViewById(R.id.list_expense);

        if (savedInstanceState != null) {
            expenseList = savedInstanceState.getParcelableArrayList("list");
            total_lbl.setVisibility(savedInstanceState.getInt("summary_state"));
            total_txt.setText(savedInstanceState.getString("total"));
        } else {
            expenseList = new ArrayList<>();
            loadExpense();
        }
        expenseAdapter = new ExpenseAdapter(this, R.layout.expense_item, expenseList);
        listView.setAdapter(expenseAdapter);
    }

//creates and stores an Expense object into an ArrayList
    public void addExpense(View view) {
        String name = name_txt.getText().toString();
        if(name.length() < 1) {
            name_txt.setError("please enter a value");
            Log.d("MAIN", "name_text is empty");
        }
        else {
            try{
                String stringValue = String.format(Locale.getDefault(),"%.2f",Double.parseDouble(value_txt.getText().toString()));
                double doubleValue = Double.parseDouble(stringValue);

                Expense expense = new Expense(name, doubleValue);
                expenseAdapter.add(expense);
                //saveExpense(name, stringValue);

            }
            catch (NumberFormatException e){
                e.printStackTrace();
                Log.d("InputMismatch", "addExpense: " + e);
                value_txt.setError("please enter a digit");
            }


        }
        name_txt.setText("");
        value_txt.setText("");
        value_txt.clearFocus();
        name_txt.clearFocus();

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("list",expenseList);
        outState.putInt("summary_state",total_lbl.getVisibility());
        outState.putString("total",total_txt.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void expenseSum(View view){
        double sum = 0;
        for(Expense e: expenseList){
            sum += e.getValue();
            Log.d("MAIN", "expenseloop: " + e.toString());
        }
        Log.d("MAIN", "expenseSum: " + sum);
        total_lbl.setVisibility(View.VISIBLE);
        total_txt.setText("Expenses: " + expenseList.size() + " Cost: $" + String.valueOf(sum));
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveExpense();
        Log.d(TAG, "onPause: called");
    }

    public void saveExpense (){
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = openFileOutput("expenseList.txt", Context.MODE_PRIVATE);
            for(int i = 0; i < expenseList.size(); i++) {
                fileOutputStream.write(expenseList.get(i).getName().getBytes());
                fileOutputStream.write(" ".getBytes());
                fileOutputStream.write(String.valueOf(expenseList.get(i).getValue()).getBytes());
                fileOutputStream.write(" ".getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //close the file in finally incase the other statements are caught in an error and the close statement doesn't execute
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//end finally
    }//end method

    //Can we make this a boolean and test if the file exsits so we can decide if we should get the data or not
    public void loadExpense(){
        //incase the file doesn't exist throw the statemnts in a try catch block, protects the app from crashing
        try {
            FileInputStream fileInputStream = openFileInput("expenseList.txt");
            int read = -1;
            StringBuilder stringBuilder = new StringBuilder();
            //read() Returns int the next byte of data, or -1 if the end of the file is reached.
            int count = 0; //track the letter position in the StringBuilder
            int start = 0; //track the start of a word
            String name = null, temp;
            double money = 0;
            while((read = fileInputStream.read())!=-1){
                stringBuilder.append((char)read);
                //check if the string ha
                if(stringBuilder.charAt(count) == ' '){
                    temp = stringBuilder.substring(start, count);
                    if(!(temp.contains("."))){
                        name = temp;
                    }
                    else {
                        money = Double.parseDouble(temp);
                        Log.d(TAG, "loadExpense: number" + money);
                        expenseList.add(new Expense(name, money));
                    }
                    Log.d(TAG, "loadExpense: in loop: " + name);
                  //  Log.d(TAG, "loadExpense: Start" + start + "count" + count);
                    start += temp.length();
                }
                count++;

            }
        } catch (FileNotFoundException e){
            Log.d(TAG, "LoadExpense: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end loadExpense()
}