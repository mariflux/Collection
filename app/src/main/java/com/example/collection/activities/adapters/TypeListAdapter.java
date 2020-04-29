package com.example.collection.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.collection.R;
import com.example.collection.activities.FilterListActivity;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;

import java.util.List;

public class TypeListAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> rType;
    String rCategory;
    List<Integer> rCount;
    DBQueries dbQueries;
    DBHelper dbHelper;

    public TypeListAdapter(Context c, String category, List<String> type, List<Integer> count) {
        super(c, R.layout.row_type, R.id.textView1, type);
        this.context = c;
        this.rCount = count;
        this.rType = type;
        this.rCategory = category;
        dbHelper = new DBHelper(context);
        dbQueries = new DBQueries(context);


    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row_type, null);
            viewHolder = new ViewHolder();

            viewHolder.myType = convertView.findViewById(R.id.textView1);
            viewHolder.myCount = convertView.findViewById(R.id.textView3);

            viewHolder.button_see = (Button) convertView
                    .findViewById(R.id.button_see);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);

        viewHolder.myType.setText(rType.get(position));
        viewHolder.myCount.setText(rCount.get(position).toString());

        viewHolder.button_see.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FilterListActivity.class);
                intent.putExtra("category", rCategory);
                intent.putExtra("title", "");
                intent.putExtra("description", "");
                intent.putExtra("type", rType.get(position));
                intent.putExtra("typeEquals", true);
                //if(rCategory.equals(DatabaseHelper.TABLE_NAME_BOOKS)) {
                intent.putExtra("author", "");

                context.startActivity(intent);

            }
        });


        return convertView;
    }


    public class ViewHolder {

        Button button_see;

        TextView myType;
        TextView myCount;
    }
}
