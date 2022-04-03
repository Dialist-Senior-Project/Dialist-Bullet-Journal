package com.example.dialist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;
    private  FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //회원가입 밑줄
        TextView signup = (TextView)findViewById(R.id.signup);
        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //비밀번호 찾기 밑줄
        TextView passwordreset = (TextView)findViewById(R.id.passwordreset);
        passwordreset.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        //회원가입 클릭 이벤트
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SignUp.class);
                startActivity(intent);
            }
        });

        //비밀번호 찾기 클릭 이벤트
        passwordreset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), PasswordReset.class);
                startActivity(intent);
            }
        });

        //로그인 클릭 이벤트
        EditText editTextID = (EditText)findViewById(R.id.editTextID);
        EditText editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        Button signinButton = (Button)findViewById(R.id.signinButton);
        signinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.signinButton:
                        signIn(editTextID.getText().toString(), editTextPassword.getText().toString());
                        break;
                }
            }
        });

        signInButton = findViewById(R.id.GooglesignInButton);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Toast.makeText(MainActivity.this, "로그인 성공3", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), First.class);
            startActivity(intent);
            finish();
        }
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GooglesignIn();
            }
        });
    }
    // [START signin]
    private void GooglesignIn() {
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
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            Toast.makeText(MainActivity.this, "로그인 성공1", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, First.class);
            startActivity(intent);
            finish();
        }
    }

    private void signIn(String id, String password){
        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "로그인 성공2", Toast.LENGTH_SHORT).show();
                            mAuth.addAuthStateListener(firebaseAuthListener);
                            Intent intent = new Intent(getApplication(), First.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                            Toast.makeText(MainActivity.this, "아이디 또는 비밀번호 틀림", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}