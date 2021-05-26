package com.example.shoppingCart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG_MainActivity";
    private boolean isMessageContentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int id = destination.getId();
            if (id == R.id.login || id == R.id.register || id == R.id.takePhoto || id == R.id.photoLogin) {
                isMessageContentFragment = false;
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                bottomNavigationView.setVisibility(View.GONE);
            } else {
                isMessageContentFragment = false;
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });

        bottomNavigationView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect r = new Rect();
            bottomNavigationView.getWindowVisibleDisplayFrame(r);
            if (isMessageContentFragment) {
                if (bottomNavigationView.getRootView().getHeight() - (r.bottom - r.top) > 500) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

}