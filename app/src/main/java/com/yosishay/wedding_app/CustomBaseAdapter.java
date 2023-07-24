package com.yosishay.wedding_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class CustomBaseAdapter extends BaseAdapter {
    public interface OnButtonClickListener {
        void onButtonClick(String name);
    }

    Context context;
    String listName[];
    String listImages[];
    String listRating[];

    String listKeys[];
    private OnButtonClickListener buttonClickListener;

    LayoutInflater inflater;
    public CustomBaseAdapter(Context ctx,String [] nameList,String [] ratingList,String[] images,String [] keyList){
        this.context=ctx;
        this.listName=nameList;
        this.listImages=images;
        this.listRating=ratingList;
        inflater=LayoutInflater.from(ctx);
        this.listKeys=keyList;
    }
    @Override
    public int getCount() {
        return listName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_custom_list_view, null);

        // Get references to the views in the custom row layout.
        TextView textView = convertView.findViewById(R.id.textView);
        TextView textView1 = convertView.findViewById(R.id.textView1);
        ImageView supImg = convertView.findViewById(R.id.imageIcon);

        ImageView btn = convertView.findViewById(R.id.button8);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call the button click listener's onButtonClick method and pass the position.
                if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(listKeys[position]);
                }
            }
        });

        // Parse the rating value to a float and format it with one decimal point.
        float rate = Float.parseFloat(listRating[position]);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        String formattedValue = decimalFormat.format(rate);

        // Set the name, formatted rating, and image using the data from the arrays.
        textView.setText(listName[position]);
        textView1.setText("דירוג: " + formattedValue);
        Picasso.get().load(listImages[position]).into(supImg); // Load the image asynchronously.

        // Return the populated view to display the custom row in the ListView.
        return convertView;
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.buttonClickListener = listener;
    }


}
