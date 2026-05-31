package com.gymly.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gymly.R;
import com.gymly.data.local.SessionManager;
import com.gymly.ui.auth.LoginActivity;
import com.gymly.ui.checkin.CheckInFragment;
import com.gymly.ui.classes.ClassesFragment;
import com.gymly.ui.home.HomeFragment;
import com.gymly.ui.profile.ProfileFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SessionManager.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = getFragmentForItem(item.getItemId());
            if (fragment == null) {
                return false;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            toolbar.setTitle(item.getTitle());
            return true;
        });

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private Fragment getFragmentForItem(int itemId) {
        if (itemId == R.id.nav_home) {
            return new HomeFragment();
        } else if (itemId == R.id.nav_classes) {
            return new ClassesFragment();
        } else if (itemId == R.id.nav_checkin) {
            return new CheckInFragment();
        } else if (itemId == R.id.nav_profile) {
            return new ProfileFragment();
        }
        return null;
    }
}
