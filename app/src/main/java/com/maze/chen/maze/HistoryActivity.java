package com.maze.chen.maze;

import android.app.ListActivity;
import android.os.Bundle;


import com.maze.chen.maze.controller.HistoryAdapter;
import com.maze.chen.maze.utils.GameUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 2016/3/10.
 */
public class HistoryActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> mDatas=new ArrayList<>();
        mDatas.add("first");
        for(int i=1;;i++){
            Integer record= GameUtil.getHistoryScore(GlobeContext.getAppContext(), i);
            if(record==0)
                break;
            mDatas.add(record+"s");
        }
        HistoryAdapter adapter=new HistoryAdapter(mDatas,this);
        setListAdapter(adapter);
        getListView().setDividerHeight(3);
    }
}
