package com.farhakhan.majulams;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ibAdminSignIn;
    ImageButton ibFacultySignIn;
    ProgressBar progressBar;
    TextView note;
    private int RC_SIGN_IN=1;
    GoogleSignInClient mGoogleSignInClient;
    private String TAG="SignInActivity";
    private FirebaseAuth mAuth;
    int i, j,k;

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

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        ibAdminSignIn =findViewById(R.id.imageButtonAdminSignIn);
        ibFacultySignIn= findViewById(R.id.imageButtonFacultySignIn);
        note= findViewById(R.id.txt_note);
        mAuth=FirebaseAuth.getInstance();
        ibAdminSignIn.setOnClickListener(this);
        ibFacultySignIn.setOnClickListener(this);

    }
    private void signIn() {
        ibAdminSignIn.setVisibility(View.GONE);
        ibFacultySignIn.setVisibility(View.GONE);
        note.setVisibility(View.GONE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            progressBar.setVisibility(View.GONE);
                            if (k==i)
                            startActivity(new Intent(SignInActivity.this,AdminMainActivity.class));
                            else if (k==j)
                                startActivity(new Intent(SignInActivity.this, FacultyMainActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                            updateUI(null);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account!=null)
        {
            String person_name= account.getDisplayName();
            String person_email=account.getEmail();
            String person_id = account.getId();
            Uri person_pic= account.getPhotoUrl();

            Toast.makeText(getApplicationContext(),"welcome "+ person_name, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imageButtonAdminSignIn:
                progressBar.setVisibility(View.VISIBLE);
                signIn();
                i= v.getId();
                break;

            case R.id.imageButtonFacultySignIn:
                progressBar.setVisibility(View.VISIBLE);
                signIn();
                j=v.getId();
                break;

        }
        k=v.getId();
    }
}