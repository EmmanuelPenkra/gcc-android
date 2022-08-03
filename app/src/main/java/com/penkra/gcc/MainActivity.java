package com.penkra.gcc;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.penkra.gcc.Helpers.Actions;
import com.squareup.picasso.Picasso;

import static com.penkra.gcc.Helpers.Constants.DOMAIN;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View nav_header = navigationView.getHeaderView(0);
        RoundedImageView imageView = nav_header.findViewById(R.id.user_image);
        TextView user_name = nav_header.findViewById(R.id.user_name);
        TextView company_name = nav_header.findViewById(R.id.company_name);

        user_name.setText(Actions.instance.storage.getString("fname", "Anonymous") + " " +
                Actions.instance.storage.getString("lname", ""));
        company_name.setText(Actions.instance.storage.getString("bis_name", "No Business Yet"));
        String image = Actions.instance.storage.getString("icon", "");
        if(!image.equals("")) Picasso.get()
                .load(DOMAIN + image)
                .placeholder(R.drawable.ic_image)
                .resize(100, 100)
                .centerCrop()
                .into(imageView);

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            switch (menuItem.getItemId()){
                case R.id.nav_businesses:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new BusinessesFragment()).commit();
                    break;
                case R.id.nav_favorites:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FavoritesFragment()).commit();
                    break;
                case R.id.nav_account:
                    if(Actions.instance.storage.getBoolean("isLoggedIn", false))
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new UserAccountFragment()).commit();
                    else {
                        startActivity(new Intent(this, SignInActivity.class));
                        return false;
                    }
                    break;
                case R.id.nav_profile:
                    if(Actions.instance.storage.getBoolean("hasBusiness", false))
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new BusinessProfileFragment()).commit();
                    else {
                        if(Actions.instance.storage.getBoolean("isLoggedIn", false))
                            startActivity(new Intent(this, NewBusinessActivity.class));
                        else startActivity(new Intent(this, SignInActivity.class));
                        return false;
                    }
                    break;
                case R.id.nav_payments:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PaymentHistoryFragment()).commit();
                    break;
                case R.id.nav_help:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HelpFragment()).commit();
                    break;
            }
            return true;
        });

        if(savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_businesses);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new BusinessesFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}
