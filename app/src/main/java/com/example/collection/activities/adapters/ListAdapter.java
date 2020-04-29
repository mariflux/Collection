package com.example.collection.activities.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.collection.R;
import com.example.collection.activities.EditActivity;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.model.Item;

import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {
//    customButtonListener customListner;
//
//    public interface customButtonListener {
//        public void onButtonClickListner(int position,String value);
//    }
//
//    public void setCustomButtonListner(customButtonListener listener) {
//        this.customListner = listener;
//    }

    Context context;
    List<String> rTitle;
    String rCategory;
    List<Item> rItem;
    DBQueries dbQueries;
    DBHelper dbHelper;

    public ListAdapter(Context c, List<Item> item, String category, List<String> title) {
        super(c, R.layout.row, R.id.textView1, title);
        this.context = c;
        this.rTitle = title;
        this.rCategory = category;
        this.rItem = item;
        dbHelper = new DBHelper(context);
        dbQueries = new DBQueries(context);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.row, null);
            viewHolder = new ViewHolder();
            viewHolder.myTitle = convertView.findViewById(R.id.textView1);
            viewHolder.myDescription = convertView.findViewById(R.id.textView2);
            viewHolder.myType = convertView.findViewById(R.id.textView3);
            viewHolder.myAuthor = convertView.findViewById(R.id.textView4);
            viewHolder.myAuthor.setVisibility(View.INVISIBLE);
            if (rCategory.equals(DBConstants.TABLE_NAME_BOOKS)) {
                viewHolder.myAuthor.setVisibility(View.VISIBLE);
            }

            viewHolder.button_del = (ImageButton) convertView
                    .findViewById(R.id.button_del);
            viewHolder.button_edit = (ImageButton) convertView
                    .findViewById(R.id.button_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String temp = getItem(position);
        viewHolder.myTitle.setText(rItem.get(position).getTitle());
        viewHolder.myDescription.setText(rItem.get(position).getDescription());
        viewHolder.myType.setText(rItem.get(position).getType());
        if (rCategory.equals(DBConstants.TABLE_NAME_BOOKS)) {
            viewHolder.myAuthor.setText(rItem.get(position).getAuthor());
        }
        viewHolder.button_del.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showMessage("Uwaga!", "Czy na pewno chcesz usunąć " + rTitle.get(position) + "?", rCategory, rItem.get(position).getId(), rTitle.get(position),rItem.get(position));
                //Toast.makeText(context, "" + rTitle.get(position) + " został usunięty", Toast.LENGTH_LONG).show();


            }
        });

        viewHolder.button_edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("category", rCategory);
                intent.putExtra("title", rItem.get(position).getTitle());
                intent.putExtra("description", rItem.get(position).getDescription());
                intent.putExtra("type", rItem.get(position).getType());
                intent.putExtra("id", rItem.get(position).getId());
                if (rCategory.equals(DBConstants.TABLE_NAME_BOOKS)) {
                    intent.putExtra("author", rItem.get(position).getAuthor());

                }

                context.startActivity(intent);


            }
        });

        return convertView;
    }

    public void showMessage(String msgTitle, String msg, final String category, final int id, final String title, final Item item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(msgTitle);
        builder.setMessage(msg);
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dbQueries.open();
                        dbQueries.deleteItem(category, id);
                        //rItem.remove(item);

                        //dbQueries.close();
                        Toast.makeText(context, "" + title + " został usunięty", Toast.LENGTH_LONG).show();

                        dialog.cancel();
                    }
                }

        );

        builder.setNeutralButton("Nie", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.cancel();
                    }
                }
        );
        builder.show();
    }

    public class ViewHolder {

        ImageButton button_del;
        ImageButton button_edit;
        TextView myTitle;
        TextView myDescription;
        TextView myType;
        TextView myAuthor;
    }
}
