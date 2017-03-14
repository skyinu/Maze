package com.maze.chen.maze.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;


import com.maze.chen.maze.data.ControlVariable;
import com.maze.chen.maze.data.Maze;
import com.maze.chen.maze.model.HackShapeObject;
import com.maze.chen.maze.model.VegeShapeObject;

import java.util.Deque;

/**
 * Created by chen on 2015/12/6.
 */
public class DrawSolver {
    private Maze maze;
    private ControlVariable controlVariable;
    private Deque<Integer> pathDeque;
    public static Bitmap treeForeBitmap;
    public static Bitmap hackBackBitmap;
    public static Bitmap busyBackground;
    private static Rect backgroundRect=new Rect();


    private VegeShapeObject vegeShapeObject;
    private HackShapeObject hackShapeObject;

    public DrawSolver(Maze maze,ControlVariable controlVariable,Deque<Integer> pathDeque){
        this.maze=maze;
        this.controlVariable=controlVariable;
        this.pathDeque=pathDeque;

        vegeShapeObject=new VegeShapeObject(treeForeBitmap);
        hackShapeObject=new HackShapeObject(hackBackBitmap);
    }

    /**
     * 绘制追击的物体
     * @param canvas
     */
    public boolean drawHack(Canvas canvas, int direct){
        hackShapeObject.drawSelfOnCorrect(canvas,maze,direct);
        if(hackShapeObject.getCoordinate().equals(vegeShapeObject.getCoordinate()))
            return true;
        return false;
    }


    /**
     * 正常情况的绘制
     * @param surfaceHolder
     * @param direct
     * @param hackDirect
     * @param width
     * @param height
     */
    public void drawGame(SurfaceHolder surfaceHolder,int direct, int hackDirect,int width,int height) {
        if(surfaceHolder==null)
            return;
        Canvas canvas = surfaceHolder.lockCanvas();
        backgroundRect.set(0, 0, width, height);
        if(canvas!=null) {
            canvas.drawBitmap(busyBackground,null,backgroundRect,null);
            maze.drawMaze(canvas);
            if(vegeShapeObject.drawSelfOnCorrect(canvas,maze,direct)){//将有效的路径加入队列中
                if(vegeShapeObject.checkIfReachEnd(maze)){
                    controlVariable.isWin=true;
                }
                if(pathDeque.isEmpty()){
                    pathDeque.add(direct);
                }
                else {
                    int last = pathDeque.peekLast();
                    if ((last == 0 && direct == 1) || (last == 1 && direct == 0) || (last == 2 && direct == 3) || (last == 3 && direct == 2)) {
                        pathDeque.pollLast();
                    } else {
                        pathDeque.addLast(direct);
                    }
                }
            }
            if(controlVariable.isHackShouldDraw){
                boolean res=drawHack(canvas, hackDirect);//每走一步保存一步
                if(res&&!controlVariable.isWin){
                    controlVariable.isGameOver=true;
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * 使追击者后退时绘制画面的类
     * @param surfaceHolder
     * @param hackDirect
     * @param width
     * @param height
     */
    public void drawBackGame(SurfaceHolder surfaceHolder,int hackDirect,int width,int height){
        if(surfaceHolder==null)
            return;
        Canvas canvas = surfaceHolder.lockCanvas();
        backgroundRect.set(0, 0, width, height);
        if(canvas != null) {
            canvas.drawBitmap(busyBackground,null,backgroundRect,null);

            maze.drawMaze(canvas);
            vegeShapeObject.drawSelfOnCorrect(canvas,maze,-1);
            drawHack(canvas,  hackDirect);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }


    public void resetAllDatas(){
        vegeShapeObject.setCoordinate(0,0);
        hackShapeObject.setCoordinate(0,0);
    }

    public HackShapeObject getHackShapeObject() {
        return hackShapeObject;
    }

    public VegeShapeObject getVegeShapeObject() {
        return vegeShapeObject;
    }
}
