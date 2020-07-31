package com.example.expenselistdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    private final int layoutResource;
    private List<Expense> expenseList;
    private final LayoutInflater layoutInflater;

    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull List<Expense> expenseList) {
        super(context, resource, expenseList);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.expenseList = expenseList;
    }

    @Override
    public int getCount() { return expenseList.size(); }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //declare the ViewHolder object and decide when to create it
        ViewHolder viewHolder;
        //convertView is the view in the list, but if it hasn't been created yet then we need to do so
        if(convertView == null){
            //inflate the view with a resource (the xml file expense list), View group (parent which is the ViewGroup on top in expense list),
            // and boolean to...
            convertView = layoutInflater.inflate(layoutResource,parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        final Expense expense = expenseList.get(position);
        viewHolder.txtName.setText(expense.getName());
        //viewHolder.txtValue.setText(String.valueOf(expense.getValue()));
        viewHolder.txtValue.setText(String.format(Locale.getDefault(),"%8.2f",expense.getValue()));
        //viewHolder.btnDelete.setTag(position);
        viewHolder.btnDelete.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseList.remove(position);
                notifyDataSetChanged();
                
            }
        });
        return convertView;
    }


    private class ViewHolder{
        final TextView txtName;
        final TextView txtValue;
        final Button btnDelete;
        public ViewHolder(View view){
            this.txtName = view.findViewById(R.id.item_txt_name);
            this.txtValue = view.findViewById(R.id.item_txt_value);
            this.btnDelete = view.findViewById(R.id.btn_delete_expense);
        }
    }
}
