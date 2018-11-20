package com.farhakhan.majulams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class FacultyMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {


    public String person_email, person_name, person_pic;
    public String strDate, strDay;
    public String empFaculty, empDepartment, empDomain;
    public CircleImageView NavCmi;
    public CircleImageView MainCmi;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    public  FabSpeedDial fabSpeedDial;
    public FrameLayout frameLayout, container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        fabSpeedDial = findViewById(R.id.fab_speed_dial);
        frameLayout= findViewById(R.id.fab_bkg);
        container = findViewById(R.id.container_faculty);
        Intent intent = getIntent();
        if (intent != null) {
            person_email = intent.getStringExtra("EmailID");
            person_email=person_email.replace(",",".");
            person_name = intent.getStringExtra("Name");
            person_pic = intent.getStringExtra("Picture");
            empFaculty = intent.getStringExtra("Faculty");
            empDepartment = intent.getStringExtra("Department");
            empDomain = intent.getStringExtra("Domain");

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy ");
            strDate = dateFormat.format(calendar.getTime());
            TextView txt_date= findViewById(R.id.Faculty_Date);
            txt_date.setText(strDate);
            SimpleDateFormat day = new SimpleDateFormat("EEEE");
            strDay=day.format(calendar.getTime());
            TextView txt_day = findViewById(R.id.Faculty_Day);
            txt_day.setText(strDay);


            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

            MainCmi = findViewById(R.id.user_pic_faculty);
            Glide.with(FacultyMainActivity.this)
                    .load(person_pic)
                    .apply(new RequestOptions().override(100, 100))
                    .apply(new RequestOptions().centerCrop())
                    .into(MainCmi);

            TextView tvName = findViewById(R.id.txt_FName);
            tvName.setText(person_name);
            TextView tvFaculty = findViewById(R.id.txt_FFac);
            if(empFaculty.equals("FOCE")) {
                String foce = "Faculty of Computing & Engineering";
                tvFaculty.setText(foce);
            }
            else if (empFaculty.equals("FOLS"))
            {
                String fols= "Faculty of Life Sciences";
                tvFaculty.setText(fols);
            }
            else
            {
                String foba= "Faculty of Business Administration";
                tvFaculty.setText(foba);
            }
            TextView tvDepartment = findViewById(R.id.txt_FDep);
            tvDepartment.setText("Department of "+ empDepartment);


               fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {

                @Override
                public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                       frameLayout.setBackgroundColor(getResources().getColor(R.color.TransparentBlack));
                   return super.onPrepareMenu(navigationMenu);
                }

                   @Override
                   public void onMenuClosed() {
                    frameLayout.setBackgroundColor(Color.TRANSPARENT);
                       super.onMenuClosed();
                   }

                   @Override
                public boolean onMenuItemSelected(MenuItem menuItem) {
                    int itemId= menuItem.getItemId();
                       frameLayout.setBackgroundColor(Color.TRANSPARENT);
                       FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
                       if(itemId==R.id.half_leave)
                    {
                        HalfLeaveFragment halfLeaveFragment = new HalfLeaveFragment();
                         transaction.replace(R.id.container_faculty, halfLeaveFragment)
                                .addToBackStack(null).commit();

                    }
                    else if (itemId==R.id.full_leave)
                    {
                        FullLeaveFragment fullLeaveFragment = new FullLeaveFragment();
                        transaction.replace(R.id.container_faculty, fullLeaveFragment)
                                .addToBackStack(null).commit();

                    }
                    else if (itemId==R.id.leave_without_pay)
                    {
                        LeaveWithoutPayFragment leaveWithoutPayFragment = new LeaveWithoutPayFragment();
                        transaction.replace(R.id.container_faculty, leaveWithoutPayFragment)
                                .addToBackStack(null).commit();
                    }
                    return false;
                }
            });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

            NavCmi = headerview.findViewById(R.id.user_pic_faculty);
            TextView txt_name = headerview.findViewById(R.id.user_name_faculty);
            txt_name.setText(person_name);
            TextView txt_email = headerview.findViewById(R.id.user_email_faculty);
            txt_email.setText(person_email);
            Glide.with(FacultyMainActivity.this)
                    .load(person_pic)
                    .apply(new RequestOptions().override(100, 100))
                    .apply(new RequestOptions().centerCrop())
                    .into(NavCmi);

    }
}
    @Override
    public void onBackPressed() {
        showFloatingActionButton();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items1 to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faculty_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            person_email=null;
            person_name=null;
            person_pic=null;
            startActivity(new Intent(FacultyMainActivity.this, SignInActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Fragment faculty = new FullLeaveFragment();
            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_faculty, faculty)
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showFloatingActionButton()
    {
        fabSpeedDial.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        fabSpeedDial.show();
        frameLayout.isShown();
    }

    public void hideFloatingActionButton()
    {
        fabSpeedDial.hide();
        frameLayout.setVisibility(View.INVISIBLE);
    }

    public void clearBackstack()
    {
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);
        getSupportFragmentManager().popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
        showFloatingActionButton();
    }
}
