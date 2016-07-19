package com.maze.chen.maze.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.data.Maze;

/**
 * Created by chen on 2016/3/8.
 */
public class VegeShapeObject extends ShapeObject{

    public VegeShapeObject() {
    }

    public VegeShapeObject(Bitmap mBackBitmap) {
        super(mBackBitmap);
    }

    @Override
    public boolean drawSelfOnCorrect(Canvas canvas, Maze maze, int direct) {
        boolean flag=true;
        int x=mCoordinate.x;
        int y=mCoordinate.y;
        if(!maze.checkIfCanGo(mCoordinate,direct)) {
            switch (direct){
                case 0:
                    x=mCoordinate.x-1;
                    break;
                case 1:
                    x=mCoordinate.x+1;
                    break;
                case 2:
                    y=mCoordinate.y-1;
                    break;
                case 3:
                    y=mCoordinate.y+1;
                    break;
            }
        }
        else{
            flag=false;
        }
        int top = (int) (x * maze.getcellWidth() + maze.getWhiteSpace() + 4);
        int left = (int) (y * maze.getcellWidth() + maze.getWhiteSpace() + 4);
        int right = (int) (left + maze.getcellWidth() - 8);
        int bottom = (int) (top + maze.getcellWidth() - 8);
        mCoordinate.x=x;
        mCoordinate.y=y;
        int cellWidth = GlobeContext.mScreenWidth/GlobeContext.mGameColumns;
        int offsetY= (GlobeContext.mContentHeight-GlobeContext.mGameRows*cellWidth)/2-1;
        mRegion.set(left, top+offsetY, right, bottom+offsetY);
        canvas.drawBitmap(mBackBitmap,null,mRegion,null);
        return flag;
    }

    public boolean checkIfReachEnd(Maze maze){
        if(mCoordinate.x==maze.getRow()-1&&mCoordinate.y==maze.getColumn()-1)
            return true;
        return false;
    }
}
