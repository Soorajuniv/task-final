package uk.ac.tees.aad.studentNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class demo extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Intent intent=getIntent();
        textView=findViewById(R.id.textView2);
        textView.setText(intent.getStringExtra("error"));
    }
}