package com.farhakhan.majulams;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    public String person_email, person_name, person_pic, mperson_email;
    public String strDate, strDay, currentYear;
    public String empFaculty, empDepartment;
    public CircleImageView NavCmi;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private GoogleApiClient mGoogleApiClient;

    SharedPreferences sharedPreferences;
    public static final String MY_SHARED_PREFERENCES = "MySharedPrefs";
    public static final String CurrYear = "CurrYearKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        Intent intent = getIntent();
        if (intent != null) {
            person_email = intent.getStringExtra("EmailID");
            mperson_email = person_email.replace(",", ".");
            person_name = intent.getStringExtra("Name");
            person_pic = intent.getStringExtra("Picture");


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy ");
            strDate = dateFormat.format(calendar.getTime());
            TextView txt_date = findViewById(R.id.Admin_Date);
            txt_date.setText(strDate);
            SimpleDateFormat day = new SimpleDateFormat("EEEE");
            strDay = day.format(calendar.getTime());
            TextView txt_day = findViewById(R.id.Admin_Day);
            txt_day.setText(strDay);

            sharedPreferences = getApplicationContext().getSharedPreferences(MY_SHARED_PREFERENCES,Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            Query queryCurrYear = mDbReference.child("Years").orderByChild("Status").equalTo("Current");
            queryCurrYear.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        currentYear = child.getKey();
                        editor.putString(CurrYear, currentYear);
                        TextView txt_currentYear = findViewById(R.id.appbar_year);
                        txt_currentYear.setText(currentYear);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});
            editor.commit();


            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();


            Toolbar toolbar = findViewById(R.id.toolbar_admin);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = findViewById(R.id.nav_view_admin);
            navigationView.setNavigationItemSelectedListener(this);
            View headerview = navigationView.getHeaderView(0);

            NavCmi = headerview.findViewById(R.id.user_pic_admin);
            TextView txt_name = headerview.findViewById(R.id.user_name_admin);
            txt_name.setText(person_name);
            TextView txt_email = headerview.findViewById(R.id.user_email_admin);
            txt_email.setText(mperson_email);
            Glide.with(AdminMainActivity.this)
                    .load(person_pic)
                    .apply(new RequestOptions().override(100, 100))
                    .apply(new RequestOptions().centerCrop())
                    .into(NavCmi);

            Toolbar facToolbar = findViewById(R.id.facToolbar);
            TabLayout tabLayout = findViewById(R.id.tablayout);
            TabItem tabFOCE = findViewById(R.id.tabfoce);
            TabItem tabFOBA = findViewById(R.id.tabfoba);
            TabItem tabFOLS = findViewById(R.id.tabfols);
            ViewPager viewPager = findViewById(R.id.viewPager);
            PageAdapterFacultyInAdmin pageAdapter = new PageAdapterFacultyInAdmin(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pageAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setupWithViewPager(viewPager);
        }
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
        if (id == R.id.action_log_out) {
            FirebaseAuth.getInstance().signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            person_email=null;
            person_name=null;
            person_pic=null;
            startActivity(new Intent(AdminMainActivity.this, SignInActivity.class));
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

        if (id == R.id.nav_summers) {
            Fragment admin = new AdminProcessedHL();
            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_admin, admin)
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_admin);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void clearBackstack()
    {
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(0);
        getSupportFragmentManager().popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().executePendingTransactions();
    }
}
