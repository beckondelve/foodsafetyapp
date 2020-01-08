package com.munish.saferfoodproject.View.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.munish.saferfoodproject.Service.Models.ViewRecordOcModel;
import com.munish.saferfoodproject.View.Ui.Adapter.ViewRecordOcAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.munish.saferfoodproject.View.Ui.Activities.HomeActivity.MY_PREFS_NAME;

public class ViewRecordOcActivity extends AppCompatActivity {
    ImageView plus,back,nodata;
    TextView title;
    RecyclerView recyclerIntro;
    List<ViewRecordOcModel> viewRecordOcModels=new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record_oc);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        intialization();
        // get data from database
        hitService(prefs.getString("id", "No name defined"));
    }

    private void hitService(String id) {
        String url= Constants.BaseUrl+Constants.userDetail+id+"/OcFoodSafety"+Constants.json;

        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.equals("null")) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerIntro.setVisibility(View.GONE);
                    } else {
                        // set into models object
                        nodata.setVisibility(View.GONE);
                        recyclerIntro.setVisibility(View.VISIBLE);
                        JSONObject object = new JSONObject(response);
                        Iterator<String> iterator = object.keys();
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            JSONObject jo = object.getJSONObject(key);
                            ViewRecordOcModel ocModel=new ViewRecordOcModel();
                            ocModel.setName(jo.getString("name"));
                            ocModel.setComment(jo.getString("comment"));
                            ocModel.setDate(jo.getString("time"));
                            ocModel.setItems(jo.getJSONArray("strings"));
                            viewRecordOcModels.add(ocModel);
                        }
                        ViewRecordOcAdapter adapter=new ViewRecordOcAdapter(getApplicationContext(),viewRecordOcModels);
                        recyclerIntro.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
      Volley.newRequestQueue(this).add(request);
    }

    private void intialization() {
        title=findViewById(R.id.title);
        title.setText("View Record");
        back=findViewById(R.id.back);
        plus=findViewById(R.id.plus);
        plus.setVisibility(View.GONE);
        recyclerIntro = findViewById(R.id.recyclerIntro);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        recyclerIntro.setLayoutManager(manager);
        nodata = findViewById(R.id.nodata);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
