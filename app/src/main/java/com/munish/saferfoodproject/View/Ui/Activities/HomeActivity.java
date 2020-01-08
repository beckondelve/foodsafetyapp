package com.munish.saferfoodproject.View.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.munish.saferfoodproject.R;
import com.munish.saferfoodproject.Service.Models.DrawerModel;
import com.munish.saferfoodproject.Service.Models.User;
import com.munish.saferfoodproject.View.Ui.Adapter.DrawerAdapter;
import com.munish.saferfoodproject.View.Ui.Fargments.IntroductionViewer;
import com.munish.saferfoodproject.View.Ui.Fargments.OcFoodSafety;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MY_PREFS_NAME = "MyUserDetail";
    private static final String TAG = "HomeActivity";
    private String[] mNavigationDrawerItemTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    ImageView toggleBtn,plus;
    TextView view_all,title;
    ActionBarDrawerToggle mDrawerToggle;
    private FirebaseAuth auth;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    String name,email,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // get from SharedPreferences

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
         name = prefs.getString("name", "No name defined");
         email = prefs.getString("email", "No name defined");
         id = prefs.getString("id", "No name defined");
        Log.e(TAG, "onCreate: "+name +" "+email+" "+id );

        intailization();

        setUpDrawer();
        loadFirstFragment();
    }


    private void intailization() {
        //mNavigationDrawerItemTitles= getResources().getStringArray(R.array.drawer_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        toggleBtn=findViewById(R.id.back);
        toggleBtn.setImageResource(R.drawable.menu_button);
        plus=findViewById(R.id.plus);
        view_all=findViewById(R.id.view_all);
        title=findViewById(R.id.title);

        auth = FirebaseAuth.getInstance();
        mFirebaseInstance = FirebaseDatabase.getInstance();


        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users-details/"+id+"/");

        toggleBtn.setOnClickListener(this);
        plus.setOnClickListener(this);
        view_all.setOnClickListener(this);


    }

    private void setUpDrawer() {
        DrawerModel[] drawerItem = new DrawerModel[7];
        drawerItem[0] = new DrawerModel(R.drawable.house, "Home");
        drawerItem[1] = new DrawerModel(R.drawable.open, "Opening Checks - Food Safety");
        drawerItem[2] = new DrawerModel(R.drawable.diagram, "FLOW DIAGRAM");
        drawerItem[3] = new DrawerModel(R.drawable.chart, "HACCP CHARTS");
        drawerItem[4] = new DrawerModel(R.drawable.rules, "HOUSE RULES");
        drawerItem[5] = new DrawerModel(R.drawable.document, "RECORDS");
        drawerItem[6] = new DrawerModel(R.drawable.signout, "Sign Out");

        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.view_drawer, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                try {
                    if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                        mDrawerLayout.openDrawer(Gravity.LEFT);
                    else if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
                    } mDrawerLayout.closeDrawer(Gravity.RIGHT);
                }catch (IllegalArgumentException e){
                    Log.e(TAG, "onClick: "+e );
                }
                break;
            case R.id.plus:
                Intent i = new Intent(HomeActivity.this, PdfViewActivity.class);
                i.putExtra("url", "https://beckon001.000webhostapp.com/flow_diafram_main.pdf");
                startActivity(i);
                break;
            case R.id.view_all:
                startActivity(new Intent(HomeActivity.this,ViewRecordOcActivity.class));
                break;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    private void selectItem(int position) {
        switch (position){
            case 0:
                title.setText("Homepage");
                view_all.setVisibility(View.GONE);
                plus.setVisibility(View.VISIBLE);
                loadFirstFragment();
                break;
            case 1:
                title.setText("Opening Checks - Food Safety");
                view_all.setVisibility(View.VISIBLE);
                plus.setVisibility(View.GONE);
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame, new OcFoodSafety());
                ft.commit();
                break;

            case 2:
                Toast.makeText(this, "Functionality Closed For Security Issues", Toast.LENGTH_SHORT).show();
                break;

                // cases may delete for Security Issues

            case 6:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Logout");
                alertDialogBuilder
                        .setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                auth.signOut();
                                startActivity(new Intent(HomeActivity.this,ContainerLogin.class));
                                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.clear();
                                editor.commit();
                                finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();




        }

    }

    private void loadFirstFragment() {
        FragmentManager fm1 = getSupportFragmentManager();
        FragmentTransaction ft1 = fm1.beginTransaction();
        ft1.replace(R.id.content_frame, new IntroductionViewer());
        ft1.commit();
    }


    private void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

}
