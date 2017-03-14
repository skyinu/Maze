package com.maze.chen.maze.data;

import android.graphics.Point;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by chen on 2017/3/11.
 */

public class FUMazeFactory extends MazeFactory {
    private HashMap<Integer,Set<Integer>> mNodeRelaMap;
    private MazeNode [][]mMazeNodes;
    private Point mStartPoint;
    private Point mEndPoint;
    private int mRowCount;
    private int mColumnCount;
    @Override
    public MazeNode[][] getMaze(Point startPoint, Point endPoint, int row, int column) {
        initConfig(startPoint, endPoint, row, column);
        setFixedPoint();
        createMaze();
        return mMazeNodes;
    }

    private void initConfig(Point startPoint, Point endPoint, int row, int column){
        this.mStartPoint = startPoint;
        this.mEndPoint = endPoint;
        this.mRowCount = row;
        this.mColumnCount = column;
        mNodeRelaMap = new HashMap<>(row * column);
        mMazeNodes = new MazeNode[row][column];
    }

    /**
     * dig start node and end node's wall
     */
    private void setFixedPoint(){
        int pos = 0;
        for (int i = 0; i < mRowCount; i++) {
            mMazeNodes[i] = new MazeNode[mColumnCount];
            for (int j = 0; j < mColumnCount; j++) {
                mNodeRelaMap.put(pos, new HashSet<Integer>());
                mNodeRelaMap.get(pos).add(pos);
                mMazeNodes[i][j] = new MazeNode();
                //设置起点和结束点的墙壁不存在
                if(i == mStartPoint.x && j == mStartPoint.y){
                    if(j == 0 ) {
                        mMazeNodes[i][j].setWallDataByIndex(WALL_DIRECT_LEFT, false);
                    }
                    else if(i == 0){
                        mMazeNodes[i][j].setWallDataByIndex(WALL_DIRECT_TOP, false);
                    }
                }
                if(i == mEndPoint.x &&j == mEndPoint.y){
                    if(j == mRowCount - 1) {
                        mMazeNodes[i][j].setWallDataByIndex(WALL_DIRECT_RIGHT, false);
                    }
                    else if(i == mColumnCount - 1){
                        mMazeNodes[i][j].setWallDataByIndex(WALL_DIRECT_BOTTOM, false);
                    }
                }
                pos++;
            }
        }
    }

    public void createMaze() {
        Random rand = new Random(System.currentTimeMillis());
        int hole = mRowCount * mColumnCount;
        int startNodeIndex = mStartPoint.x * mColumnCount + mStartPoint.y;
        int endNodeIndex = mEndPoint.x * mColumnCount + mEndPoint.y;
        while (!isConnected(startNodeIndex, endNodeIndex)) {
            int cellIndex = rand.nextInt(hole);//选择一个单元格
            int wallIndex = rand.nextInt(4);//选择一个墙
            MazeNode cell = getCellByIndex(cellIndex);
            switch (wallIndex) {
                case 0://选中上,选中的格子的行数要大于0
                    if (cellIndex / mColumnCount > 0) {
                        int closeIndex = cellIndex - mColumnCount;
                        if (!isConnected(cellIndex, closeIndex)) {
                            mergeSet(cellIndex, closeIndex);
                            getCellByIndex(closeIndex).setWallDataByIndex(1, false);
                            cell.setWallDataByIndex(wallIndex, false);
                        }
                    }
                    break;
                case 1://选中下,选中的格子的行数要小于row - 1
                    if (cellIndex / mColumnCount < mRowCount - 1) {
                        int closeIndex = cellIndex + mColumnCount;
                        if (!isConnected(cellIndex, closeIndex)) {
                            mergeSet(cellIndex, closeIndex);
                            getCellByIndex(closeIndex).setWallDataByIndex(0, false);
                            cell.setWallDataByIndex(wallIndex, false);
                        }
                    }
                    break;
                case 2://选中左，列数要大于0
                    if (cellIndex % mColumnCount > 0) {
                        int closeIndex = cellIndex - 1;
                        if (!isConnected(cellIndex, closeIndex)) {
                            mergeSet(cellIndex, closeIndex);
                            getCellByIndex(closeIndex).setWallDataByIndex(3, false);
                            cell.setWallDataByIndex(wallIndex, false);
                        }
                    }
                    break;
                case 3://选中右
                    if (cellIndex % mColumnCount < mColumnCount - 1) {
                        int closeIndex = cellIndex + 1;
                        if (!isConnected(cellIndex, closeIndex)) {
                            mergeSet(cellIndex, closeIndex);
                            getCellByIndex(closeIndex).setWallDataByIndex(2, false);
                            cell.setWallDataByIndex(wallIndex, false);
                        }
                    }
                    break;
            }
        }
        mNodeRelaMap.clear();
        mNodeRelaMap = null;
    }

    /**
     * whether maze node's  is connected
     * @param startNodeIndex
     * @param mEndNodeIndex
     * @return
     */
    private boolean isConnected(int startNodeIndex, int mEndNodeIndex) {
        Set<Integer> ite= mNodeRelaMap.keySet();
        for(Integer i:ite){
            Set<Integer> set= mNodeRelaMap.get(i);
            if(set.contains(startNodeIndex)&&set.contains(mEndNodeIndex)){
                return true;
            }
        }
        return false;
    }

    private void mergeSet(int cellIndex, int closeIndex) {
        int m=-1;
        int n=-1;
        Set<Integer> set = mNodeRelaMap.keySet();
        for(Integer i:set){
            if(mNodeRelaMap.get(i).contains(cellIndex)){
                m=i;
            }
            else if(mNodeRelaMap.get(i).contains(closeIndex)){
                n=i;
            }
            if(m!=-1&&n!=-1){
                break;
            }
        }
        mNodeRelaMap.get(m).addAll(mNodeRelaMap.get(n));
        mNodeRelaMap.remove(n);
    }

    /**
     * get maze'node by index
     * @param index index refers to the maze node'serial number, from left to right, from top to bottom
     * @return
     */
    public MazeNode getCellByIndex(int index) {
        int r = index / mColumnCount;
        int c = index % mColumnCount;
        return mMazeNodes[r][c];
    }

}
