package com.maze.chen.maze.data;

import android.graphics.Point;

/**
 * Created by chen on 2017/3/11.
 */

public abstract class MazeFactory {
    //these const represent the maze node's wall direct
    public static final int WALL_DIRECT_TOP = 0x0;
    public static final int WALL_DIRECT_BOTTOM = 0x1;
    public static final int WALL_DIRECT_LEFT = 0x2;
    public static final int WALL_DIRECT_RIGHT = 0x3;
    /**
     * retrieve the maze data
     * @param startPoint the entry-point of maze, the entry-point must be at the margin of maze
     * @param endPoint the out-point of maze, the out-point must be at the margin of maze
     * @param row the row count of maze
     * @param column the column count of maze
     * @return
     */
    public abstract MazeNode[][]  getMaze(Point startPoint, Point endPoint, int row, int column);
}
