package com.munish.saferfoodproject.View.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import com.munish.saferfoodproject.R;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.munish.saferfoodproject.View.Ui.Activities.HomeActivity.MY_PREFS_NAME;

public class SplashScreen extends AppCompatActivity {
    final private  int RequestPermissionCode=11;
    SharedPreferences prefs;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
       // get from SharedPreferences
        prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        id = prefs.getString("id", "");

        Rq();

        }
        public void Rq() {
        //checking permission
            if (CheckingPermissionIsEnabledOrNot()) {
                new Handler().postDelayed(new Runnable() {


                    @Override
                    public void run() {
                        if(id.equals("")) {
                            // This method will be executed once the timer is over
                            Intent i = new Intent(SplashScreen.this, ContainerLogin.class);
                            startActivity(i);
                            finish();
                        }else{
                            Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }, 2500);
            }

            // If, If permission is not enabled then else condition will execute.
            else {

                //Calling method to enable permission.
                RequestMultiplePermission();

            }
        }

        public boolean CheckingPermissionIsEnabledOrNot() {

            int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return FirstPermissionResult == PackageManager.PERMISSION_GRANTED;
        }

        private void RequestMultiplePermission() {
            ActivityCompat.requestPermissions(SplashScreen.this, new String[]
                    {
                            WRITE_EXTERNAL_STORAGE
                    }, RequestPermissionCode);

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch (requestCode) {

                case RequestPermissionCode:

                    if (grantResults.length > 0) {
                        boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                        if (CameraPermission) {
                            new Handler().postDelayed(new Runnable() {


                                @Override
                                public void run() {
                                    if(id.equals("")) {
                                        // This method will be executed once the timer is over
                                        Intent i = new Intent(SplashScreen.this, ContainerLogin.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                        Intent i = new Intent(SplashScreen.this, HomeActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }, 2500);
                        } else {
                            /*Toast.makeText(SplashScreen.this,"Permission Denied",Toast.LENGTH_LONG).show();*/
                            Rq();

                        }
                    }
                    break;
            }
    }
}
