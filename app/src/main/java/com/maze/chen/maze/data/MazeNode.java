package com.maze.chen.maze.data;

import java.util.Arrays;

/**
 * 迷宫单元格的数据结构
 */
public class MazeNode {
	private boolean wall[];//0:上、1:下、2:左、3:右
	public MazeNode(){
		wall=new boolean[4];
		Arrays.fill(wall, true);
	}

	/**
	 * 根据索引获取每个格子的墙壁是否存在的数据
	 * @param index
	 * @return
     */
	public boolean getWallDataByIndex(int index){
		return wall[index];
	}

	/**
	 * 根据索引设置每个格子的墙壁的存在性
	 * @param index
	 * @param flag
     */
	public void setWallDataByIndex(int index,boolean flag){
		wall[index]=flag;
	}

	@Override
	public String toString() {
		return "MazeNode [ wall=" + Arrays.toString(wall) + "]";
	}
}