package com.maze.chen.maze.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.data.Maze;


/**
 * Created by chen on 2016/3/8.
 */
public class HackShapeObject extends ShapeObject {

    public HackShapeObject() {
    }

    public HackShapeObject(Bitmap mBackBitmap) {
        super(mBackBitmap);
    }

    @Override
    public boolean drawSelfOnCorrect(Canvas canvas, Maze maze, int direct) {
        if(direct==0) {
            mCoordinate.x= mCoordinate.x - 1;
        }
        else if(direct==1) {
            mCoordinate.x = mCoordinate.x + 1;
        }
        else if(direct==2) {
            mCoordinate.y = mCoordinate.y - 1;
        }
        else if(direct==3) {
            mCoordinate.y = mCoordinate.y + 1;
        }

        int top = (int) (mCoordinate.x * maze.getcellWidth() + maze.getWhiteSpace() + 4);
        int left = (int) (mCoordinate.y * maze.getcellWidth() + maze.getWhiteSpace() + 4);
        int right = (int) (left + maze.getcellWidth() - 8);
        int bottom = (int) (top + maze.getcellWidth() - 8);
        int cellWidth = GlobeContext.mScreenWidth/GlobeContext.mGameColumns;
        int offsetY= (GlobeContext.mContentHeight-GlobeContext.mGameRows*cellWidth)/2-1;
        mRegion.set(left, top+offsetY, right, bottom+offsetY);
        canvas.drawBitmap(mBackBitmap,null,mRegion,null);
        return false;
    }
}
