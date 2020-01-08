package com.munish.saferfoodproject.View.Ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Models.IntroRecylerModel;
import com.munish.saferfoodproject.View.Ui.Activities.WebViewActivity;

import java.util.List;

public class IntroRecAdapter extends RecyclerView.Adapter<IntroRecAdapter.MyHolder> {
    Context context;
    List<IntroRecylerModel> models;

    public IntroRecAdapter(Context context, List<IntroRecylerModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.view_intro_adapter,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder,final int i) {
        holder.name.setText(models.get(i).getName());
        holder.date.setText(models.get(i).getDate());
        holder.viewPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, WebViewActivity.class);
                intent.putExtra("name",models.get(i).getName());
                intent.putExtra("url",models.get(i).getUrl());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name,date;
        ImageView viewPdf;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
        name=itemView.findViewById(R.id.name);
        date=itemView.findViewById(R.id.date);
        viewPdf=itemView.findViewById(R.id.viewPdf);
        }
    }
}
