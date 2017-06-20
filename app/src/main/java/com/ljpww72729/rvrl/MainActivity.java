package com.ljpww72729.rvrl;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ljpww72729.rvrl.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainFragment mainFragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        mainFragment = (MainFragment) fragmentManager.findFragmentById(R.id.content_frag);
        if (mainFragment == null) {
            mainFragment = MainFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.content_frag, mainFragment).commit();
        }
    }
}
