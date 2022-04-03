package com.example.dialist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;

public class edit_chkbox_content extends AppCompatActivity {

    String content;
    static Context context_editchk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chkbox_content);
        context_editchk = this;

        EditText editText = findViewById(R.id.chk_content);
        Button button = findViewById(R.id.chk_insert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //content = editText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("cntt", editText.getText().toString());
                setResult(111, intent);  //?
                finish();
            }
        });
    }
}