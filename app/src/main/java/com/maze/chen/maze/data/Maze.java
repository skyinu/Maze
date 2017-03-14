package com.maze.chen.maze.data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.maze.chen.maze.GlobeContext;
import com.maze.chen.maze.utils.GameUtil;

public class Maze {
	private MazeFactory mMazeFactory;//factory use to generate maze
	private MazeNode cells[][];//maze nodes' data
	private int mRow;//row count
	private int mColumn;//column count
	private Point mStartPoint;//entry-point
	private Point mEndPoint;//end-point
	private Paint wallPaint;//paint to draw maze
	private float whiteSpace;//margin
	private float cellWidth;//every maze node's dimension

	/**
	 * 构造方法，初始化迷宫节点
	 * @param row
	 * @param column
	 */
	public Maze(int row, int column) {
		this.mRow = row;
		this.mColumn = column;
		mMazeFactory = new FUMazeFactory();
		wallPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
		wallPaint.setStrokeWidth(3);
		wallPaint.setColor(Color.parseColor("#FF3A13"));
		wallPaint.setStrokeCap(Paint.Cap.ROUND);
	}

	/**
	 * 创建迷宫,构建迷宫的相关数据
	 */
	public void createMaze(int startX, int startY, int endX, int endY) {
		mStartPoint = new Point(startX, startY);
		mEndPoint = new Point(endX, endY);
		cells = mMazeFactory.getMaze(mStartPoint, mEndPoint, mRow, mColumn);
	}

	/**
	 * 根据所以索引获取一个迷宫的单元格
	 * @param index
	 * @return
	 */
	public MazeNode getCellByIndex(int index) {
		int r = index / mColumn;
		int c = index % mColumn;
		return cells[r][c];
	}

	/**
	 * 绘制迷宫的数据
	 * @param canvas
	 */
	public void drawMaze(Canvas canvas){
		cellWidth = GameUtil.getScreenWidth(GlobeContext.getAppContext())/ mColumn;
		int offsetY= (int) (GlobeContext.mContentHeight- mRow *cellWidth)/2-1;
		whiteSpace = (GameUtil.getScreenWidth(GlobeContext.getAppContext()) - cellWidth * mColumn)/2;
		for(int i = 0; i< mRow; i++){
			for(int j = 0; j< mColumn; j++){
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
		if(direct<0||(curPos.y== mColumn -1&&direct==3)||(curPos.y==0&&direct==2)){
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
		MazeNode node=getCellByIndex(point.x* mColumn +point.y);
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
			return getCellByIndex((point.x-1)* mColumn +point.y);
		}
		else if(index==1&&point.x< mRow -1){
			return getCellByIndex((point.x+1)* mColumn +point.y);
		}
		else if(index==2&&point.y>0){
			return getCellByIndex(point.x* mColumn +point.y-1);
		}
		else if(index==3&&point.y< mColumn -1){
			return getCellByIndex(point.x* mColumn +point.y+1);
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
		return mRow;
	}

	public int getColumn() {
		return mColumn;
	}
}
