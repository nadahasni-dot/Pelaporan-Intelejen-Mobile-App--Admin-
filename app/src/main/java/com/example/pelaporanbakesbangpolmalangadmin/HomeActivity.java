package com.example.pelaporanbakesbangpolmalang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        drawer = findViewById(R.id.drawer_layout_admin);
        NavigationView navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.menu_home);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Home");
                break;
            case R.id.menu_laporan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new LaporanFragment()).commit();
                getSupportActionBar().setTitle("Laporan");
                break;
            case R.id.menu_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new UsersFragment()).commit();
                getSupportActionBar().setTitle("Pengguna");
                break;
            case R.id.menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ProfileFragment()).commit();
                getSupportActionBar().setTitle("Profil");
                break;
            case R.id.menu_password:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new PasswordFragment()).commit();
                getSupportActionBar().setTitle("Rubah Password");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}