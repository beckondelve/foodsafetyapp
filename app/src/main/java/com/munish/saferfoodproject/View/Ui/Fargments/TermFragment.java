package com.munish.saferfoodproject.View.Ui.Fargments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.View.Ui.Activities.HomeActivity;

import static android.content.Context.MODE_PRIVATE;

public class TermFragment extends Fragment implements View.OnClickListener {
    public static final String MY_PREFS_NAME = "MyUserDetail";
    private static final String TAG = "TermFragment";
    Button delineBtn, acceptBtn;
    View view;
    String name;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.term_fragment, container, false);
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    Log.e(TAG, "onAuthStateChanged: noii");
                }
            }
        };
        //get data from perivious activity
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
        }

        //initialize the view content
        intialization();

        return view;
    }


    private void intialization() {
        //find Id's
        acceptBtn = view.findViewById(R.id.acceptBtn);
        delineBtn = view.findViewById(R.id.delineBtn);

        //click events
        acceptBtn.setOnClickListener(this);
        delineBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.acceptBtn:
                //user accept the terms
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", name);
                editor.putString("email", user.getEmail());
                editor.putString("id", user.getUid());
                editor.apply();
                editor.commit();
                //Intent to next activity
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
                break;
            case R.id.delineBtn:
                if (user != null) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.e(TAG, "onComplete: yess");
                                        mFirebaseDatabase = mFirebaseInstance.getReference("users-details");
                                        mFirebaseDatabase.child(user.getUid()).removeValue();
                                        getActivity().finish();
                                    } else {
                                        Log.e(TAG, "onComplete: noooo");
                                    }
                                }
                            });
                } else {
                    Log.e(TAG, "onClick: dddddd");
                }
                break;
        }
    }
}
