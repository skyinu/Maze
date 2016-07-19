package com.maze.chen.maze.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maze.chen.maze.R;

import java.util.List;

/**
 * Created by chen on 2016/3/10.
 */
public class HistoryAdapter extends BaseAdapter {
    private List<String> mDatas;
    private LayoutInflater mInflater;

    public HistoryAdapter(List<String> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mInflater =LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.hisorylayout,parent,false);
            holder=new Holder();
            holder.rank= (TextView) convertView.findViewById(R.id.id_rank);
            holder.reecord= (TextView) convertView.findViewById(R.id.id_best_recoder);
            convertView.setTag(holder);
        }
        else{
            holder= (Holder) convertView.getTag();
        }
        if(position==0){
            holder.rank.setText("关卡");
            holder.rank.setTextSize(24);
            holder.reecord.setText("最佳纪录");
            holder.reecord.setTextSize(24);
        }
        else{
            holder.rank.setText("第"+position+"关");
            holder.rank.setTextSize(20);
            holder.reecord.setText(mDatas.get(position));
            holder.reecord.setTextSize(20);
        }
        return convertView;
    }
    class Holder{
        public TextView rank;
        public TextView reecord;
    }
}
