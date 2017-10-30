package com.jxnu.cure.geekbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by asus on 2017/10/27.
 */

public class RecyclerIndexAdapter extends RecyclerView.Adapter<RecyclerIndexAdapter.IndexHolder> {

    private Context context;
    private ArrayList<IndexItem> item_list;
    private OnItemClickListener onItemClickListener;

    RecyclerIndexAdapter (Context context, ArrayList<IndexItem> itemList,OnItemClickListener listener){
        item_list = itemList;
        this.context = context;
        onItemClickListener = listener;
    }

    static class IndexHolder extends RecyclerView.ViewHolder{
        private Button button;
        private TextView textView;
        private IndexItem indexItem;
        public IndexHolder(View itemView) {
            //Holder处理与布局关联的item实例(inflate),这里的itemView正如其名(内容、文字为空，只是有个实例)
            super(itemView);
            button = itemView.findViewById(R.id.button_index_item);
            textView = itemView.findViewById(R.id.text_index_item);
        }
    }

    @Override
    public IndexHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_item_layout,parent,false);
        //这个false代表不加入根，去除造成闪退
        return new IndexHolder(view);
    }

    @Override
    public void onBindViewHolder(IndexHolder holder, final int position) {
        IndexItem iitem = item_list.get(position);
        holder.indexItem = iitem;
        if(iitem == null)
            return;
        holder.indexItem = iitem;
        holder.textView.setText(iitem.getText().toCharArray(),0,iitem.getText().length());
        //设置背景为首个文字
        if(iitem.getImg() == IndexItem.NONE_IMAGE)
            holder.button.setText(iitem.getText().substring(0,1).toCharArray()
                    ,0, 1 );
        else
            holder.button.setBackgroundResource(iitem.getImg()); //设置图片

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClick(position,item_list);
            }
        });
    }


    @Override
    public int getItemCount() {
        return item_list.size();
    }

    public interface OnItemClickListener{
        public void onClick (int position, ArrayList<IndexItem> itemList);
    }


}
