package com.example.pelaporanbakesbangpolmalang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import com.example.pelaporanbakesbangpolmalang.fragment.HomeFragment;
import com.example.pelaporanbakesbangpolmalang.fragment.LaporanFragment;
import com.example.pelaporanbakesbangpolmalang.fragment.PasswordFragment;
import com.example.pelaporanbakesbangpolmalang.fragment.PemberitahuanFragment;
import com.example.pelaporanbakesbangpolmalang.fragment.ProfileFragment;
import com.example.pelaporanbakesbangpolmalang.fragment.UsersFragment;
import com.example.pelaporanbakesbangpolmalang.helper.SessionHelper;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SessionHelper sessionHelper;

    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    boolean doubleBackToExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // fungsi menginisialisasi menu
        init(savedInstanceState);
    }

    private void init(Bundle savedInstanceState) {
        sessionHelper = new SessionHelper(this);

        // setup toolbar
        toolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

        // setup navigation drawer
        drawer = findViewById(R.id.drawer_layout_admin);
        navigationView = findViewById(R.id.nav_view_admin);
        navigationView.setNavigationItemSelectedListener(this);

        // setup tombol burgerm menu
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        if (intent.hasExtra("GOTO_FRAGMENT")) {
            String activeFragment = getIntent().getStringExtra("GOTO_FRAGMENT");

            if (activeFragment.contentEquals("LAPORAN")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new LaporanFragment()).commit();
                getSupportActionBar().setTitle("Laporan");
                navigationView.setCheckedItem(R.id.menu_laporan);
            }

            if (activeFragment.contentEquals("PENGGUNA")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new UsersFragment()).commit();
                getSupportActionBar().setTitle("Pengguna");
                navigationView.setCheckedItem(R.id.menu_users);
            }

            if (activeFragment.contentEquals("PEMBERITAHUAN")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new PemberitahuanFragment()).commit();
                getSupportActionBar().setTitle("Pemberitahuan");
                navigationView.setCheckedItem(R.id.menu_pemberitahuan);
            }
        } else {
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Home");
                navigationView.setCheckedItem(R.id.menu_home);
            }
        }
    }

    // handling tombol back
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExit) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExit = true;
            Snackbar.make(findViewById(R.id.drawer_layout_admin), "Tekan kembali sekali lagi untuk keluar", Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExit = false;
                }
            }, 2000);
        }
    }

    // handling menu navigation selected
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new HomeFragment()).commit();
                getSupportActionBar().setTitle("Home");
                navigationView.setCheckedItem(R.id.menu_home);
                break;
            case R.id.menu_laporan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new LaporanFragment()).commit();
                getSupportActionBar().setTitle("Laporan");
                navigationView.setCheckedItem(R.id.menu_laporan);
                break;
            case R.id.menu_users:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new UsersFragment()).commit();
                getSupportActionBar().setTitle("Pengguna");
                navigationView.setCheckedItem(R.id.menu_users);
                break;
            case R.id.menu_pemberitahuan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new PemberitahuanFragment()).commit();
                getSupportActionBar().setTitle("Pemberitahuan");
                navigationView.setCheckedItem(R.id.menu_users);
                break;
            case R.id.menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new ProfileFragment()).commit();
                getSupportActionBar().setTitle("Profil");
                navigationView.setCheckedItem(R.id.menu_profile);
                break;
            case R.id.menu_password:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_admin, new PasswordFragment()).commit();
                getSupportActionBar().setTitle("Rubah Password");
                navigationView.setCheckedItem(R.id.menu_password);
                break;
            case R.id.menu_logout:
                if(sessionHelper.logout()) {
                    Intent toLogin = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(toLogin);
                    finish();
                } else {
                    Snackbar.make(findViewById(R.id.drawer_layout_admin), "Gagal memproses logout", Snackbar.LENGTH_SHORT).show();
                }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}