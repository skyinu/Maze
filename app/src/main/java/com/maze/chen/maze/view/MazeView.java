package com.maze.chen.maze.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


import com.maze.chen.maze.MainActivity;
import com.maze.chen.maze.R;
import com.maze.chen.maze.data.ControlVariable;
import com.maze.chen.maze.data.Maze;

import java.lang.ref.WeakReference;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by chen on 2015/12/6.
 */
public class MazeView extends SurfaceView implements SurfaceHolder.Callback {


    /**
     * 迷宫的数据源
     */
    private Maze maze;
    /**
     *用于同步数据与绘图的信号量
     */
    private Semaphore semaphore;
    /**
     * surfaceView的管理者
     */
    private SurfaceHolder surfaceHolder;
    /**
     * 绘图线程
     */
    private Thread drawThread;

    /**
     * 用与绘制物体的类
     */
    private DrawSolver drawSolver;
    /**
     * 这两个坐标用于表示物体的位置
     */


    /**
     * 方向消息队列
     */
    private BlockingQueue<Integer> messageQueen;
    /**
     * 保存物体的行走路径极其同步信号量
     */
    private Deque<Integer> pathDeque;
    private Stack<Integer> pathBackStack;
    /**
     * 以下用于控制绘图线程的绘图更新
     */
    private boolean isPause=true;

    private ControlVariable controlVariable;
    /**
     * 主活动的引用
     */
    private MainActivity mainActivity;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.maze_view);
        DrawSolver.treeForeBitmap=((BitmapDrawable)ta.getDrawable(R.styleable.maze_view_fore_bitmap)).getBitmap();
        DrawSolver.hackBackBitmap=((BitmapDrawable)ta.getDrawable(R.styleable.maze_view_back_bitmap)).getBitmap();
        DrawSolver.busyBackground=((BitmapDrawable)ta.getDrawable(R.styleable.maze_view_busy_background)).getBitmap();
        ta.recycle();
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        semaphore=new Semaphore(1);

        pathDeque=new LinkedList<Integer>();
        pathBackStack=new Stack<>();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setKeepScreenOn(true);
        controlVariable=new ControlVariable();
    }

    /**
     * 用于初始化游戏，在这里获取迷宫数据,初始化物体的坐标、游戏开始时间的获取
     * @param row    迷宫的行数
     * @param column 迷宫的列数
     */
    public void initGame(final int row, final int column) {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                pathDeque.clear();
                pathBackStack.clear();
                controlVariable.resetVariable();
                hackHandler.removeMessages(1);
                maze = Maze.initMaze(row, column);
                maze.createMaze();
                drawSolver=new DrawSolver(maze,controlVariable,pathDeque);
                drawSolver.resetAllDatas();
                semaphore.release();
            }
        });
        th.start();
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isPause){
                }
                hackHandler.sendEmptyMessageDelayed(0, 1000);
                controlVariable.isShouldRun=true;
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                semaphore.release();
                drawSolver.drawGame(surfaceHolder, -1, -1, getWidth(), getHeight());
                while (controlVariable.isShouldRun) {
                    if (!controlVariable.isGameOver&&!controlVariable.isWin) {
                        if(controlVariable.backNumber==0) {
                            updateUI();
                        }
                        else{
                            backHack();
                        }
                    }
                    else {
                        MazeView.this.post(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.gameOver(controlVariable.isWin);
                            }
                        });

                        break;
                    }
                }

            }
        });
        drawThread.start();
    }

    /**
     * 将HackPointh后退两步
     */
    private void backHack() {
        int backDirect=getBackPath();
        drawSolver.drawBackGame(surfaceHolder, backDirect, getWidth(),getHeight());
        controlVariable.backNumber--;
    }

    /**
     * 获取一个后退方向
     * @return
     */
    public int getBackPath(){
        if(pathBackStack.isEmpty()){
            return -1;
        }
        int direct=pathBackStack.pop();
        pathDeque.addFirst(direct);
        if(direct==0){
            return 1;
        }
        else if(direct==1){
            return 0;
        }
        else if(direct==2){
            return 3;
        }
        else{
            return 2;
        }
    }
    /**
     * 更新UI参数
     */
    private void updateUI() {
        int hackDirect=-1;
        int direct=checkPathDeque();
        if(controlVariable.isShouldUpdate) {
            hackDirect = checkHackShouldDraw();
            controlVariable.isShouldUpdate=false;
        }
        drawSolver.drawGame(surfaceHolder, direct, hackDirect, getWidth(),getHeight());
    }

    /**
     * 检查追击者是否需要显示或者跟新
     */
    private int checkHackShouldDraw() {
        boolean flag=false;
        Integer direct=-1;
        if(controlVariable.isHackShouldDraw) {
            while(pathDeque.size()>0){
                direct= pathDeque.pollFirst();
                if(!maze.checkIfCanGo(drawSolver.getHackShapeObject().getmCoordinate(),direct)){
                    flag=true;
                    pathBackStack.push(direct);
                    break;
                }
            }
            if (!flag) {
                controlVariable.isGameOver=true;
            }
        }
        return direct;
    }

    /**
     * 检查移动方向，每响应一条命令则增加一条路径，同时要删除无效的重复命令
     */
    private int checkPathDeque() {
        int direct=-1;
        if(messageQueen.size()>0){
            direct=messageQueen.poll();
        }
        return direct;
    }
    private HackHandler hackHandler=new HackHandler(MazeView.this);
    public static int TIME_GAP=700;
    /**
     * 用于处理追击物体的位置更新
     */
    private static class HackHandler extends Handler{
        private WeakReference<MazeView> mazeView;
        public HackHandler(MazeView mazeview){
            mazeView=new WeakReference<>(mazeview);
        }
        @Override
        public void handleMessage(Message msg) {
            if(msg.what<=5&&!mazeView.get().getControlVariable().isHackShouldDraw) {
                if(msg.what==5) {
                    mazeView.get().getControlVariable().isHackShouldDraw = true;
                    sendEmptyMessageDelayed(6,TIME_GAP);
                }
                else {
                    sendEmptyMessageDelayed(msg.what + 1, 1000);
                }
            }
            else if(msg.what==6){
                mazeView.get().getControlVariable().isShouldUpdate=true;
                sendEmptyMessageDelayed(6, TIME_GAP);
            }
        }
    }


    @Override
    public void surfaceChanged(final SurfaceHolder holder, int format, int width, int height) {
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        controlVariable.isShouldRun=false;//结束绘图线程
    }

    public void setMessageQueen(BlockingQueue<Integer> messageQueen) {
        this.messageQueen = messageQueen;
    }

    public ControlVariable getControlVariable() {
        return controlVariable;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    /**
     * 移除等待显示的消息
     */
    public void removeMessage() {
        for(int i=0;i<7;i++){
            hackHandler.removeMessages(i);
        }
    }

    /**
     * 发送更新消息
     * @param what
     */
    public void sendMessage(int what){
        if(what<6) {
            hackHandler.sendEmptyMessageDelayed(what,1000);
        }
        else{
            hackHandler.sendEmptyMessageDelayed(6,0);
        }
    }

    /**
     * 去掉当前点的一个墙
     * @param index
     */
    public void destroyWall(int index){
        maze.destroyWallByPoint(drawSolver.getVegeShapeObject().getmCoordinate(),index);
    }
}
