package com.maze.chen.maze.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maze.chen.maze.R;


/**
 * Created by chen on 2016/3/8.
 */
public class TopPanelFragment extends Fragment{
    private TextView tv_History;
    private TextView tv_CurTime;
    private TextView tv_pass;

    private View mTopPanel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mTopPanel=inflater.inflate(R.layout.toppanel_layout,container,false);
        tv_History= (TextView) mTopPanel.findViewById(R.id.record);
        tv_CurTime= (TextView) mTopPanel.findViewById(R.id.scroe);
        tv_pass= (TextView) mTopPanel.findViewById(R.id.tv_pass);
        return mTopPanel;
    }
    public void setTv_History_Text(String text){
        tv_History.setText(text);
    }
    public void setTv_CurTime_Text(String text){
        tv_CurTime.setText(text);
    }
    public void setTv_Pass_Text(String text){
        tv_pass.setText(text);
    }

}
