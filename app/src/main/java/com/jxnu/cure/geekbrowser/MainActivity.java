package com.jxnu.cure.geekbrowser;


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity
    implements HomeFragment.OnFragmentInteractionListener,View.OnClickListener {
    private HomeFragment homeFragment;
    private Dialog exitDialog;

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
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragView_home);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    /**
     *function: when touch back button,go back the forward website .
     **/
    @Override
    public void onBackPressed() {
        if(homeFragment.canBack())
            onClickBackward();
        else
            showExitDialog();
        //super.onBackPressed();
    }

    /**
     * 弹出退出菜单
     * */
    public void showExitDialog(){
        exitDialog = new Dialog(this,R.style.DialogShow);
        LinearLayout baseLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.exit_application_layout,null);
        baseLayout.findViewById(R.id.button_exit_cancel).setOnClickListener(this);
        baseLayout.findViewById(R.id.button_exit_confirm).setOnClickListener(this);
        exitDialog.setContentView(baseLayout);
        Window dialogWindow = exitDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        baseLayout.measure(0,0);
        layoutParams.height = baseLayout.getMeasuredHeight();
        layoutParams.width = baseLayout.getMeasuredWidth();
        dialogWindow.setAttributes(layoutParams);
        exitDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_exit_cancel:
                exitDialog.cancel();
                break;
            case R.id.button_exit_confirm:
                finish();
                onDestroy();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClickBackward() {
        homeFragment.backPage();
    }

    @Override
    public void onClickForward() {
        homeFragment.forPage();
    }
}
