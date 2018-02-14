package com.maze.chen.maze.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.RectF;

import com.maze.chen.maze.data.Maze;


/**
 * Created by chen on 2016/3/8.
 */
public abstract class ShapeObject {
    /**
     * 物块所在的坐标位置
     */
    protected Point mCoordinate;
    /**
     * 物块大小
     */
    protected RectF mRegion;
    /**
     * 物块的图片
     */
    protected Bitmap mBackBitmap;

    public ShapeObject() {
        mCoordinate=new Point(0,0);
        mRegion=new RectF();
    }

    public ShapeObject(Bitmap mBackBitmap) {
        this();
        this.mBackBitmap = mBackBitmap;
    }

    public abstract boolean  drawSelfOnCorrect(Canvas canvas, Maze maze, int direct);

    public void setCoordinate(int x, int y) {
        mCoordinate.x=x;
        mCoordinate.y=y;
    }

    public void setBackBitmap(Bitmap mBackBitmap) {
        this.mBackBitmap = mBackBitmap;
    }

    public Point getCoordinate() {
        return mCoordinate;
    }
}
