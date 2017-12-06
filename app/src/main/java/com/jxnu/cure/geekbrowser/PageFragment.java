package com.jxnu.cure.geekbrowser;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment
    implements View.OnClickListener{
    final static int SHOW_INDEX = 0;
    final static int SHOW_WEB = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentWebClient myClient; //网址代理
    private WebSettings webSettings;//网页显示的设置
    private EditText etWebsite;    //网址输入框
    private WebView pageWebView;   //网页显示控件
    private Button connectButton;  //确定键
    private LinearLayout web_navy;
    private int iconWH;
    //本地网页
    private RecyclerView recycler_sys;
    private ArrayList<IndexItem> item_list_sys;
    private RecyclerSysWebAdapter.OnItemClickListener onItemClickListener_sys;
    //用户收藏网页
    private RecyclerView recycler_user;
    private ArrayList<IndexItem> item_list_user;
    private RecyclerIndexAdapter.OnItemClickListener onItemClickListener_user;
    //传递消息
    private MainActivity.ActHandler handler;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    class FragmentWebClient extends WebViewClient{
        ///用于防止调用其它浏览器打开Url
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(pageWebView!=null)
                view.reload();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //TODO：加载完成、执行去广告操作
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            //TODO:网页加载进度条
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  //等待证书响应
        }
    }

    class FragmentWebChromeClient extends WebChromeClient{

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress < 100){
                String pro = newProgress+"%";
                //这里写要展示进度的UI处理
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {

        }
    }

    public PageFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PageFragment newInstance(String param1, String param2) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_connect:
                loadURL(etWebsite.getText().toString(),pageWebView);
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myClient = new FragmentWebClient();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        iconWH = (getResources().getDisplayMetrics().widthPixels/13);
        Button button = view.findViewById(R.id.button_connect);
        Drawable[] draws = button.getCompoundDrawables();
        draws[2].setBounds(0,0,(int)(iconWH*0.7),(int)(iconWH*0.7));
        button.setCompoundDrawables(draws[0],draws[1],draws[2],draws[3]);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity act = getActivity();
        //网页收藏栏总布局
        web_navy = act.findViewById(R.id.layout_web_navy);
        //系统导航栏的监听器实例化及界面显示
        onItemClickListener_sys = new RecyclerSysWebAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, ArrayList<IndexItem> list) {
                //点击事件加载对应URI
                IndexItem item = list.get(position);
                loadURL(item.getLink().toString(),pageWebView);
            }
        };
        int sys_column = 7;
        item_list_sys = getItemListFromDatabase("SYS_WEBSITE");
        recycler_sys = act.findViewById(R.id.recycler_sys_website);
        GridLayoutManager glm_sys = new GridLayoutManager(getContext(),sys_column);
        recycler_sys.setLayoutManager(glm_sys);
        //recycler_sys.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));//设置分割线
        recycler_sys.setAdapter(new RecyclerSysWebAdapter(getContext(),item_list_sys,onItemClickListener_sys,glm_sys));

        //用户导航栏的监听器实例化及界面显示
        onItemClickListener_user = new RecyclerIndexAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position,  ArrayList<IndexItem> itemList) {
                //点击事件加载对应URI
                IndexItem item = itemList.get(position);
                loadURL(item.getLink().toString(),pageWebView);
            }
        };
        item_list_user = getItemListFromDatabase("USER_WEBSITE");
        recycler_user = act.findViewById(R.id.recycler_user_website);
        GridLayoutManager glm_user = new GridLayoutManager(getContext(),4);   //宽度为4
        recycler_user.setLayoutManager(glm_user);
        recycler_user.setAdapter( new RecyclerIndexAdapter(getContext(),item_list_user,onItemClickListener_user,glm_user));

        //显示网页布局及输入框的初始化
        pageWebView = act.findViewById(R.id.webview_page);     pageWebView.setVisibility(View.GONE);//一开始不显示加载网页
        etWebsite =  act.findViewById(R.id.editText_website);
        etWebsite.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    //当按下确定键时
                    loadURL(etWebsite.getText().toString(),pageWebView);
                    return true;
                }
                return false;
            }
        });
        connectButton = act.findViewById(R.id.button_connect);          connectButton.setOnClickListener(this);
        webSettings = pageWebView.getSettings();
        //TODO:防范脚本攻击
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);   //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true);  // 缩放至屏幕的大小
        webSettings.setSupportZoom(true);   //缩放支持
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
    }

    public void loadURL(String net,WebView webView){
        if(net.length() == 0 || net.matches("[ \n\r\f\t]*"))
            return;
        //指定webview加载对应URL，并设置可见
        if(!net.matches(".*[:][/]{2}.*"))
            net = "http://" + net;
        webView.loadUrl(net);
        webView.setWebViewClient(myClient);
        changeVisible(SHOW_WEB);
        handler.notifyNavButtonState();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        handler = (MainActivity.ActHandler)args.getSerializable("ActHandler");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private ArrayList<IndexItem> getItemListFromDatabase(String table_name){
        //读取用户资料
        ArrayList<IndexItem> list = new ArrayList<IndexItem>();
        Cursor cursor = BaseActivity.database.query(table_name,new String[]{"*"}
        ,null,null,null,null,null,null);
        if(!cursor.moveToFirst()){
            //尚未保存网址
            list.clear();
            return list;
        }
        try{
            do{
                String title = cursor.getString(cursor.getColumnIndex("TITLE"));
                URI website = new URI( cursor.getString(cursor.getColumnIndex("URI")) );
                int id = cursor.getInt(cursor.getColumnIndex("IMG_ID"));
                IndexItem indexItem = new IndexItem(title,id,website);
                list.add(indexItem);
            }while(cursor.moveToNext());
        }catch (URISyntaxException urie){
            Log.e(".HomeFragment",urie+"\nURI Failed ! ");
        }
        return list;
    }

    public void changeVisible(int changePlan){
        if(changePlan == SHOW_INDEX){
            web_navy.setVisibility(View.VISIBLE);
            pageWebView.stopLoading();
            pageWebView.setVisibility(View.GONE);
        }else if(changePlan == SHOW_WEB){
            web_navy.setVisibility(View.GONE);
            pageWebView.setVisibility(View.VISIBLE);
        }
    }

    public boolean isIndexVisible(){
        if(web_navy.getVisibility()==View.VISIBLE )
            return true;
        return false;
    }

    public void backPage(){
        if(pageWebView.canGoBack()){
            if(isIndexVisible())
                changeVisible(SHOW_WEB);
            else
                pageWebView.goBack();
        }else
            changeVisible(SHOW_INDEX);
    }

    public void forPage(){
        if(pageWebView.canGoForward()){
            if(isIndexVisible())
                changeVisible(SHOW_WEB);
            else
                pageWebView.goForward();
        }
    }

    public boolean canBack(){
        return pageWebView.canGoBack();
    }

    public boolean canFor(){
        return pageWebView.canGoForward();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onClickHome();
    }
}
