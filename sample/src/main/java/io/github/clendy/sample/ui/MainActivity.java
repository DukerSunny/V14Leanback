package io.github.clendy.sample.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.github.clendy.sample.R;

public class MainActivity extends Activity implements View.OnClickListener {

    Button mVerticalBtn;
    Button mHorizontalBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVerticalBtn = ((Button) findViewById(R.id.vertical));
        mHorizontalBtn = ((Button) findViewById(R.id.horizontal));
        mVerticalBtn.setOnClickListener(this);
        mHorizontalBtn.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vertical:
                startActivity(new Intent(MainActivity.this, VerticalActivity.class));
                break;
            case R.id.horizontal:
                startActivity(new Intent(MainActivity.this, HorizontalActivity.class));
                break;
        }
    }
}
