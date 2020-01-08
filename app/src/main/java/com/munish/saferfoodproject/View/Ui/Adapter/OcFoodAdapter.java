package com.munish.saferfoodproject.View.Ui.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.munish.saferfoodproject.Interface.LongPressOcFood;
import com.munish.saferfoodproject.Interface.OcFoodInterface;
import com.munish.saferfoodproject.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class OcFoodAdapter extends RecyclerView.Adapter<OcFoodAdapter.MyHolder> {
    FragmentActivity activity;
    JSONArray array;
    ArrayList<String> checked_item_name=new ArrayList<>();
    OcFoodInterface ocFoodInterface;
    LongPressOcFood longPressOcFood;
    public OcFoodAdapter(FragmentActivity activity, JSONArray array, OcFoodInterface ocFoodInterface, LongPressOcFood longPressOcFood) {
        this.activity = activity;
        this.array = array;
        this.ocFoodInterface = ocFoodInterface;
        this.longPressOcFood = longPressOcFood;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(activity);
        View view=inflater.inflate(R.layout.view_oc_food,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        try {
            final String item_name=array.getString(position);
            holder.item.setText(item_name);
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        checked_item_name.add(item_name);
                        holder.checkbox.setChecked(isChecked);
                        ocFoodInterface.onClick(checked_item_name);
                    } else {
                        checked_item_name.remove(item_name);
                        holder.checkbox.setChecked(isChecked);
                        ocFoodInterface.onClick(checked_item_name);
                    }
                }
            });

            holder.ll1.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holder.ll1.setVisibility(View.GONE);
                    holder.ll2.setVisibility(View.VISIBLE);
                    holder.updateEt.setText(holder.item.getText());
                    return false;
                }
            });
            holder.updateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    longPressOcFood.onClick(view,position,holder.updateEt.getText().toString());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return array.length();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView item;
        CheckBox checkbox;
        LinearLayout ll1,ll2;
        EditText updateEt;
        Button updateBtn;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            item=itemView.findViewById(R.id.item);
            checkbox=itemView.findViewById(R.id.checkbox);
            ll1=itemView.findViewById(R.id.ll1);
            ll2=itemView.findViewById(R.id.ll2);
            updateEt=itemView.findViewById(R.id.updateEt);
            updateBtn=itemView.findViewById(R.id.updateBtn);
        }
    }
}
