package com.example.test3hit.models;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.example.test3hit.R;
import java.util.ArrayList;

public class ShoppingListAdapter extends ArrayAdapter<ShoppingItem> {



    public ShoppingListAdapter(Context context, ArrayList<ShoppingItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShoppingItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_list_item, parent, false);
        }


        TextView itemName = convertView.findViewById(R.id.itemName);
        CheckBox itemCheckBox = convertView.findViewById(R.id.itemCheckBox);
        Button deleteButton = convertView.findViewById(R.id.deleteButton);

        itemName.setText(item.getName());
        itemCheckBox.setChecked(item.isChecked());

        if (item.isChecked()) {
            itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        itemCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setChecked(isChecked);
            if (isChecked) {
                itemName.setPaintFlags(itemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                itemName.setPaintFlags(itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });

        itemName.setOnClickListener(v -> {
            // Handle tap to edit
            EditText editText = new EditText(getContext());
            editText.setText(item.getName());
            new AlertDialog.Builder(getContext())
                    .setTitle("Edit Item")
                    .setView(editText)
                    .setPositiveButton("Save", (dialog, which) -> {
                        item.setName(editText.getText().toString());
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        deleteButton.setOnClickListener(v -> {
            remove(item);
            notifyDataSetChanged();
        });

        return convertView;
    }
}