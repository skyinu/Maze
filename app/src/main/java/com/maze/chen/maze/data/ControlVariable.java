package com.maze.chen.maze.data;

/**
 * Created by chen on 2015/12/9.
 * 是MazeView的控制数据，影响MazeView的逻辑
 */
public class ControlVariable {
    public boolean isHackShouldDraw;
    public boolean isGameOver=false;
    public boolean isShouldUpdate=false;
    public boolean isShouldRun=true;
    public boolean isWin;
    public volatile int backNumber;
    public ControlVariable(){
        resetVariable();
    }
    public void resetVariable(){
        isGameOver=false;
        isHackShouldDraw=false;
        isShouldRun=true;
        isWin=false;
        isShouldUpdate=false;
        backNumber=0;
    }
}
