package com.jxnu.cure.geekbrowser;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements PageFragment.OnFragmentInteractionListener, View.OnClickListener{
    private ImageButton navFor, navBack, navMenu, navPage, navHome;
    private LinearLayout navLayFor, navLayBack, navLayMenu, navLayPage, navLayHome;
    private ActHandler handler = new ActHandler(this);
    private Dialog dialogExit, dialogPageMenu;
    private PopupWindow popupWindow;
    private View indexMenu;
    private int iconWH,screenW,screenH;
    //页面集合
    private ArrayList<PageFragment> pageFragmentArrayList = new ArrayList<PageFragment>();
    private int currentPageNum;
    private GestureDetector gestureDetector;

    static class ActHandler extends Handler implements Serializable {
        private MainActivity act;

        protected ActHandler(MainActivity act) {
            this.act = act;
        }

        protected void notifyNavButtonState() {
            act.detectForwardAndBack(act.currentPageNum);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentPageNum = 0;
        pageFragmentArrayList.add(
                (PageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_page));
        Bundle bundle = new Bundle();
        bundle.putSerializable("ActHandler", handler);
        pageFragmentArrayList.get(0).setArguments(bundle);
        initialUI();
    }

    @Override
    protected void initialUI() {
        LayoutParams params;
        //宽度
        screenW = getResources().getDisplayMetrics().widthPixels;
        screenH = getResources().getDisplayMetrics().heightPixels;
        iconWH = screenW / 13;
        int margin = (int) ((1.0 * screenW - iconWH * 5) / (2 * 5));
        navFor = findViewById(R.id.navigation_forward);
        navBack = findViewById(R.id.navigation_backward);
        navMenu = findViewById(R.id.navigation_menu);
        navPage = findViewById(R.id.navigation_page);
        navHome = findViewById(R.id.navigation_home);
        //点击事件
        navLayBack = findViewById(R.id.layout_navigation_backward);
        navLayBack.setOnClickListener(this);
        navLayFor = findViewById(R.id.layout_navigation_forward);
        navLayFor.setOnClickListener(this);
        navLayMenu = findViewById(R.id.layout_navigation_menu);
        navLayMenu.setOnClickListener(this);
        navLayPage = findViewById(R.id.layout_navigation_page);
        navLayPage.setOnClickListener(this);
        navLayHome = findViewById(R.id.layout_navigation_home);
        navLayHome.setOnClickListener(this);
        //设置长宽 & 图标对齐 & margin
        params = (LayoutParams) navFor.getLayoutParams();
        params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams) navBack.getLayoutParams();
        params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams) navMenu.getLayoutParams();
        params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams) navPage.getLayoutParams();
        params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        params = (LayoutParams) navHome.getLayoutParams();
        params.width = params.height = iconWH;
        params.leftMargin = params.rightMargin = margin;
        navFor.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navMenu.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navPage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navHome.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //
        initialMenu();
        initialGesture();
        initialPageMenu();
    }

    protected void initialMenu() {

        indexMenu = LayoutInflater.from(this).inflate(R.layout.menu_index, null);
        indexMenu.findViewById(R.id.index_menu_outside).setOnClickListener(this);
        indexMenu.findViewById(R.id.layout_index_menu_drop).setOnClickListener(this);
        //调整矢量图尺寸
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_keep)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_bookmark)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_download)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_renovate)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_share)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_about)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_setting)).setBackgroundSize(1, iconWH, iconWH);
        ((AdjustDrawButton) indexMenu.findViewById(R.id.button_menu_exit)).setBackgroundSize(1, iconWH, iconWH);
        //监听器
        indexMenu.findViewById(R.id.button_menu_keep).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_bookmark).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_download).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_renovate).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_share).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_about).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_setting).setOnClickListener(this);
        indexMenu.findViewById(R.id.button_menu_exit).setOnClickListener(this);
        //
        LayoutParams params = (LayoutParams) indexMenu.findViewById(R.id.index_menu_drop).getLayoutParams();
        params.width = params.height = iconWH;
        //
        popupWindow = new PopupWindow(indexMenu, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
        indexMenu.findViewById(R.id.index_menu_outside).getLayoutParams().height =
                getResources().getDisplayMetrics().heightPixels;
        popupWindow.setAnimationStyle(R.style.anim_menu);
        popupWindow.setTouchable(true);
    }

    protected void initialGesture(){
        gestureDetector =  new GestureDetector(this, new GestureDetector.OnGestureListener() {
            /**
             *  轻击事件,如果这个过程中产生了onLongPress、onScroll和onFling事件，就不会产生onSingleTapUp事件。
             * */
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            /**
             * 当长按事件发生时(通过OnGestureListener接口实现)
             */
            @Override
            public void onLongPress(MotionEvent e) {
                showPageDialog(e.getX(),e.getY());
            }

            /**
             *   点击了触摸屏，但是没有移动和弹起的动作。onShowPress和onDown的区别在于
             *   onDown是，一旦触摸屏按下，就马上产生onDown事件，但是onShowPress是onDown事件产生后，
             *   一段时间内，如果没有移动鼠标和弹起事件，就认为是onShowPress事件。
             * */
            @Override
            public void onShowPress(MotionEvent e) {;}

            /**
             *      滚动事件，当在触摸屏上迅速的移动，会产生onScroll。由ACTION_MOVE产生
             *      e1：第1个ACTION_DOWN MotionEvent
             *      e2：最后一个ACTION_MOVE MotionEvent
             *      distanceX：距离上次产生onScroll事件后，X抽移动的距离
             *      distanceY：距离上次产生onScroll事件后，Y抽移动的距离
             * */
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            /**
             * 用户按下触摸屏、快速移动后松开,这个时候，你的手指运动是有加速度的。
             * 由1个MotionEvent ACTION_DOWN,
             * 多个ACTION_MOVE, 1个ACTION_UP触发
             * e1：第1个ACTION_DOWN MotionEvent
             * e2：最后一个ACTION_MOVE MotionEvent
             * velocityX：X轴上的移动速度，像素/秒
             * velocityY：Y轴上的移动速度，像素/秒
             * */
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }
        });
    }

    private void initialPageMenu(){
        View layout = LayoutInflater.from(this).inflate(R.layout.dialog_page_menu, null);
        layout.findViewById(R.id.text_view_page_select_text).setOnClickListener(this);
        layout.findViewById(R.id.text_view_page_select_bookmark).setOnClickListener(this);
        layout.findViewById(R.id.text_view_page_select_index).setOnClickListener(this);
        layout.findViewById(R.id.text_view_page_select_tools).setOnClickListener(this);
        //菜单
        dialogPageMenu = new Dialog(this, R.style.DialogPageMenu);
        dialogPageMenu.setContentView(layout);
    }

    /**
     * function: when touch back button,go back the forward website .
     **/
    @Override
    public void onBackPressed() {
        if (popupWindow.isShowing())
            popupWindow.dismiss();
        else if (pageFragmentArrayList.get(currentPageNum).canBack())
            pageFragmentArrayList.get(currentPageNum).backPage();
        else if(!pageFragmentArrayList.get(currentPageNum).isIndexVisible())
            pageFragmentArrayList.get(currentPageNum).changeVisible(PageFragment.SHOW_INDEX);
        else
            showExitDialog();
        //super.onBackPressed();
    }

    /**
     * 弹出退出菜单
     */
    public void showExitDialog() {
        if (dialogExit != null) { //已经创建则显示
            dialogExit.show();
            return;
        }
//        创建显示对象（确定显示格式，但内容未定）
        dialogExit = new Dialog(this, R.style.DialogExit);
//        创建显示的内容
        LinearLayout baseLayout = (LinearLayout) LayoutInflater.from(this)
                .inflate(R.layout.dialog_exit_application, null);
//        建立点击事件处理对象
        baseLayout.findViewById(R.id.button_exit_cancel).setOnClickListener(this);
        baseLayout.findViewById(R.id.button_exit_confirm).setOnClickListener(this);
//        给Dialog传入要显示的内容
        dialogExit.setContentView(baseLayout);
//        获取Window管理当前界面，从而实现对内容的显示设置
        Window dialogWindow = dialogExit.getWindow();
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
        dialogExit.show();
    }

    /**
     * 弹出页面菜单
     */
    protected void showPageDialog(float x, float y) {
        if(dialogPageMenu==null)
            initialPageMenu();
        //TODO：确定显示位置而后显示
        Window window = dialogPageMenu.getWindow();
        WindowManager.LayoutParams attri = window.getAttributes();
        View root = dialogPageMenu.findViewById(R.id.layout_page_menu);
        root.measure(0, 0);
        int titleH = (findViewById(R.id.layout_input_website)).getHeight();
        //如果越界
        if (x + root.getWidth() > screenW)
            x -= root.getWidth();
        if (y + root.getHeight() > screenH-titleH)
            y -= root.getHeight();
        //执行设置
        attri.x = (int)x-root.getWidth();
        attri.y = (int)y-root.getHeight()-titleH;
//        Toast.makeText(this,"x="+x+" y="+y+" w="+root.getWidth()+" h="+root.getHeight(),Toast.LENGTH_SHORT).show();
        window.setAttributes(attri);
        dialogPageMenu.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(gestureDetector.onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_exit_cancel:
                dialogExit.cancel();
                break;
            case R.id.button_exit_confirm:
                //TODO:退出前保存数据
                finish();
                System.exit(0);
                break;
            case R.id.layout_navigation_forward:
                pageFragmentArrayList.get(currentPageNum)
                        .forPage();
                //检测能否前进后退并进行相应处理
                detectForwardAndBack(currentPageNum);
                break;
            case R.id.layout_navigation_backward:
                pageFragmentArrayList.get(currentPageNum)
                        .backPage();
                //检测能否前进后退并进行相应处理
                detectForwardAndBack(currentPageNum);
                break;
            case R.id.layout_navigation_menu:
                showMenu();
                break;
            case R.id.layout_navigation_page:
                break;
            case R.id.layout_navigation_home:
                onClickHome();
                break;
            //以下为菜单选项处理
            case R.id.index_menu_outside:
                cancelMenu();
                break;
            case R.id.layout_index_menu_drop:
                cancelMenu();
                break;
            case R.id.button_menu_keep:

                cancelMenu();
                break;
            case R.id.button_menu_bookmark:

                cancelMenu();
                break;
            case R.id.button_menu_download:

                cancelMenu();
                break;
            case R.id.button_menu_renovate:
                pageFragmentArrayList.get(currentPageNum).renovate();
                cancelMenu();
                break;
            case R.id.button_menu_share:

                cancelMenu();
                break;
            case R.id.button_menu_about:

                cancelMenu();
                break;
            case R.id.button_menu_setting:

                cancelMenu();
                break;
            case R.id.button_menu_exit:
                //TODO:退出前保存数据
                cancelMenu();
                finish();
                System.exit(0);
                break;

            //接下来是page menu点击事件
        }
    }

    protected void cancelMenu() {
        if (!popupWindow.isShowing())
            return;
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        popupWindow.dismiss();
        attr = getWindow().getAttributes();
        attr.alpha = 1f;
        getWindow().setAttributes(attr);
    }

    protected void showMenu() {
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.alpha = 0.7f;
        getWindow().setAttributes(attr);
        popupWindow.showAtLocation(navMenu, Gravity.BOTTOM, 0,
                findViewById(R.id.layout_navigation_backward).getHeight());
    }

    protected GestureDetector getGestureDetector(){
        return gestureDetector;
    }

    protected void detectForwardAndBack(int position) {
//        if(pageFragmentArrayList.get(position).canFor()
//                || (pageFragmentArrayList.get(position).isIndexVisible() && pageFragmentArrayList.get(position).canFor())){
//            navLayFor.setEnabled(true);
//            navFor.getBackground().setBounds(0,0,iconWH,iconWH);
//        }else{
//            navLayFor.setEnabled(false);
//            navFor.getBackground().setBounds(iconWH/4,iconWH/4,iconWH*3/4,iconWH*3/4);
//        }
//        if(pageFragmentArrayList.get(position).canBack()
//                || !pageFragmentArrayList.get(position).isIndexVisible()){
//            navLayBack.setEnabled(true);
//            navBack.getBackground().setBounds(0,0,iconWH,iconWH);
//        }else{
//            navLayBack.setEnabled(false);
//            navBack.getBackground().setBounds(iconWH/4,iconWH/4,iconWH*3/4,iconWH*3/4);
//        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClickHome() {
        pageFragmentArrayList.get(currentPageNum)
                .changeVisible(PageFragment.SHOW_INDEX);

    }

}
