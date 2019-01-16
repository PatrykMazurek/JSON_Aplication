package com.example.patryyyk21.json_aplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddJsonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_json);

        final EditText etId = (EditText)findViewById(R.id.et_Id);
        final EditText etNick = (EditText)findViewById(R.id.et_Nick);
        final EditText etPost = (EditText)findViewById(R.id.et_Text);
        Button bSave = (Button)findViewById(R.id.b_Save);
        Button bPrev = (Button)findViewById(R.id.b_prev);
        final JSONAsyncTask jsonAsyncTask = new JSONAsyncTask(this);

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish();
            }
        });
    }
}
