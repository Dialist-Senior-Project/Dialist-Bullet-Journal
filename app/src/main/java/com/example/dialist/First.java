package com.example.dialist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class First extends AppCompatActivity implements View.OnClickListener {
    Button logoutButton;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        logoutButton = (Button)findViewById(R.id.logoutButton);

        mAuth = FirebaseAuth.getInstance();

        logoutButton.setOnClickListener(this);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                signOut();
                finishAffinity();
                break;
        }
    }
}