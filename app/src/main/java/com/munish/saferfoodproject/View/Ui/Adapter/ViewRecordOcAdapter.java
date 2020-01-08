package com.munish.saferfoodproject.View.Ui.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Models.ViewRecordOcModel;

import org.json.JSONException;

import java.util.List;

public class ViewRecordOcAdapter extends RecyclerView.Adapter<ViewRecordOcAdapter.MyHolder> {
    Context context;
    List<ViewRecordOcModel> viewRecordOcModels;
    String itemName = "";
 /*   Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);

*/
    public ViewRecordOcAdapter(Context context, List<ViewRecordOcModel> viewRecordOcModels) {
        this.context = context;
        this.viewRecordOcModels = viewRecordOcModels;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_oc_adapter, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int i) {
        holder.name.setText(viewRecordOcModels.get(i).getName());
        holder.comment.setText(viewRecordOcModels.get(i).getComment());
        holder.date.setText(viewRecordOcModels.get(i).getDate());
        itemName = "";
        for (int j = 0; j < viewRecordOcModels.get(i).getItems().length(); j++) {
            String temp = "";
            try {
                temp = (j + 1) + ". " + viewRecordOcModels.get(i).getItems().getString(j);
                itemName = itemName + temp + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        holder.items.setText(itemName);
        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                holder.down.setVisibility(View.GONE);
                holder.items.startAnimation(slideDown);
                holder.items.setVisibility(View.VISIBLE);

                holder.up.setVisibility(View.VISIBLE);
            }
        });
        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                holder.up.setVisibility(View.GONE);
                holder.down.setVisibility(View.VISIBLE);
                holder.items.startAnimation(slideUp);
                holder.items.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return viewRecordOcModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, comment, date, items;
        ImageView down,up;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            items = itemView.findViewById(R.id.items);
            down = itemView.findViewById(R.id.down);
            up = itemView.findViewById(R.id.up);

        }
    }
}
