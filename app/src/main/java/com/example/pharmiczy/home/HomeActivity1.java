package com.example.pharmiczy.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pharmiczy.MyOrdersFragment;
import com.example.pharmiczy.R;
import com.example.pharmiczy.home.activity.SearchActivity;
import com.example.pharmiczy.home.fragmentss.CartFragment;
import com.example.pharmiczy.home.fragmentss.HomeFragment;
import com.example.pharmiczy.loginandsignup.SignupActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class HomeActivity1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String OPEN_CART_FRAGMENT = "open_cart_fragment";
    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int ORDERS_FRAGMENT = 2;
    private static final int WISHLIST_FRAGMENT = 3;
    private static final int REWARDS_FRAGMENT = 4;
    private static final int ACCOUNT_FRAGMENT = 5;
    public static Boolean showCart = false;
    private FrameLayout frameLayout;
    private ImageView actionBarLogo,sb;
    private int currentFragment = -1;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.actionbar_logo);
//        setSupportActionBar(toolbar);
        sb=findViewById(R.id.sb);
        sb.setOnClickListener(v -> {
            Intent intent=new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        frameLayout = findViewById(R.id.main_framelayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.green2));
//            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.green));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                actionBarLogo.setVisibility(View.VISIBLE);
                setFragment(new HomeFragment(), HOME_FRAGMENT);
                return true;
            } else if (id == R.id.navigation_upload) {
                gotoFragment("Cart", new CartFragment(), CART_FRAGMENT);
                return true;
            } else if (id == R.id.navigation_profile) {
                gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
                return true;
            }
            return false;
        });


        if (showCart) {
//            drawer.setDrawerLockMode();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            gotoFragment("My Cart", new MyCartFragment(), -2);
        } else {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
//
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        }
        if (getIntent().getBooleanExtra(OPEN_CART_FRAGMENT, false)) {
            loadCartFragment();
        } else {
            // load default fjrjagmentmj
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentFragment == HOME_FRAGMENT) {
                currentFragment = -1;
                super.onBackPressed();
            } else {
                if (showCart) {
                    showCart = false;
                    finish();
                } else {
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu();
                    setFragment(new HomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search_icon) {
            //todo:search
            return true;
        } else if (id == R.id.main_notification_icon) {
            //todo:notifications
            return true;
        } else if (id == R.id.main_cart_icon) {
            final Dialog signInDialog = new Dialog(HomeActivity1.this);
            signInDialog.setContentView(R.layout.sign_in_dialog);
            signInDialog.setCancelable(true);
            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            Button dialogSignInBtn = signInDialog.findViewById(R.id.dialog_sign_in_btn);
            Button dialogSignUpBtn = signInDialog.findViewById(R.id.dialog_sign_up_btn);
            final Intent registerIntent = new Intent(HomeActivity1.this, SignupActivity.class);
            dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    signInDialog.dismiss();
//                    setSignUpFragment =false;
//                    startActivity(registerIntent);
                }
            });
            dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInDialog.dismiss();
//                    setSignUpFragment =true;
//                    startActivity(registerIntent);
                }
            });
            signInDialog.show();
            //gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            return true;
        } else if (id == android.R.id.home) {
            if (showCart) {
                showCart = false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoFragment(String title, Fragment fragment, int fragmentNo) {
        if (actionBarLogo != null) {
            actionBarLogo.setVisibility(View.GONE);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(title);
        }

        invalidateOptionsMenu();
        setFragment(fragment, fragmentNo);

        if (fragmentNo == CART_FRAGMENT && navigationView != null) {
            navigationView.getMenu().getItem(3).setChecked(true);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_my_Home) {
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu();
            setFragment(new HomeFragment(), HOME_FRAGMENT);
        } else if (id == R.id.nav_my_orders) {
            gotoFragment("My Orders", new MyOrdersFragment(), ORDERS_FRAGMENT);
        } else if (id == R.id.nav_my_rewards) {
//            gotoFragment("My Rewards", new MyRewardsFragment(), REWARDS_FRAGMENT);
        } else if (id == R.id.nav_my_cart) {
            gotoFragment("My Cart", new CartFragment(), CART_FRAGMENT);

        } else if (id == R.id.nav_my_wishlist) {
//            gotoFragment("My Wishlist", new MyWishlistFragment(), WISHLIST_FRAGMENT);
        } else if (id == R.id.nav_my_account) {
//            gotoFragment("My Account", new MyAccountFragment(), ACCOUNT_FRAGMENT);
        } else if (id == R.id.nav_sign_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Fragment fragment, int fragmentNo) {

        if (fragmentNo != currentFragment) {
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }

    }
    public void loadCartFragment() {
        Log.d("Recvd","lk)");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_framelayout, new CartFragment())
                .addToBackStack(null)
                .commit();
    }
}
