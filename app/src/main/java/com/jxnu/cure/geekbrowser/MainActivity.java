package com.jxnu.cure.geekbrowser;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
    implements HomeFragment.OnFragmentInteractionListener {


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_backward:
                    onClickBackward();
                    return true;
                case R.id.navigation_forward:
                    onClickForward();
                    return true;
                case R.id.navigation_menu:

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClickBackward() {
        ((HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragView_home)).backPage();
    }

    @Override
    public void onClickForward() {
        ((HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragView_home)).forPage();
    }
}
