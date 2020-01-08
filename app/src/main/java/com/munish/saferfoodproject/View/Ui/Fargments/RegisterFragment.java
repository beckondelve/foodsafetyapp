package com.munish.saferfoodproject.View.Ui.Fargments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Constants;
import com.munish.saferfoodproject.Service.Models.MainModel;
import com.munish.saferfoodproject.Service.Models.PermiseData;
import com.munish.saferfoodproject.Service.Models.User;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";
    TextView title, txt_alreadyregistered;
    ImageView back,plus;
    View view;
    String userId, fullName = "", email = "", password = "";
    ProgressDialog myDialog;
    private EditText firstName, lastName, emailTxt, passTxt, passConTxt;
    private Button btnSignIn, btn_signup, btnResetPassword;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.register_fragment, container, false);
        //initialize the view content
        initialization();

        //firebase authentication instance(Firebase)
        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node(Firebase)
        mFirebaseDatabase = mFirebaseInstance.getReference("users-details");
        // store app title to 'app_title' node(Firebase)
        mFirebaseInstance.getReference("app_title").setValue("Just-meals");
        // app_title change listener(Firebase)
        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "App title updated" + dataSnapshot.getValue(String.class));
                title.setText("Register On Safer-Food");
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read app title value.", error.toException());
            }
        });
        return view;
    }


    private void initialization() {
        //finds ids for view
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        emailTxt = view.findViewById(R.id.emailTxt);
        passTxt = view.findViewById(R.id.passTxt);
        passConTxt = view.findViewById(R.id.passConTxt);
        plus = view.findViewById(R.id.plus);
        back = view.findViewById(R.id.back);
        btn_signup = view.findViewById(R.id.btn_signup);
        title = view.findViewById(R.id.title);
        txt_alreadyregistered = view.findViewById(R.id.txt_alreadyregistered);

        //Visibility gone
        plus.setVisibility(View.GONE);

        //activate clicks events
        back.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        txt_alreadyregistered.setOnClickListener(this);

    }


    private void createUser() {
        //crate user in authentication section(Firebase)
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        myDialog.dismiss();

                        try {
                            if (!task.isSuccessful()) {
                                //If task is not successful(Firebase)
                                Toast.makeText(getActivity(), "" + getString(R.string.err), Toast.LENGTH_SHORT).show();
                            } else {
                                //get User UID(Firebase)
                                userId = task.getResult().getUser().getUid();
                                Log.e(TAG, "onComplete: yes" + task.getResult().getUser().getUid());
                                //crate user in database section(Firebase)
                                genrateUser(task.getResult().getUser().getUid(), fullName, email);
                            }
                        } catch (RuntimeExecutionException e) {
                            Toast.makeText(getActivity(), "" + getString(R.string.email_match), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void genrateUser(String uid, String fullName, String email) {
        //Permise Data Model for Permise Array
        PermiseData data = new PermiseData("Beckon", "New Jersey");
        //Add user details
        User user = new User(fullName, email);
        //main model add on database (Firebase)
        MainModel mainModel=new MainModel(user,data);
        //mFirebase is Reference or location to store data
        mFirebaseDatabase.child(uid).setValue(mainModel);
        //change listener to notify database
        addUserChangeListener(uid);
    }

    private void addUserChangeListener(final String id) {
        // User data change listener
        mFirebaseDatabase.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                // Check for null
                if (user == null) {
                    Toast.makeText(getActivity(), "" + getString(R.string.err), Toast.LENGTH_SHORT).show();
                } else {
                    //load next Activity
                    TermFragment fragment = new TermFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name", fullName);
                    fragment.setArguments(bundle);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction().addToBackStack(null);
                    ft.replace(R.id.container,fragment);
                    ft.commit();
                }

                //      Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
                Toast.makeText(getActivity(), "" + getString(R.string.net), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                getActivity().onBackPressed();
                break;
            case R.id.btn_signup:
                fullName = firstName.getText().toString().trim() + " " + lastName.getText().toString().trim();
                email = emailTxt.getText().toString().trim();
                password = passTxt.getText().toString().trim();
                if (!fullName.equals("") && fullName.length() > 4) {
                    if (email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                        if (password.length() > 5) {
                            if (password.equals(passConTxt.getText().toString().trim())) {
                                myDialog=Constants.showProgressDialog(getActivity(),"Please Wait..");
                                createUser();
                            } else {
                                passConTxt.setError("Password doesn't match");
                            }
                        } else {
                            passTxt.setError("Enter Password upto 6 digit");
                        }
                    } else {
                        emailTxt.setError("Invalid Email Address");
                    }
                } else {
                    lastName.setError("Enter valid name");
                }
                break;
            case R.id.txt_alreadyregistered:
                getActivity().onBackPressed();
                break;
        }
    }
}
