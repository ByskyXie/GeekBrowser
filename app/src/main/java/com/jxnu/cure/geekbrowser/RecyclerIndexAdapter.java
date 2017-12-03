package com.jxnu.cure.geekbrowser;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus on 2017/10/27.
 */

public class RecyclerIndexAdapter extends RecyclerView.Adapter<RecyclerIndexAdapter.IndexHolder> {

    private Context context;
    private GridLayoutManager glm;
    private ArrayList<IndexItem> item_list;
    private OnItemClickListener onItemClickListener;

    RecyclerIndexAdapter (Context context, ArrayList<IndexItem> itemList, OnItemClickListener listener, GridLayoutManager glm){
        this.glm = glm;
        item_list = itemList;
        this.context = context;
        onItemClickListener = listener;
    }

    static class IndexHolder extends RecyclerView.ViewHolder{
        private Button button;
        private TextView textView;
        private LinearLayout layout;
        private IndexItem indexItem;
        public IndexHolder(View itemView) {
            //Holder处理与布局关联的item实例(inflate),这里的itemView正如其名(内容、文字为空，只是有个实例)
            super(itemView);
            layout = (LinearLayout) itemView;
            button = itemView.findViewById(R.id.button_index_item);
            textView = itemView.findViewById(R.id.text_index_item);
        }
    }

    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.index_item_layout,parent,false);//这个false代表不加入根，去除造成闪退
        return new IndexHolder(view);
    }

    @Override
    public void onBindViewHolder(final IndexHolder holder, final int position) {
        IndexItem item = item_list.get(position);
        holder.indexItem = item;
        holder.indexItem = item;
        holder.textView.setText(item.getText());

        if(item.getImg() == IndexItem.NONE_IMAGE)//设置背景为首个文字
            holder.button.setText(item.getText().substring(0,1));
        else //设置图片
            holder.button.setBackgroundResource(item.getImg());
        //设置长度
        ViewGroup.LayoutParams parm = holder.button.getLayoutParams();
        parm.height = glm.getWidth()/glm.getSpanCount() -holder.button.getPaddingLeft()
                - 2*((ViewGroup.MarginLayoutParams)parm).leftMargin;

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(holder.getAdapterPosition(),item_list);
            }
        });
    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener{
        void onClick (int position, ArrayList<IndexItem> itemList);
    }


}
