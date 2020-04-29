package com.personal.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().replace(R.id.framecontent,new homemain()).commit();
        }

        bottomBar=findViewById(R.id.bottomBar);

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelect(int i) {
                switch (i)
                {
                    case 0:
                        getSupportFragmentManager().
                                beginTransaction().replace(R.id.framecontent,new homemain()).commit();
                        break;

                    case 1:
                        getSupportFragmentManager().
                                beginTransaction().replace(R.id.framecontent,new homeFragment()).commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent,new resource()).commit();
                        break;
                    case 3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framecontent,new appointment()).commit();
                        break;

                }



            }
        });
    }
}
