package com.munish.saferfoodproject.View.Ui.Fargments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.munish.saferfoodproject.Service.Models.IntroRecylerModel;
import com.munish.saferfoodproject.Service.Models.pdfNameModel;
import com.munish.saferfoodproject.View.Ui.Adapter.IntroRecAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.munish.saferfoodproject.View.Ui.Activities.HomeActivity.MY_PREFS_NAME;

public class IntroductionViewer extends Fragment {
    private static final String TAG = "IntroductionViewer";
    //https://choudharymunish24.000webhostapp.com/apple.pdf
    View view;
    WebView webView;
    String name, email, id;
    RecyclerView recyclerIntro;
    ImageView nodata;
    List<IntroRecylerModel> models = new ArrayList<>();
    private DatabaseReference mFireDataReference;
    private FirebaseDatabase mFirebaseInstance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_introduction, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        name = prefs.getString("name", "No name defined");
        email = prefs.getString("email", "No name defined");
        id = prefs.getString("id", "No name defined");
        Log.e(TAG, "onCreate: " + name + " " + email + " " + id);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFireDataReference = mFirebaseInstance.getReference("users-details/" + id + "/pdfDataModel/");


        recyclerIntro = view.findViewById(R.id.recyclerIntro);
        nodata = view.findViewById(R.id.nodata);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerIntro.setLayoutManager(manager);


        return view;
    }

    private void hitService() {
        String url = Constants.BaseUrl +Constants.userDetail+ id + "/pdfDataModel.json";
        Log.e(TAG, "hitService: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    if (response.equals("null")) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerIntro.setVisibility(View.GONE);
                    } else {
                        nodata.setVisibility(View.GONE);
                        recyclerIntro.setVisibility(View.VISIBLE);

                        models.clear();
                    Log.e(TAG, "onResponse: " + response);

                    JSONObject jsonObject = new JSONObject(response);
                    Iterator<String> iterator = jsonObject.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        JSONObject object = jsonObject.getJSONObject(key);
                        IntroRecylerModel model = new IntroRecylerModel(object.getString("name"), object.getString("url"), object.getString("date"));
                        models.add(model);
                        IntroRecAdapter introRecAdapter = new IntroRecAdapter(getActivity(), models);
                        recyclerIntro.setAdapter(introRecAdapter);

                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error );

            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: jjklkddd");
        Log.e(TAG, "onResume: " + Constants.checkResume);
        if (Constants.checkResume == 0) {
            hitService();
            //get download folder path
            ArrayList<String> mFileNames;
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
            Log.e(TAG, "selectItem: " + path);
            File file = new File(path);
            mFileNames = new ArrayList<String>();
            File filesInDirectory[] = file.listFiles();
            if (filesInDirectory != null) {
                for (int a = 0; a < filesInDirectory.length; a++) {
                    mFileNames.add(filesInDirectory[a].getName());
                }
                Constants.arrayList = new ArrayList<>();
                Constants.arrayList = mFileNames;
            }
        } else {
            Constants.checkResume = 0;
            try {
                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                Log.e(TAG, "onResume: " + mydate);
                checkFileData(mydate);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
    }
//get new file from folder
    private void checkFileData(final String time) throws IndexOutOfBoundsException {
        ArrayList<String> mFileNames;
        String path = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;
        Log.e(TAG, "selectItem: " + path);
        File file = new File(path);
        mFileNames = new ArrayList<String>();
        File filesInDirectory[] = file.listFiles();
        if (filesInDirectory != null) {
            for (int a = 0; a < filesInDirectory.length; a++) {
                mFileNames.add(filesInDirectory[a].getName());
            }
        }

        ArrayList<String> uniqueArr = new ArrayList<>();
        final ArrayList<String> duplicateArr = new ArrayList<>();
        for (int i = 0; i < mFileNames.size(); i++) {
            if (!duplicateArr.contains(mFileNames.get(i))) {
                uniqueArr.add(mFileNames.get(i));
                duplicateArr.add(mFileNames.get(i));
            } else {
                uniqueArr.remove(mFileNames.get(i));
            }
        }
        for (int j = 0; j < Constants.arrayList.size(); j++) {
            if (!duplicateArr.contains(Constants.arrayList.get(j))) {
                uniqueArr.add(Constants.arrayList.get(j));
                duplicateArr.add(Constants.arrayList.get(j));
            } else {
                uniqueArr.remove(Constants.arrayList.get(j));
            }
        }

        Log.e(TAG, "selectItem: " + uniqueArr);

        //found unique value
        final String s = uniqueArr.get(0);

        //upload to firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://safer-food.appspot.com");

        Uri file1 = Uri.fromFile(new File("/storage/emulated/0/Download/" + s));
        Log.e("file", file1.getPath());


        final StorageReference riversRef = storageRef.child(s);

        UploadTask uploadTask = riversRef.putFile(file1);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("uploadFail", "" + exception);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.e(TAG, "onSuccess: " + uri);
                        String downloadUrl = uri.toString();

                        //register on runtime database
                        pdfNameModel pdfData = new pdfNameModel(s, downloadUrl, time);
                        String key = mFireDataReference.push().getKey();
                        Map<String, Object> map = new HashMap<>();
                        map.put(key, pdfData);


                        mFireDataReference.updateChildren(map);
                        addUserChangeListener();
                        //  childChangeListener(id);

                    }
                });
            }
        });

    }

    private void childChangeListener(String id) {

    }


    public void addUserChangeListener() {
        // User data change listener
        mFireDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear model and call resume method
                models.clear();
                onResume();

                Log.e(TAG, "onDataChange: " + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());

            }
        });

    }
}
