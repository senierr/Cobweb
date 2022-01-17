package com.senierr.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.senierr.cobweb.Cobweb;
import com.senierr.cobweb.DataObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cobweb.initialize(getApplication());

        Button btnTest = findViewById(R.id.btn_test);
        btnTest.setOnClickListener(v -> {
//            Cobweb.with("/test")
//                    .putString("key", "value")
//                    .build()
//                    .call();
//
            Cobweb.with("/test")
                    .putString("key", "value")
                    .build()
                    .startActivity(this);
//
//            Fragment fragment = Cobweb.with("/test")
//                    .putString("key", "value")
//                    .build()
//                    .obtainFragment();
//
//            View view = Cobweb.with("/test")
//                    .putString("key", "value")
//                    .build()
//                    .obtainView();
//            Log.e("MainActivity", "view: " + view);

//            String data = Cobweb.with("/test")
//                    .putString("key", "value")
//                    .build()
//                    .fetchData();
//            Log.e("MainActivity", "data: " + data);
//
//            Cobweb.with("/test")
//                    .putString("key", "value")
//                    .build()
//                    .observeData(new DataObserver() {
//                        @Override
//                        public void onChanged(Object value) {
//                            Log.e("MainActivity", "data: " + value);
//                        }
//                    }, true);
        });
    }
}
