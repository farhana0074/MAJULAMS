package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.farhakhan.majulams.model_classes.TotalAnnualLeaves;
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
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class FacultyMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {


    public String person_email, person_name, person_pic, mperson_email;
    public String strDate, strDay, empDesignation, currentYear;
    public String empFaculty, empDepartment, empDomain;
    public Long TotalLeavesHL, TotalLeavesFL, TotalLeavesLWP, TotalLeavesSL;
    public CircleImageView NavCmi;
    public CircleImageView MainCmi;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private GoogleApiClient mGoogleApiClient;
    public  FabSpeedDial fabSpeedDial;
    public FrameLayout frameLayout, container;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();
        fabSpeedDial = findViewById(R.id.fab_speed_dial);
        frameLayout= findViewById(R.id.fab_bkg);
        container = findViewById(R.id.container_faculty);
        Intent intent = getIntent();
        if (intent != null) {
            person_email = intent.getStringExtra("EmailID");
            mperson_email=person_email.replace(",",".");
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

            Query queryCurrYear = mDbReference.child("Years").orderByChild("Status").equalTo("Current");
                    queryCurrYear.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot child: dataSnapshot.getChildren()){
                     currentYear = child.getKey();
                     TextView txt_currentYear = findViewById(R.id.app_bar_year);
                     txt_currentYear.setText(currentYear);

                     final TextView tvTotalHL = findViewById(R.id.hl_total);
                     final TextView tvTotalFL = findViewById(R.id.fl_total);
                     final TextView tvTotalLWP = findViewById(R.id.pl_total);
                     final TextView tvTotalSL = findViewById(R.id.sl_total);

                     mDbReference.child("Years").child(currentYear).child("LeavesAllowed")
                             .addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                                     if (datasnapshot!= null && datasnapshot.getValue()!= null)
                                     {
                                         final TotalAnnualLeaves totalAnnualLeaves = datasnapshot.getValue(TotalAnnualLeaves.class);
                                         TotalLeavesHL = totalAnnualLeaves.HalfLeaves;
                                         TotalLeavesFL = totalAnnualLeaves.FullLeaves;
                                         TotalLeavesLWP = totalAnnualLeaves.LeavesWithoutPay;
                                         TotalLeavesSL = totalAnnualLeaves.SummerLeaves;

                                         tvTotalHL.setText("Total Half Leaves:  "+ TotalLeavesHL);
                                         tvTotalFL.setText("Total Full Leaves:  "+ TotalLeavesFL);
                                         tvTotalLWP.setText("Total Leaves Without Pay:  "+ TotalLeavesLWP);
                                         tvTotalSL.setText("Total Summer Leaves:  "+ TotalLeavesSL);


                                     }
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError databaseError) {

                                 }
                             });


                     TextView tvRemainingHL = findViewById(R.id.hl_rem);
                     tvRemainingHL.setText("Remaining:  ");

                     TextView tvRemainingFL = findViewById(R.id.fl_rem);
                     tvRemainingFL.setText("Remaining:  ");

                     TextView tvRemainingLWP = findViewById(R.id.pl_rem);
                     tvRemainingLWP.setText("Remaining:  ");

                     TextView tvRemainingSL = findViewById(R.id.sl_rem);
                     tvRemainingSL.setText("Remaining:  ");

                     TextView tvAvailedHL = findViewById(R.id.hl_avld);
                     tvAvailedHL.setText("Availed:  ");

                     TextView tvAvailedFL = findViewById(R.id.fl_avld);
                     tvAvailedFL.setText("Availed:  ");

                     TextView tvAvailedLWP = findViewById(R.id.pl_avld);
                     tvAvailedLWP.setText("Availed:  ");

                     TextView tvAvailedSL = findViewById(R.id.sl_avld);
                     tvAvailedSL.setText("Availed:  ");


                 }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

            final TextView tvDesignation = findViewById(R.id.txt_FDesig);

            mDbReference.child(empFaculty).child(empDepartment).child(empDomain).child(person_email)
                    .child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null)
                    empDesignation = dataSnapshot.getValue().toString();
                    tvDesignation.setText(empDesignation);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

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
                       Bundle bundle = new Bundle();
                       bundle.putString("EmailID", person_email);
                       bundle.putString("Faculty", empFaculty);
                       bundle.putString("Department", empDepartment);
                       bundle.putString("Domain", empDomain);
                       bundle.putString("Designation", empDesignation);
                       FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();

                       if(itemId==R.id.half_leave)
                    {
                        HalfLeaveFragment halfLeaveFragment = new HalfLeaveFragment();
                        halfLeaveFragment.setArguments(bundle);
                         transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                 .replace(R.id.container_faculty, halfLeaveFragment)
                                .addToBackStack(null).commit();

                    }
                    else if (itemId==R.id.full_leave)
                    {
                        FullLeaveFragment fullLeaveFragment = new FullLeaveFragment();
                        fullLeaveFragment.setArguments(bundle);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.container_faculty, fullLeaveFragment)
                                .addToBackStack(null).commit();

                    }
                    else if (itemId==R.id.leave_without_pay)
                    {
                        LeaveWithoutPayFragment leaveWithoutPayFragment = new LeaveWithoutPayFragment();
                        leaveWithoutPayFragment.setArguments(bundle);
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.container_faculty, leaveWithoutPayFragment)
                                .addToBackStack(null).commit();
                    }
                    return false;
                }
            });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer =  findViewById(R.id.drawer_layout);


        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
             this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
            final Query queryCheck = mDbReference.child(empFaculty).child(empDepartment).child(empDomain)
                    .child(person_email).child("LeavesHistory");

            ((DatabaseReference) queryCheck).child("HalfLeaves").orderByChild("Seen")
                    .equalTo("No").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshotNavHL) {
                    menu = navigationView.getMenu();
                    final MenuItem alertItem = menu.findItem(R.id.nav_alert);
                    final Drawable alertIcon = alertItem.getIcon();
                    final SpannableString strTitle = new SpannableString(alertItem.getTitle().toString());

                    if (dataSnapshotNavHL.exists())
                     {
                         toggle.setDrawerIndicatorEnabled(false);
                         toggle.setHomeAsUpIndicator(R.drawable.toggle_with_notif);
                         toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 if (drawer.isDrawerVisible(GravityCompat.START))
                                     drawer.closeDrawer(GravityCompat.START);
                                 else
                                     drawer.openDrawer(GravityCompat.START);
                             }});

                         alertIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                        alertItem.setIcon(alertIcon);
                        strTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, strTitle.length(), 0);
                        alertItem.setTitle(strTitle);
                    }
                    else {
                        ((DatabaseReference) queryCheck).child("FullLeaves").orderByChild("Seen").equalTo("No").addValueEventListener
                                        (new ValueEventListener()
                                         {
                                             @Override
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshotNavFL) {
                                                 if(dataSnapshotNavFL.exists())
                                                 {
                                                     toggle.setDrawerIndicatorEnabled(false);
                                                     toggle.setHomeAsUpIndicator(R.drawable.toggle_with_notif);
                                                     toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if (drawer.isDrawerVisible(GravityCompat.START))
                                                                 drawer.closeDrawer(GravityCompat.START);
                                                             else
                                                                 drawer.openDrawer(GravityCompat.START);
                                                         }});

                                                     alertIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                                                     alertItem.setIcon(alertIcon);
                                                     strTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, strTitle.length(), 0);
                                                     alertItem.setTitle(strTitle);
                                                 }
                                                   else
                                                 {
                                                     ((DatabaseReference) queryCheck).child("LeavesWithoutPay").orderByChild("Seen").equalTo("No").addValueEventListener
                                                             (new ValueEventListener() {
                                                                 @Override
                                                                 public void onDataChange(@NonNull DataSnapshot dataSnapshotNavLWP) {
                                                                     if(dataSnapshotNavLWP.exists()) {
                                                                         toggle.setDrawerIndicatorEnabled(false);
                                                                         toggle.setHomeAsUpIndicator(R.drawable.toggle_with_notif);
                                                                         toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                                                             @Override
                                                                             public void onClick(View v) {
                                                                                 if (drawer.isDrawerVisible(GravityCompat.START))
                                                                                     drawer.closeDrawer(GravityCompat.START);
                                                                                 else
                                                                                     drawer.openDrawer(GravityCompat.START);
                                                                             }});

                                                                         alertIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                                                                         alertItem.setIcon(alertIcon);
                                                                         strTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, strTitle.length(), 0);
                                                                         alertItem.setTitle(strTitle);
                                                                     }
                                                                     else
                                                                     {
                                                                         ((DatabaseReference) queryCheck).child("SummerLeaves").orderByChild("Seen").equalTo("No").addValueEventListener
                                                                                 (new ValueEventListener() {
                                                                                     @Override
                                                                                     public void onDataChange(@NonNull DataSnapshot dataSnapshotNavSL) {
                                                                                         if(dataSnapshotNavSL.exists())
                                                                                         {
                                                                                             toggle.setDrawerIndicatorEnabled(false);
                                                                                             toggle.setHomeAsUpIndicator(R.drawable.toggle_with_notif);
                                                                                             toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                                                                                                 @Override
                                                                                                 public void onClick(View v) {
                                                                                                     if (drawer.isDrawerVisible(GravityCompat.START))
                                                                                                         drawer.closeDrawer(GravityCompat.START);
                                                                                                     else
                                                                                                         drawer.openDrawer(GravityCompat.START);
                                                                                                 }});
                                                                                             alertIcon.mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                                                                                             alertItem.setIcon(alertIcon);
                                                                                             strTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, strTitle.length(), 0);
                                                                                             alertItem.setTitle(strTitle);
                                                                                         }

                                                                                         else
                                                                                         {
                                                                                             toggle.setDrawerIndicatorEnabled(true);
                                                                                             drawer.addDrawerListener(toggle);
                                                                                             toggle.syncState();
                                                                                             alertItem.setIcon(getResources().getDrawable(R.drawable.alert_icon));
                                                                                             alertItem.setTitle(alertItem.getTitle().toString());
                                                                                         }
                                                                                     }

                                                                                     @Override
                                                                                     public void onCancelled(@NonNull DatabaseError databaseError) { }});
                                                                     }
                                                                 }

                                                                 @Override
                                                                 public void onCancelled(@NonNull DatabaseError databaseError) { }});
                                                 }
                                                 }
                                                 @Override
                                                 public void onCancelled(@NonNull DatabaseError databaseError) { }});
                    }
                    }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        View headerview = navigationView.getHeaderView(0);

            NavCmi = headerview.findViewById(R.id.user_pic_faculty);
            TextView txt_name = headerview.findViewById(R.id.user_name_faculty);
            txt_name.setText(person_name);
            TextView txt_email = headerview.findViewById(R.id.user_email_faculty);
            txt_email.setText(mperson_email);
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
        }
        else{
            if(getSupportFragmentManager().getBackStackEntryCount()>= 0)
            {
                new AlertDialog.Builder(this)
                        .setTitle(" Exit")
                        .setIcon(R.drawable.my_alert_icon)
                        .setMessage("Are you sure you want to exit?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                quit();
                                FacultyMainActivity.super.onBackPressed();
                            }
                        }).create().show();
            }
              else
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
         int id = item.getItemId();
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

        if (id == R.id.nav_summers) {
            Fragment summerLeaves = new SummerLeavesFragment();
            Bundle bundle = new Bundle();
            bundle.putString("EmailID", person_email);
            bundle.putString("Faculty", empFaculty);
            bundle.putString("Department", empDepartment);
            bundle.putString("Domain", empDomain);
            bundle.putString("Designation", empDesignation);
            summerLeaves.setArguments(bundle);
            FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(R.id.container_faculty, summerLeaves)
                    .addToBackStack(null).commit();
        } else if (id == R.id.nav_alert) {

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

    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }
}
