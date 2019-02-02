package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.farhakhan.majulams.model_classes.FLforHodApprovl;
import com.farhakhan.majulams.model_classes.HLforHodApprovl;
import com.farhakhan.majulams.model_classes.SLnLWPforHodApprovl;
import com.farhakhan.majulams.model_classes.UserNamePic;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class HodMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener
{
    public String person_email, person_name, person_pic, mperson_email;
    public String empFaculty, empDepartment;
    public String strDate, strDay, strAppDateTimeOutHL, strAppDateTimeInHL, strAppDateTimeInFL,
            strAppDateTimeOutFL, strAppDateTimeInLWP, strAppDateTimeOutLWP, strAppDateTimeInSL,
            strAppDateTimeOutSL, currentYear;
    public CircleImageView NavCmi;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    private GoogleApiClient mGoogleApiClient;
    private RecyclerView mRecyclerViewHL;
    private RecyclerView mRecyclerViewFL;
    private RecyclerView mRecyclerViewLWP;
    private RecyclerView mRecyclerViewSL;
    private LinearLayoutManager mLayoutManagerHL;
    private LinearLayoutManager mLayoutManagerFL;
    private LinearLayoutManager mlayoutManagerLWP;
    private LinearLayoutManager mlayoutManagerSL;
    FirebaseRecyclerAdapter<HLforHodApprovl, LeaveAppViewHolder> mFirebaseAdapterHL;
    FirebaseRecyclerAdapter<FLforHodApprovl, LeaveAppViewHolder> mFirebaseAdapterFL;
    FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LeaveAppViewHolder> mFirebaseAdapterLWP;
    FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LeaveAppViewHolder> mFirebaseAdapterSL;
    private TextView tvNoLeaves;
    Date ApplyingDateTimeHL, ApplyingDateTimeFL, ApplyingDateTimeLWP, ApplyingDateTimeSL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_main);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy ");
        strDate = dateFormat.format(calendar.getTime());
        TextView txt_date= findViewById(R.id.HoD_Date);
        txt_date.setText(strDate);
        SimpleDateFormat day = new SimpleDateFormat("EEEE");
        strDay=day.format(calendar.getTime());
        TextView txt_day = findViewById(R.id.HoD_Day);
        txt_day.setText(strDay);

        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();

        Intent intent = getIntent();
        if (intent != null) {
            person_email = intent.getStringExtra("EmailID");
            mperson_email = person_email.replace(",", ".");
            person_name = intent.getStringExtra("Name");
            person_pic = intent.getStringExtra("Picture");
            empFaculty = intent.getStringExtra("Faculty");
            empDepartment = intent.getStringExtra("Department");
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tvFaculty = findViewById(R.id.txtI_fac);
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

        final Bundle bundle = new Bundle();
        bundle.putString("person_email", person_email);
        bundle.putString("empDepartment", empDepartment);
        bundle.putString("empFaculty", empFaculty);
        TextView tvDepartment = findViewById(R.id.txtI_dep);
        tvDepartment.setText("Department of "+ empDepartment);
        Query queryCurrYear = mDbReference.child("Years").orderByChild("Status").equalTo("Current");
        queryCurrYear.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    currentYear = child.getKey();
                    bundle.putString("currentYear", currentYear);
                    TextView txt_currentYear = findViewById(R.id.txt_Year);
                    txt_currentYear.setText(currentYear);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        txt_email.setText(mperson_email);
        Glide.with(HodMainActivity.this)
                .load(person_pic)
                .apply(new RequestOptions().override(100, 100))
                .apply(new RequestOptions().centerCrop())
                .into(NavCmi);

        tvNoLeaves = findViewById(R.id.tv_no_leave_apps);


        final Query UserInfo = mDbReference.child("Users").child("Faculty");

        final SimpleDateFormat AppDateTimeOutFormat = new SimpleDateFormat("dd MMM, yyyy | EEEE | h:mm a");
        final SimpleDateFormat AppDateTimeInFormat = new SimpleDateFormat("yyyy-MM-dd, h:mm");

        mRecyclerViewHL = findViewById(R.id.recycler_view_hl);
        mLayoutManagerHL = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        mRecyclerViewHL.setLayoutManager(mLayoutManagerHL);
        mRecyclerViewHL.addItemDecoration(new HeaderDecoration(this, mRecyclerViewHL, R.layout.header_item_hl));
        Query queryHL = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves")
                .child("HalfLeaves").orderByChild("LeaveApprovalStatus").equalTo("Pending");

             FirebaseRecyclerOptions<HLforHodApprovl> optionsHL =
                new FirebaseRecyclerOptions.Builder<HLforHodApprovl>()
                        .setQuery(queryHL, HLforHodApprovl.class)
                        .build();
        mFirebaseAdapterHL = new FirebaseRecyclerAdapter<HLforHodApprovl, LeaveAppViewHolder>(optionsHL) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LeaveAppViewHolder holder, int position, @NonNull final HLforHodApprovl model) {
               if(getItemCount() == 0)
                viewNoLeaves();
               else
                   hideNoLeaves();
                holder.tvLeaveType.setText("Half Leave ("+model.LeaveType+" )");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                           String EmployeeDomain = dataSnapshot.getValue().toString();
                           holder.tvApplicantDomain.setText(EmployeeDomain);

                           Query queryED= mDbReference.child(empFaculty).child(empDepartment).child(EmployeeDomain)
                                  .child(model.EmployeeEmail);

                           ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                   if (dataSnapshot3!=null) {
                                       String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                       holder.tvApplicantDesignation.setText(EmployeeDesignation);
                                   }
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) { }});

                            ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                   if (dataSnapshot2!=null) {
                                       UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                       String ApplicantName = userNamePic.Name;
                                       String ApplicantPic = userNamePic.PicUrl;
                                       holder.tvApplicantName.setText(ApplicantName);
                                       Glide.with(getApplicationContext())
                                               .load(ApplicantPic)
                                               .apply(new RequestOptions().override(100, 100))
                                               .apply(new RequestOptions().centerCrop())
                                               .into(holder.civApplicantPic); } }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                strAppDateTimeInHL = model.LeaveApplyingDate+ ", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeHL = AppDateTimeInFormat.parse(strAppDateTimeInHL);
                    if(ApplyingDateTimeHL !=null)
                        strAppDateTimeOutHL = AppDateTimeOutFormat.format(ApplyingDateTimeHL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutHL);
                final String HLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("HLKey", HLKey);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        HodHalfLeaveDetails hodHalfLeaveDetails =  new HodHalfLeaveDetails();
                        hodHalfLeaveDetails.setArguments(bundle);
                        fragmentTransaction.add(R.id.container_hod, hodHalfLeaveDetails)
                                .addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public LeaveAppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LeaveAppViewHolder(view);
            }
        };
        mRecyclerViewHL.setAdapter(mFirebaseAdapterHL);

        mRecyclerViewFL = findViewById(R.id.recycler_view_fl);
        mLayoutManagerFL = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        mRecyclerViewFL.setLayoutManager(mLayoutManagerFL);
        mRecyclerViewFL.addItemDecoration(new HeaderDecoration(this, mRecyclerViewFL, R.layout.header_item_fl));
        Query queryFL = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves")
                .child("FullLeaves").orderByChild("LeaveApprovalStatus").equalTo("Pending");

        FirebaseRecyclerOptions<FLforHodApprovl> optionsFL =
                new FirebaseRecyclerOptions.Builder<FLforHodApprovl>()
                        .setQuery(queryFL, FLforHodApprovl.class)
                        .build();
        mFirebaseAdapterFL = new FirebaseRecyclerAdapter<FLforHodApprovl, LeaveAppViewHolder>(optionsFL) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LeaveAppViewHolder holder, int position, @NonNull final FLforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                hideNoLeaves();
                holder.tvLeaveType.setText("Full Leave ("+model.LeaveType+" )");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                            String EmployeeDomain = dataSnapshot.getValue().toString();
                        holder.tvApplicantDomain.setText(EmployeeDomain);

                        Query queryED= mDbReference.child(empFaculty).child(empDepartment).child(EmployeeDomain)
                                .child(model.EmployeeEmail);

                        ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                if (dataSnapshot3 != null) {
                                    String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                    holder.tvApplicantDesignation.setText(EmployeeDesignation);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }});

                        ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                if (dataSnapshot2!=null) {
                                    UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                    String ApplicantName = userNamePic.Name;
                                    String ApplicantPic = userNamePic.PicUrl;
                                    holder.tvApplicantName.setText(ApplicantName);
                                    Glide.with(getApplicationContext())
                                            .load(ApplicantPic)
                                            .apply(new RequestOptions().override(100, 100))
                                            .apply(new RequestOptions().centerCrop())
                                            .into(holder.civApplicantPic); } }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}});
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }});

            strAppDateTimeInFL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                ApplyingDateTimeFL = AppDateTimeInFormat.parse(strAppDateTimeInFL);
                if(ApplyingDateTimeFL !=null)
                    strAppDateTimeOutFL = AppDateTimeOutFormat.format(ApplyingDateTimeFL);
            } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutFL);

                final String FLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("FLKey", FLKey);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        HodFullLeaveDetails hodFullLeaveDetails =  new HodFullLeaveDetails();
                        hodFullLeaveDetails.setArguments(bundle);
                        fragmentTransaction.add(R.id.container_hod, hodFullLeaveDetails)
                                .addToBackStack(null).commit();
                    }
                });
        }

            @NonNull
            @Override
            public LeaveAppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LeaveAppViewHolder(view);
            }
        };
        mRecyclerViewFL.setAdapter(mFirebaseAdapterFL);

        mRecyclerViewLWP = findViewById(R.id.recycler_view_lwp);
        mlayoutManagerLWP =new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        mRecyclerViewLWP.setLayoutManager(mlayoutManagerLWP);
        mRecyclerViewLWP.addItemDecoration(new HeaderDecoration(this, mRecyclerViewLWP, R.layout.header_item_lwp));
        Query queryLWP = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves")
                .child("LeavesWithoutPay").orderByChild("LeaveApprovalStatus").equalTo("Pending");

        FirebaseRecyclerOptions<SLnLWPforHodApprovl> optionsLWP =
                new FirebaseRecyclerOptions.Builder<SLnLWPforHodApprovl>()
                        .setQuery(queryLWP, SLnLWPforHodApprovl.class)
                        .build();
        mFirebaseAdapterLWP = new FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LeaveAppViewHolder>(optionsLWP) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull final LeaveAppViewHolder holder, int position, @NonNull final SLnLWPforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveType.setText("Leave Without Pay");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                            String EmployeeDomain = dataSnapshot.getValue().toString();
                            holder.tvApplicantDomain.setText(EmployeeDomain);
                            Query queryED= mDbReference.child(empFaculty).child(empDepartment).child(EmployeeDomain)
                                    .child(model.EmployeeEmail);

                            ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if (dataSnapshot3 != null) {
                                        String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                        holder.tvApplicantDesignation.setText(EmployeeDesignation);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});

                            ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2!=null) {
                                        UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                        String ApplicantName = userNamePic.Name;
                                        String ApplicantPic = userNamePic.PicUrl;
                                        holder.tvApplicantName.setText(ApplicantName);
                                        Glide.with(getApplicationContext())
                                                .load(ApplicantPic)
                                                .apply(new RequestOptions().override(100, 100))
                                                .apply(new RequestOptions().centerCrop())
                                                .into(holder.civApplicantPic); } }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                strAppDateTimeInLWP = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeLWP = AppDateTimeInFormat.parse(strAppDateTimeInLWP);
                    if(ApplyingDateTimeLWP !=null)
                        strAppDateTimeOutLWP = AppDateTimeOutFormat.format(ApplyingDateTimeLWP);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutLWP);
                final String LWPKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("LWPKey", LWPKey);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        HodLeavesWithoutPayDetails hodLeavesWithoutPayDetails =  new HodLeavesWithoutPayDetails();
                        hodLeavesWithoutPayDetails.setArguments(bundle);
                        fragmentTransaction.add(R.id.container_hod, hodLeavesWithoutPayDetails)
                                .addToBackStack(null).commit();
                    }
                });

            }

            @NonNull
            @Override
            public LeaveAppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LeaveAppViewHolder(view);
            }
        };
        mRecyclerViewLWP.setAdapter(mFirebaseAdapterLWP);

        mRecyclerViewSL = findViewById(R.id.recycler_view_sl);
        mlayoutManagerSL=new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false );
        mRecyclerViewSL.setLayoutManager(mlayoutManagerSL);
        mRecyclerViewSL.addItemDecoration(new HeaderDecoration(this, mRecyclerViewSL, R.layout.header_item_sl));
        Query querySL = mDbReference.child(empFaculty).child(empDepartment).child("EmployeeLeaves")
                .child("SummerLeaves").orderByChild("LeaveApprovalStatus").equalTo("Pending");

        FirebaseRecyclerOptions<SLnLWPforHodApprovl> optionsSL =
                new FirebaseRecyclerOptions.Builder<SLnLWPforHodApprovl>()
                        .setQuery(querySL, SLnLWPforHodApprovl.class)
                        .build();

        mFirebaseAdapterSL = new FirebaseRecyclerAdapter<SLnLWPforHodApprovl, LeaveAppViewHolder>(optionsSL) {

            @Override
            protected void onBindViewHolder(@NonNull final LeaveAppViewHolder holder, int position, @NonNull final SLnLWPforHodApprovl model) {
                if(getItemCount() == 0)
                    viewNoLeaves();
                else
                    hideNoLeaves();
                holder.tvLeaveType.setText("Summer Leave");
                ((DatabaseReference) UserInfo).child(model.EmployeeEmail).child("Domain").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot!= null)
                        {
                            String EmployeeDomain = dataSnapshot.getValue().toString();
                            holder.tvApplicantDomain.setText(EmployeeDomain);

                            Query queryED= mDbReference.child(empFaculty).child(empDepartment).child(EmployeeDomain)
                                    .child(model.EmployeeEmail);

                            ((DatabaseReference) queryED).child("Designation").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot3) {
                                    if (dataSnapshot3 != null) {
                                        String EmployeeDesignation = dataSnapshot3.getValue().toString();
                                        holder.tvApplicantDesignation.setText(EmployeeDesignation);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) { }});

                            ((DatabaseReference)queryED).child("Info").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                    if (dataSnapshot2!=null) {
                                        UserNamePic userNamePic =dataSnapshot2.getValue(UserNamePic.class);
                                        String ApplicantName = userNamePic.Name;
                                        String ApplicantPic = userNamePic.PicUrl;
                                        holder.tvApplicantName.setText(ApplicantName);
                                        Glide.with(getApplicationContext())
                                                .load(ApplicantPic)
                                                .apply(new RequestOptions().override(100, 100))
                                                .apply(new RequestOptions().centerCrop())
                                                .into(holder.civApplicantPic); } }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}});
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }});

                strAppDateTimeInSL = model.LeaveApplyingDate+", "+model.LeaveApplyingTime;
                try {
                    ApplyingDateTimeSL = AppDateTimeInFormat.parse(strAppDateTimeInSL);
                    if(ApplyingDateTimeSL !=null)
                        strAppDateTimeOutSL = AppDateTimeOutFormat.format(ApplyingDateTimeSL);
                } catch (ParseException e) { e.printStackTrace(); }

                holder.tvLeaveApplyingTime.setText("Applied: "+" " + strAppDateTimeOutSL);
                final String SLKey = this.getRef(position).getKey();
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bundle.putString("SLKey", SLKey);
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        HodSummerLeavesDetails hodSummerLeavesDetails =  new HodSummerLeavesDetails();
                        hodSummerLeavesDetails.setArguments(bundle);
                        fragmentTransaction.add(R.id.container_hod, hodSummerLeavesDetails)
                                .addToBackStack(null).commit();
                    }
                });
            }

            @NonNull
            @Override
            public LeaveAppViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.leave_app_item, viewGroup, false);

                return new LeaveAppViewHolder(view);
            }
        };
        mRecyclerViewSL.setAdapter(mFirebaseAdapterSL);

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewNoLeaves();
        mFirebaseAdapterHL.startListening();
        mFirebaseAdapterFL.startListening();
        mFirebaseAdapterLWP.startListening();
        mFirebaseAdapterSL.startListening();
    }

    @Override
    public void onBackPressed() {
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
                                HodMainActivity.super.onBackPressed();
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
        getMenuInflater().inflate(R.menu.hod_main, menu);
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
            startActivity(new Intent(HodMainActivity.this, SignInActivity.class));
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
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_history) {

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


    public static class LeaveAppViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView tvApplicantName;
        TextView tvLeaveApplyingTime;
        TextView tvApplicantDomain;
        TextView tvLeaveType;
        TextView tvApplicantDesignation;
        CircleImageView civApplicantPic;

        public LeaveAppViewHolder(View v) {
            super(v);
            view = v;
            tvApplicantName = v.findViewById(R.id.txtI_name);
            tvApplicantDesignation = v.findViewById(R.id.txtI_designation);
            tvLeaveApplyingTime = v.findViewById(R.id.txtI_app_time);
            tvApplicantDomain = v.findViewById(R.id.txtI_domain);
            tvLeaveType = v.findViewById(R.id.txt_leaveTyp);
            civApplicantPic = v.findViewById(R.id.iv_person_pic_fl);
              }
    }

    public void quit() {
        Intent start = new Intent(Intent.ACTION_MAIN);
        start.addCategory(Intent.CATEGORY_HOME);
        start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(start);
    }

    public void viewNoLeaves() {
        tvNoLeaves.setVisibility(View.VISIBLE);
    }

    public void  hideNoLeaves() {
        tvNoLeaves.setVisibility(View.INVISIBLE);
    }
    }
