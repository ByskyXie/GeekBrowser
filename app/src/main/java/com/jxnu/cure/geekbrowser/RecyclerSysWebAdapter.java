package com.jxnu.cure.geekbrowser;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by asus on 2017/10/31.
 */

public class RecyclerSysWebAdapter extends RecyclerView.Adapter<RecyclerSysWebAdapter.SysWebHolder> {

    private Context context;
    private GridLayoutManager glm;
    private ArrayList<IndexItem> list;
    private OnItemClickListener listener;

    public RecyclerSysWebAdapter(Context context, ArrayList<IndexItem> list, OnItemClickListener listener,GridLayoutManager glm) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.glm = glm;
    }

    static class SysWebHolder extends RecyclerView.ViewHolder{
        private LinearLayout layout;
        private Button button_img;
        private TextView textView;
        private IndexItem indexItem;
        public SysWebHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView;
            textView = itemView.findViewById(R.id.text_sys_web_item);
            button_img = itemView.findViewById(R.id.button_sys_web_item);
        }
    }

    @Override
    public SysWebHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sys_web_item_layout,parent,false);
        return new SysWebHolder(view);
    }

    @Override
    public void onBindViewHolder(final SysWebHolder holder, final int position) {
        IndexItem item = list.get(position);
        holder.indexItem = item;
        holder.textView.setText(item.getText());
        if(item.getImg() != IndexItem.NONE_IMAGE){
            holder.button_img.setBackgroundResource(item.getImg());
        }else
            holder.button_img.setText(item.getText().substring(0,1));
        //改变长度
        ViewGroup.LayoutParams parm = holder.button_img.getLayoutParams();
        parm.height = glm.getWidth()/glm.getSpanCount()
                - holder.button_img.getPaddingLeft() - 2*((ViewGroup.MarginLayoutParams)parm).leftMargin;

        //设置触发事件
        holder.button_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(holder.getAdapterPosition(),list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    interface OnItemClickListener{
        void onClick(int position,ArrayList<IndexItem> list);
    }
}
