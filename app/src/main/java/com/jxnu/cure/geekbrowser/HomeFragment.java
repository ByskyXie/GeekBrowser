package com.jxnu.cure.geekbrowser;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment
    implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragWebClient myClient;
    private WebSettings webSettings;
    private EditText et_website;
    private WebView webView_home;
    private ImageButton imgButton;

    private RecyclerIndexAdapter.OnItemClickListener listener;
    private RecyclerView recycler;
    private ArrayList<IndexItem> item_list;
    private String temp;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    class FragWebClient extends WebViewClient{
        ///用于防止调用其它浏览器打开Url
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(webView_home!=null)
                webView_home.loadUrl(temp);
            return true;
        }
    }
    public HomeFragment() {
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
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_commit:
                webView_home.setVisibility(View.VISIBLE);
                temp = et_website.getText().toString();
                loadURL(temp,webView_home);
                break;
            case R.id.editText_website:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myClient = new FragWebClient();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity act = getActivity();
        ///
        listener = new RecyclerIndexAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position,  ArrayList<IndexItem> itemList) {
                //点击事件加载对应URI
                IndexItem item = itemList.get(position);
                loadURL(item.getLink().toString(),webView_home);
            }
        };
        item_list = getItemList();
        recycler = act.findViewById(R.id.recycle_index);         recycler.setAdapter( new RecyclerIndexAdapter(getContext(),item_list,listener));
        GridLayoutManager glm = new GridLayoutManager(getContext(),4);   //宽度为4
        recycler.setLayoutManager(glm);
        ///
        webView_home = act.findViewById(R.id.webView_home);     webView_home.setVisibility(View.GONE);//一开始不显示加载网页
        et_website =  act.findViewById(R.id.editText_website);  et_website.setOnClickListener(this);
        imgButton = act.findViewById(R.id.img_commit);          imgButton.setOnClickListener(this);
        webSettings = webView_home.getSettings();               webSettings.setJavaScriptEnabled(true);
    }

    public void loadURL(String net,WebView webView){
        //指定webview加载对应URL，并设置可见
        if(net.length()<7||!net.substring(0,7).equals("http://"))
            net = "http://" + net;
        if(net.equals("http://"))
            return;
        //处理后网址不为空，即只有"http://"
        webView.loadUrl(net);
        webView.setWebViewClient(myClient);
        webView.setVisibility(View.VISIBLE);
        //
        recycler.setVisibility(View.GONE);
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    private ArrayList<IndexItem> getItemList(){
        //TODO 读取资料
        Cursor cursor = BaseActivity.database.query("USER_WEBSITE",new String[]{"*"},null,null,null,null,null,null);
        ArrayList<IndexItem> list = new ArrayList<IndexItem>();
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
        void onClickBackward();
        void onClickForward();
        void onClickHome();
    }
    public void backPage(){
        if(webView_home.canGoBack())
            webView_home.goBack();
        else
            goHome();
    }
    public void forPage(){
        if(webView_home.canGoForward())
            webView_home.goForward();
    }
    public void goHome(){
        //TODO:后期直接使用webView_home的方法可能要找到并改为传参
        webView_home.stopLoading();
        webView_home.setVisibility(View.GONE);
        recycler.setVisibility(View.VISIBLE);
    }
    public boolean canBack(){
        return webView_home.canGoBack();
    }
}
