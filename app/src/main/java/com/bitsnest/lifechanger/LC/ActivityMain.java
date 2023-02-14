package com.bitsnest.lifechanger.LC;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bitsnest.lifechanger.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ActivityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


//        BadgeDrawable badge =navView.getOrCreateBadge(R.id.navigation_home);
//        badge.setBackgroundColor(Color.RED);
//        badge.setBadgeTextColor(Color.WHITE);
//        badge.setMaxCharacterCount(3);
//        badge.setNumber(10);
//        badge.setVisible(true);



//        BadgeDrawable badgeDrawable =  BadgeDrawable.create(getContext());
//        badgeDrawable.setNumber(3);
//        badgeDrawable.setVisible(true);
    }

}