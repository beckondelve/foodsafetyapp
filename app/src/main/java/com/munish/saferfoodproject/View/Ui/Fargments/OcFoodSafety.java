package com.munish.saferfoodproject.View.Ui.Fargments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.munish.saferfoodproject.Interface.LongPressOcFood;
import com.munish.saferfoodproject.Interface.OcFoodInterface;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.munish.saferfoodproject.Service.Models.OcFoodSafetyModel;
import com.munish.saferfoodproject.View.Ui.Adapter.OcFoodAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.munish.saferfoodproject.View.Ui.Activities.HomeActivity.MY_PREFS_NAME;

public class OcFoodSafety extends Fragment {
    private static final String TAG = "OcFoodSafety";
    View view;
    RecyclerView recycler;
    OcFoodInterface ocFoodInterface;
    LongPressOcFood longPressOcFood;
    EditText name,comment,itemName;
    TextView addItem;
    ArrayList<String> arrayItem;
    Button done,saveNewItem;
    private DatabaseReference mFireDataReference;
    private DatabaseReference mFireDataReference1;
    private FirebaseDatabase mFirebaseInstance;
    int indexNewItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.oc_food_safety, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("id", "No name defined");
        intialization(view);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFireDataReference = mFirebaseInstance.getReference("users-details/" + id + "/OcFoodSafety/");
        mFireDataReference1 = mFirebaseInstance.getReference("food_safety_data");

        arrayItem=new ArrayList<>();
        ocFoodInterface=new OcFoodInterface() {
            @Override
            public void onClick(ArrayList<String> arrayList) {
                Log.e(TAG, "onClick: "+arrayList );
                arrayItem=arrayList;

            }
        };
        longPressOcFood=new LongPressOcFood() {
            @Override
            public void onClick(View v, int postition, String name) {
                Log.e(TAG, "onClick: "+name+" " +postition );

                Map<String, Object> map = new HashMap<>();
                map.put(""+postition, name);

                mFireDataReference1.updateChildren(map);
                loadFragment();
            }
        };


// clicks events
        click();
        
        return view;
    }
    private void intialization(View v) {
        //find id's from xml
        name=v.findViewById(R.id.name);
        comment=v.findViewById(R.id.comment);
        done=v.findViewById(R.id.done);
        saveNewItem=v.findViewById(R.id.saveNewItem);
        itemName=v.findViewById(R.id.itemName);
        addItem=v.findViewById(R.id.addItem);
        recycler=v.findViewById(R.id.recycler);
        LinearLayoutManager  manager=new LinearLayoutManager(getContext());
        recycler.setLayoutManager(manager);

    }
    private void hitService() {
        //get data from firebase
    String url= Constants.BaseUrl+"food_safety_data"+Constants.json;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array=new JSONArray(response);
                    indexNewItem=array.length();
                    OcFoodAdapter adapter=new OcFoodAdapter(getActivity(),array,ocFoodInterface,longPressOcFood);
                    recycler.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        itemName.setVisibility(View.GONE);
        itemName.setText("");
        saveNewItem.setVisibility(View.GONE);
        hitService();
    }

    private void click() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

                if(name.getText().length()>4){
                    if(comment.getText().length()>10){
                        if(!arrayItem.toString().equals("[]")){
                            OcFoodSafetyModel model=new OcFoodSafetyModel(name.getText().toString(),comment.getText().toString(),mydate,arrayItem);

                            String key = mFireDataReference.push().getKey();
                            Map<String, Object> map = new HashMap<>();
                            map.put(key, model);


                            mFireDataReference.updateChildren(map);
                            //addUserChangeListener();
                            loadFragment();



                        }else{
                            Toast.makeText(getActivity(), "Please add atleast one item", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        comment.setError("Please enter valid comment");
                    }

                }else{
                    name.setError("Enter valid name");
                }
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemName.setVisibility(View.VISIBLE);
                saveNewItem.setVisibility(View.VISIBLE);
            }
        });
        saveNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemName.getText().length()>5){
                    Map<String, Object> map = new HashMap<>();
                    map.put(""+indexNewItem, itemName.getText().toString());


                    mFireDataReference1.updateChildren(map);
                    onResume();
                    //addUserChangeListener();

                }else{
                    Toast.makeText(getActivity(), "Enter vaild item name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUserChangeListener() {
        // User data change listener
        mFireDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e(TAG, "onDataChange: " + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());

            }
        });

    }
   void loadFragment(){
        FragmentManager fm =getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content_frame, new OcFoodSafety());
        ft.commit();

    }
}
