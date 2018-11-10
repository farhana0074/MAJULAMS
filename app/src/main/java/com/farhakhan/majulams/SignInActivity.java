package com.farhakhan.majulams;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farhakhan.majulams.model_classes.EmpFacDepDom;
import com.farhakhan.majulams.model_classes.UserNamePic;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    ImageButton ibAdminSignIn;
    ImageButton ibFacultySignIn;
    ProgressBar progressBar;
    TextView note;
    private int RC_SIGN_IN=1;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleApiClient mGoogleApiClient;
    private String TAG="SignInActivity";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference mDbReference;
    int id_imgbtn_adm, id_imgbtn_emp, id_imgbtn;
    String person_email, person_name, person_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressBar=findViewById(R.id.login_progress);
        progressBar.setVisibility(View.INVISIBLE);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();





        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        note= findViewById(R.id.txt_note);
        ibAdminSignIn =findViewById(R.id.imageButtonAdminSignIn);
        ibFacultySignIn= findViewById(R.id.imageButtonEmployeeSignIn);
        ibAdminSignIn.setOnClickListener(this);
        ibFacultySignIn.setOnClickListener(this);
        mAuth=FirebaseAuth.getInstance();
        mFirebaseDb=FirebaseDatabase.getInstance();
        mDbReference=mFirebaseDb.getReference();
    }

    private void signIn() {
        hideWidgets();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            progressBar.setVisibility(View.INVISIBLE);
            showWidgets();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                progressBar.setVisibility(View.VISIBLE);
                hideWidgets();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            person_email=person_email.replace(".",",");

                            if (id_imgbtn ==id_imgbtn_adm) {
                                mDbReference.child("Users").child("Administrators")
                                        .addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                progressBar.setVisibility(View.INVISIBLE);
                                                if (dataSnapshot.hasChild(person_email))
                                                {
                                                    Toast.makeText(getApplicationContext(),"welcome "+ person_name, Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(SignInActivity.this, AdminMainActivity.class));
                                                    finish();
                                                }
                                                else
                                                {
                                                    user.delete();
                                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                                    builder.setTitle("Invalid Administrator ID");
                                                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                                                    builder.setMessage(R.string.Adm_invalid_Sign_in_note);
                                                    builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                        }
                                                    });
                                                    builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finishAffinity();
                                                        }
                                                    });
                                                    builder.show();
                                                    showWidgets();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                                        });
                            }
                            if (id_imgbtn == id_imgbtn_emp)
                                mDbReference.child("Users").child("Faculty")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        if(dataSnapshot.hasChild(person_email)) {
                                            Toast.makeText(getApplicationContext(),"welcome "+ person_name, Toast.LENGTH_LONG).show();

                                            mDbReference.child("Users").child("Faculty").child(person_email).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                               if (dataSnapshot1 != null && dataSnapshot1.getValue() != null)
                                               {
                                                   final EmpFacDepDom empFacDepDom = dataSnapshot1.getValue(EmpFacDepDom.class);
                                                   final String empFaculty = empFacDepDom.Faculty;
                                                   String empDepartment= empFacDepDom.Department;
                                                   String empDomain= empFacDepDom.Domain;
                                                   UserNamePic mUserNamePic = new UserNamePic(person_name, person_pic);

                                                   Map<String, Object> values = mUserNamePic.toMap();
                                                   Map<String, Object> childUpdates = new HashMap<>();
                                                   childUpdates.put("UserInfo", values);

                                                   mDbReference.child(empFaculty).child(empDepartment).child(empDomain)
                                                           .child(person_email).updateChildren(childUpdates);
                                                   Bundle bundle = new Bundle();
                                                   bundle.putString("EmailID", person_email);
                                                   bundle.putString("Picture", person_pic);
                                                   bundle.putString("Name", person_name);
                                                   bundle.putString("Faculty", empFaculty);
                                                   bundle.putString("Department", empDepartment);
                                                   bundle.putString("Domain", empDomain);
                                                   Intent intent= new Intent(SignInActivity.this, FacultyMainActivity.class);
                                                   intent.putExtras(bundle);
                                                   startActivity(intent);
                                                   finish();
                                               }
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                        else
                                        {
                                            mDbReference.child("Users").child("HoD")
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            if (dataSnapshot.hasChild(person_email))
                                                            {
                                                                Toast.makeText(getApplicationContext(),"welcome "+ person_name, Toast.LENGTH_LONG).show();
                                                                startActivity(new Intent(SignInActivity.this, HodMainActivity.class));
                                                                finish();
                                                            }
                                                            else
                                                            {
                                                                user.delete();
                                                                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                                                final AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                                                                builder.setTitle("Invalid Employee ID");
                                                                builder.setIcon(android.R.drawable.ic_dialog_alert);
                                                                builder.setMessage(R.string.Emp_invalid_Sign_in_note);
                                                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                    }
                                                                });
                                                                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        finishAffinity();
                                                                    }
                                                                });
                                                                builder.show();
                                                                showWidgets();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                            updateUI(null);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account!=null)
        {
            person_name= account.getDisplayName();
            person_email=account.getEmail();
            person_pic= account.getPhotoUrl().toString();
               }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageButtonAdminSignIn:
                progressBar.setVisibility(View.VISIBLE);
                signIn();
                id_imgbtn_adm= v.getId();
                break;

            case R.id.imageButtonEmployeeSignIn:
                progressBar.setVisibility(View.VISIBLE);
                signIn();
                id_imgbtn_emp =v.getId();
                break;
        }
        id_imgbtn =v.getId();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    public void hideWidgets()
    {
        ibAdminSignIn.setVisibility(View.INVISIBLE);
        ibFacultySignIn.setVisibility(View.INVISIBLE);
        note.setVisibility(View.INVISIBLE);
    }

    public void showWidgets()
    {
        ibAdminSignIn.setVisibility(View.VISIBLE);
        ibFacultySignIn.setVisibility(View.VISIBLE);
        note.setVisibility(View.VISIBLE);
    }

    }