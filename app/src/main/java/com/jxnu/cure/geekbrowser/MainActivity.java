package com.jxnu.cure.geekbrowser;

import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class MainActivity extends BaseActivity
        implements HomeFragment.OnFragmentInteractionListener, View.OnClickListener{
    private HomeFragment homeFragment;
    private Dialog exitDialog;
    private PopupWindow popupWindow;
    private View indexMenu;
    private ImageButton navFor,navBack,navMenu,navPage,navHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragView_home);
        initialUI();

    }

    @Override
    protected void initialUI() {
        LayoutParams params;
        //宽度
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int iconWH = width/13;
        int margin = (int)((1.0*width-iconWH*5) / (2*5));
        navFor = findViewById(R.id.navigation_forward);
        navBack = findViewById(R.id.navigation_backward);
        navMenu = findViewById(R.id.navigation_menu);
        navPage = findViewById(R.id.navigation_page);
        navHome = findViewById(R.id.navigation_home);
        //点击事件
        findViewById(R.id.layout_navigation_backward).setOnClickListener(this);
        findViewById(R.id.layout_navigation_forward).setOnClickListener(this);
        findViewById(R.id.layout_navigation_menu).setOnClickListener(this);
        findViewById(R.id.layout_navigation_page).setOnClickListener(this);
        findViewById(R.id.layout_navigation_home).setOnClickListener(this);
        //设置长宽 & 图标对齐 & margin
        params = (LayoutParams) navFor.getLayoutParams();  params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams)navBack.getLayoutParams();  params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams)navMenu.getLayoutParams();  params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams)navPage.getLayoutParams();  params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams)navHome.getLayoutParams();  params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        navFor.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navMenu.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navPage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navHome.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //
        indexMenu = LayoutInflater.from(this).inflate(R.layout.menu_index,null);
        indexMenu.findViewById(R.id.index_menu_outside).setOnClickListener(this);
        indexMenu.findViewById(R.id.layout_index_menu_drop).setOnClickListener(this);
        params = (LayoutParams) indexMenu.findViewById(R.id.index_menu_drop).getLayoutParams();
        params.width = params.height = iconWH;
        //
        popupWindow = new PopupWindow( indexMenu,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,true);
        indexMenu.findViewById(R.id.index_menu_outside).getLayoutParams().height = height;
        popupWindow.setAnimationStyle(R.style.anim_menu);
        popupWindow.setTouchable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()){
            case MotionEvent.ACTION_BUTTON_PRESS:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * function: when touch back button,go back the forward website .
     **/
    @Override
    public void onBackPressed() {
        if(popupWindow.isShowing())
            popupWindow.dismiss();
        else if (homeFragment.canBack())
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
        WindowManager.LayoutParams attr;
        switch (view.getId()) {
            case R.id.button_exit_cancel:
                exitDialog.cancel();
                break;
            case R.id.button_exit_confirm:
                //TODO:退出前保存数据
                finish();
                System.exit(0);
                break;
            case R.id.layout_navigation_forward:
                onClickForward();
                break;
            case R.id.layout_navigation_backward:
                onClickBackward();
                break;
            case R.id.layout_navigation_menu:
                attr = getWindow().getAttributes();  attr.alpha = 0.8f;
                getWindow().setAttributes(attr);
                popupWindow.showAtLocation(navMenu,Gravity.BOTTOM,0,
                        findViewById(R.id.layout_navigation_backward).getHeight());
                break;
            case R.id.layout_navigation_page:
                break;
            case R.id.layout_navigation_home:
                onClickHome();
                break;
            //
            case R.id.index_menu_outside:
                popupWindow.dismiss();
                attr = getWindow().getAttributes();  attr.alpha = 1f;
                getWindow().setAttributes(attr);
                break;
            case R.id.layout_index_menu_drop:
                attr = getWindow().getAttributes();  attr.alpha = 1f;
                getWindow().setAttributes(attr);
                popupWindow.dismiss();
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
