package com.maze.chen.maze.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.provider.Settings;


import com.maze.chen.maze.GlobeContext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Maze {
	private MazeNode cells[][];
	private int row;
	private int column;
	private static Maze maze;
	private Paint wallPaint;
	private Map<Integer, Set<Integer>> relaMap;
	private float whiteSpace;
	private float cellWidth;

	/**
	 * 构造方法，初始化迷宫节点
	 * @param row
	 * @param column
	 */
	private Maze(int row, int column) {
		this.row = row;
		this.column = column;
		relaMap=new HashMap<>();
		cells = new MazeNode[row][column];
		wallPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		wallPaint.setStrokeWidth(3);
		wallPaint.setColor(Color.parseColor("#FF3A13"));
		wallPaint.setStrokeCap(Paint.Cap.ROUND);
		int pos = 0;
		for (int i = 0; i < row; i++) {
			cells[i] = new MazeNode[column];
			for (int j = 0; j < column; j++) {
				relaMap.put(pos, new HashSet<Integer>());
				relaMap.get(pos).add(pos);
				cells[i][j] = new MazeNode();
				//设置起点和结束点的墙壁不存在
				if(i==0&&j==0){
					cells[i][j].setWallDataByIndex(2, false);
				}
				if(i==row-1&&j==column-1){
					cells[i][j].setWallDataByIndex(3, false);
				}
				pos++;
			}
		}
	}

	/**
	 * 初始化迷宫的配置数据：指定行数、列数
	 *
	 * @param row 行数
	 * @param column 列数
	 */
	public static Maze initMaze(int row, int column) {
		maze = new Maze(row, column);
		return maze;
	}

	/**
	 * 创建迷宫,构建迷宫的相关数据
	 */
	public void createMaze() {
		Random rand = new Random(System.currentTimeMillis());
		int hole = row * column;
		while (!isConnected(0, hole-1)) {
			int cellIndex = rand.nextInt(hole);//选择一个单元格
			int wallIndex = rand.nextInt(4);//选择一个墙
			MazeNode cell = getCellByIndex(cellIndex);
			switch (wallIndex) {
				case 0://选中上,选中的格子的行数要大于0
					if (cellIndex / column > 0) {
						int closeIndex = cellIndex - column;
						if (!isConnected(cellIndex, closeIndex)) {
							changeSet(cellIndex, closeIndex);
							getCellByIndex(closeIndex).setWallDataByIndex(1, false);
							cell.setWallDataByIndex(wallIndex, false);
						}
					}
					break;
				case 1://选中下,选中的格子的行数要小于row - 1
					if (cellIndex / column < row - 1) {
						int closeIndex = cellIndex + column;
						if (!isConnected(cellIndex, closeIndex)) {
							changeSet(cellIndex, closeIndex);
							getCellByIndex(closeIndex).setWallDataByIndex(0, false);
							cell.setWallDataByIndex(wallIndex, false);
						}
					}
					break;
				case 2://选中左，列数要大于0
					if (cellIndex % column > 0) {
						int closeIndex = cellIndex - 1;
						if (!isConnected(cellIndex, closeIndex)) {
							changeSet(cellIndex, closeIndex);
							getCellByIndex(closeIndex).setWallDataByIndex(3, false);
							cell.setWallDataByIndex(wallIndex, false);
						}
					}
					break;
				case 3://选中右
					if (cellIndex % column < column - 1) {
						int closeIndex = cellIndex + 1;
						if (!isConnected(cellIndex, closeIndex)) {
							changeSet(cellIndex, closeIndex);
							getCellByIndex(closeIndex).setWallDataByIndex(2, false);
							cell.setWallDataByIndex(wallIndex, false);
						}
					}
					break;
			}
		}
		relaMap.clear();
		relaMap=null;
	}

	/**
	 * 合并集合元素，并删除其一
	 * @param cellIndex
	 * @param closeIndex
	 */
	private void changeSet(int cellIndex, int closeIndex) {
		int m=-1;
		int n=-1;
		Set<Integer> set=relaMap.keySet();
		for(Integer i:set){
			if(relaMap.get(i).contains(cellIndex)){
				m=i;
			}
			else if(relaMap.get(i).contains(closeIndex)){
				n=i;
			}
			if(m!=-1&&n!=-1){
				break;
			}
		}
		relaMap.get(m).addAll(relaMap.get(n));
		relaMap.remove(n);
	}

	/**
	 * 判断两个迷宫单元是连通
	 * @param cellIndex
	 * @param closeCell
	 * @return
	 */
	private boolean isConnected(int cellIndex, int closeCell) {
		Set<Integer> ite=relaMap.keySet();
		for(Integer i:ite){
			Set<Integer> set=relaMap.get(i);
			if(set.contains(cellIndex)&&set.contains(closeCell)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 根据所以索引获取一个迷宫的单元格
	 * @param index
	 * @return
	 */
	public MazeNode getCellByIndex(int index) {
		int r = index / column;
		int c = index % column;
		return cells[r][c];
	}

	/**
	 * 绘制迷宫的数据
	 * @param canvas
	 */
	public void drawMaze(Canvas canvas){
		cellWidth = GlobeContext.mScreenWidth/column;
		int offsetY= (int) (GlobeContext.mContentHeight-row*cellWidth)/2-1;
		whiteSpace = (GlobeContext.mScreenWidth- cellWidth *column)/2;
		for(int i=0;i<row;i++){
			for(int j=0;j<column;j++){
				int startX= Math.round ((cellWidth)*j+ whiteSpace);
				int startY= Math.round  ((cellWidth)*i+ whiteSpace)+offsetY;
				MazeNode node=cells[i][j];
				if(node.getWallDataByIndex(0)) {
					canvas.drawLine(startX,startY,startX+ cellWidth,startY,wallPaint);
				}
				if(node.getWallDataByIndex(1)) {
					canvas.drawLine(startX,startY+ cellWidth,startX+ cellWidth,startY+ cellWidth,wallPaint);
				}
				if(node.getWallDataByIndex(2)) {
					canvas.drawLine(startX,startY,startX,startY+ cellWidth,wallPaint);
				}
				if(node.getWallDataByIndex(3)) {
					canvas.drawLine(startX+ cellWidth,startY,startX+ cellWidth,startY+ cellWidth,wallPaint);
				}
			}
		}
	}

	/**
	 * 检查能否向下一步走
	 * @param curPos
	 * @param direct
	 * @return
	 */
	public boolean checkIfCanGo(Point curPos,int direct){
		if(direct<0||(curPos.y==column-1&&direct==3)||(curPos.y==0&&direct==2)){
			return true;
		}
		return cells[curPos.x][curPos.y].getWallDataByIndex(direct);
	}

	/**
	 * 随机拆除一个格子周围的一堵墙
	 * @param point
	 * @param index
	 */
	public void destroyWallByPoint(Point point, int index){
		MazeNode node=getCellByIndex(point.x*column+point.y);
		MazeNode close;
		if((close=checkIsCandestroy(point,index))!=null&&node.getWallDataByIndex(index)){
			node.setWallDataByIndex(index,false);
			if(index==0){
				close.setWallDataByIndex(1,false);
			}
			else if(index==1){
				close.setWallDataByIndex(0,false);
			}
			else if(index==2){
				close.setWallDataByIndex(3,false);
			}
			else{
				close.setWallDataByIndex(2,false);
			}
		}
		else{
			for(int i=0;i<4;i++){
				if(checkIsCandestroy(point,i)!=null&&node.getWallDataByIndex(i)){
					destroyWallByPoint(point,i);
					break;
				}
			}
		}
	}

	/**
	 * 检查当前点是否可销毁
	 * @param point
	 * @param index
	 * @return
	 */
	private MazeNode checkIsCandestroy(Point point,int index){
		if(index==0&&point.x>0){
			return getCellByIndex((point.x-1)*column+point.y);
		}
		else if(index==1&&point.x<row-1){
			return getCellByIndex((point.x+1)*column+point.y);
		}
		else if(index==2&&point.y>0){
			return getCellByIndex(point.x*column+point.y-1);
		}
		else if(index==3&&point.y<column-1){
			return getCellByIndex(point.x*column+point.y+1);
		}
		return null;
	}
	public float getcellWidth() {
		return cellWidth;
	}

	public float getWhiteSpace() {
		return  whiteSpace;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
}
