package com.munish.saferfoodproject.View.Ui.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.View.Ui.Fargments.LoginFragment;

public class ContainerLogin extends AppCompatActivity {
// just a fragment container class
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_login);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.container, new LoginFragment());
        ft.commit();
    }
}
