package com.jxnu.cure.geekbrowser;


import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity
        implements HomeFragment.OnFragmentInteractionListener, View.OnClickListener {
    private HomeFragment homeFragment;
    private Dialog exitDialog;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    onClickHome();
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
     * function: when touch back button,go back the forward website .
     **/
    @Override
    public void onBackPressed() {
        if (homeFragment.canBack())
            onClickBackward();
        else
            showExitDialog();
        //super.onBackPressed();
    }

    /**
     * 弹出退出菜单
     */
    public void showExitDialog() {
        if (exitDialog != null) { //已经创建则显示
            exitDialog.show();
            return;
        }
//        创建显示对象（确定显示格式，但内容未定）
        exitDialog = new Dialog(this, R.style.DialogShow);
//        创建显示的内容
        LinearLayout baseLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.exit_application_layout, null);
//        建立点击事件处理对象
        baseLayout.findViewById(R.id.button_exit_cancel).setOnClickListener(this);
        baseLayout.findViewById(R.id.button_exit_confirm).setOnClickListener(this);
//        给Dialog传入要显示的内容
        exitDialog.setContentView(baseLayout);
//        获取Window管理当前界面，从而实现对内容的显示设置
        Window dialogWindow = exitDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
//        x y 作为计算的起始点，全屏则为0
        layoutParams.x = 0;
        layoutParams.y = 0;
//        测量内容占用大小从而赋值
        baseLayout.measure(0, 0);
        layoutParams.height = baseLayout.getMeasuredHeight();
        layoutParams.width = baseLayout.getMeasuredWidth();
        dialogWindow.setAttributes(layoutParams);
        exitDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
    public void onClickHome() {  homeFragment.goHome();}

    @Override
    public void onClickBackward() {
        homeFragment.backPage();
    }

    @Override
    public void onClickForward() {
        homeFragment.forPage();
    }
}
