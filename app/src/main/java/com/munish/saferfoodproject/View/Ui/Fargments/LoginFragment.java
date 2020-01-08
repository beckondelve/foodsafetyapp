package com.munish.saferfoodproject.View.Ui.Fargments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
//ecem telli
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.munish.saferfoodproject.View.Ui.Activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

// netropidia , esace , elemnent

import static android.content.Context.MODE_PRIVATE;
import static com.munish.saferfoodproject.View.Ui.Activities.HomeActivity.MY_PREFS_NAME;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    TextView btn_signup;
    EditText edit_email, edit_pwd;
    Button btn_login;
    View view;
    private FirebaseAuth auth;
    ProgressDialog myDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
        auth = FirebaseAuth.getInstance();
        intialization();
        click();
        return view;
    }


    private void intialization() {
        btn_login = view.findViewById(R.id.btn_login);
        btn_signup = view.findViewById(R.id.btn_signup);
        edit_email = view.findViewById(R.id.edit_email);
        edit_pwd = view.findViewById(R.id.edit_pwd);
    }

    private void click() {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                ft.replace(R.id.container, new RegisterFragment());
                ft.commit();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edit_email.getText().toString();
                final String password = edit_pwd.getText().toString();
                if (email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    if (password.length() > 5) {
                        myDialog=Constants.showProgressDialog(getActivity(),"Please Wait..");

                        loginMethod(email,password);

                    } else {
                        edit_pwd.setError("Enter valid password");
                    }
                } else {
                    edit_email.setError("Invalid Email Address");
                }

            }
        });
    }

    private void loginMethod(String email, String password) {
        //authenticate user
        Log.e(TAG, "loginMethod: "+email+password );
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        myDialog.dismiss();
                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithEmail:success");
                            getUserFromData(task.getResult().getUser().getUid());
                       } else {

                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void getUserFromData(final String uid) {
        //get data from firebase database
        String url= Constants.BaseUrl+Constants.userDetail+uid+"/user"+Constants.json;
        Log.e(TAG, "getUserFromData: "+url);
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "onResponse: "+response );
                try {
                    JSONObject object=new JSONObject(response);
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("name", object.getString("fullName"));
                    editor.putString("email", object.getString("emailAddress"));
                    editor.putString("id", uid);
                    editor.apply();
                    editor.commit();
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();
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
        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);

    }
}
