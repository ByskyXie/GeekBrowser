package com.jxnu.cure.geekbrowser;


import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import java.util.zip.Inflater;

public class MainActivity extends BaseActivity
        implements HomeFragment.OnFragmentInteractionListener, View.OnClickListener {
    private HomeFragment homeFragment;
    private Dialog exitDialog;
    private ImageButton navFor,navBack,navMenu,navPage,navHome;

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
//                    PopupMenu popupMenu = new PopupMenu( context,navigation);
//                    popupMenu.getMenuInflater().inflate(R.menu.navigation,popupMenu.getMenu());
////                    popupMenu.setOnMenuItemClickListener();
//                    popupMenu.show();
//                    IndexMenuAttributeSet set = new IndexMenuAttributeSet();

//                    final PopupWindow popupWindow = new PopupWindow(LayoutInflater.from(getActivity()).inflate(R.layout.menu_index,null)
//                            , 500, 300,true);
//                    popupWindow.setTouchable(true);
//                    popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_border));
//                    popupWindow.showAsDropDown(etWebsite);
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

//        //导航栏
//        BottomNavigationView navigation =  findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void initialUI() {
        View view = findViewById(R.id.layout_menu_bar);
        ViewGroup.LayoutParams params;
        int width = (getWindow().getWindowManager().getDefaultDisplay().getWidth())/5;
        Log.e("1=============1",width+"");
        navFor = findViewById(R.id.navigation_forward);     navFor.setOnClickListener(this);
        navBack = findViewById(R.id.navigation_backward);   navBack.setOnClickListener(this);
        navMenu = findViewById(R.id.navigation_menu);       navMenu.setOnClickListener(this);
        navPage = findViewById(R.id.navigation_page);       navPage.setOnClickListener(this);
        navHome = findViewById(R.id.navigation_home);       navHome.setOnClickListener(this);
        navFor.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navMenu.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navPage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navHome.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //
        params = navFor.getLayoutParams();  params.width = params.height = width;
        params = navBack.getLayoutParams();  params.width = params.height = width;
        params = navMenu.getLayoutParams();  params.width = params.height = width;
        params = navPage.getLayoutParams();  params.width = params.height = width;
        params = navHome.getLayoutParams();  params.width = params.height = width;

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
                //TODO:退出前保存数据
                finish();
                System.exit(0);
                break;
            case R.id.navigation_forward:
                onClickForward();
                break;
            case R.id.navigation_backward:
                onClickBackward();
                break;
            case R.id.navigation_menu:
                break;
            case R.id.navigation_page:
                break;
            case R.id.navigation_home:
                onClickHome();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClickHome() {  homeFragment.changeVisible(HomeFragment.SHOW_INDEX);}

    @Override
    public void onClickBackward() {
        homeFragment.backPage();
    }

    @Override
    public void onClickForward() {
        homeFragment.forPage();
    }
}
