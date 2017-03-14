package com.maze.chen.maze;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;


import com.maze.chen.maze.data.GameInfoBean;
import com.maze.chen.maze.utils.GameUtil;
import com.maze.chen.maze.view.GameContentView;
import com.maze.chen.maze.view.MazeView;
import com.maze.chen.maze.view.TopPanelFragment;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by chen on 2015/12/6.
 */
public class MainActivity extends Activity  {
    /**
     * 顶部布局
     */
    private TopPanelFragment topPanelFragment;
    /**
     * 迷宫视图
     */
    private MazeView mazeView;
    /**
     * 消息队列
     */
    private BlockingQueue<Integer> messageQueen;
    /**
     * 关卡和分数记录
     */
    private static int curScore;
    /**
     * 计数恢复相关的控制逻辑和计数Handler
     */
    private boolean isNeedSendMessage=false;
    private Handler calScoreHandler;
    /**
     * 产生后退等效果相关的数据
     */
    private static int destroyWall = 0;
    private static int subVolicity = 0;
    private static int goBack = 0;
    private static int backDestroyWall = 0;
    private static int backSubVolicity = 0;
    private static int backGoBack = 0;
    /**
     * 减速时间,单位为秒
     */
    private static int SUBTIME = 0;

    /**
     * 用于产生随机奖励
     */
    private Random random;

    private GameContentView mGameContentView;

    private GameInfoBean mGameInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        mGameInfo = new GameInfoBean();
        mGameContentView = (GameContentView) findViewById(R.id.game_content_view);
        mGameContentView.setMainActivity(this);
        topPanelFragment = (TopPanelFragment) getFragmentManager().findFragmentById(R.id.id_topfragment);
        mazeView = (MazeView) findViewById(R.id.maze_view);
        random = new Random(System.currentTimeMillis());
    }

    @Override
    protected void onStart() {
        super.onStart();
        reStartGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reStartGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseGame();
    }

    @Override
    public void onBackPressed() {
        pauseGame();
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("是否确认退出?");
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GlobeContext.pass=1;
                finish();
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                reStartGame();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    /**
     * 用于第一次初始化界面所用
     */
    private boolean hasContentHeight = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasContentHeight) {
            hasContentHeight = true;
            GlobeContext.mContentHeight = mazeView.getHeight();
            InitData();

        }
    }


    public void pauseGame(){
        isNeedSendMessage = true;
        mazeView.removeMessage();
        for(int i=0;i<10;i++){
            calScoreHandler.removeMessages(SUBTIME);
        }
    }


    /**
     * 发程序中用于计时的Handler消息、迷宫更新消息
     */
    public void reStartGame() {
        if (isNeedSendMessage) {
            isNeedSendMessage = false;
            calScoreHandler.sendEmptyMessageDelayed(0, 1000);
            mazeView.sendMessage(curScore);
            if (SUBTIME > 0) {
                Message msg = Message.obtain(calScoreHandler, SUBTIME);
                msg.sendToTarget();
            }
        }
    }


    /**
     * 程序相关运行数据的初始化
     */
    private void InitData() {
        calScoreHandler = new CalcuteScoreHandler(this);
        newGame(GlobeContext.pass);

        messageQueen = new LinkedBlockingQueue<>(200);
        mazeView.setMessageQueen(messageQueen);
        mazeView.setMainActivity(this);
        mGameContentView.setMessageQueen(messageQueen);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(int i=0;i<10;i++){
            calScoreHandler.removeMessages(SUBTIME);
        }
    }

    /**
     * 每开始一关调用该方法初始化数据
     *  @param pass
     *
     */
    public void newGame(int pass) {
        isOverOnce=true;
        getmGameContentView().setEnabled(false);
        //设置初始化的界面显示参数
        mGameContentView.setButtonText(getResources().getText(R.string.destroy_wall)+"x"+destroyWall,2);
        mGameContentView.setButtonText(getResources().getText(R.string.go_back)+"x"+goBack,1);
        mGameContentView.setButtonText(getResources().getText(R.string.sub_velocity)+"x"+subVolicity,0);
        isNeedSendMessage = false;
        curScore = 0;
        topPanelFragment.setTv_History_Text(GameUtil.getHistoryScore(GlobeContext.getAppContext(), pass) + "s");
        topPanelFragment.setTv_Pass_Text("第" + pass + "关");
        topPanelFragment.setTv_CurTime_Text("0s");
        GlobeContext.nextPass();
        mazeView.initGame(GlobeContext.mGameRows,GlobeContext.mGameColumns);
        //其他参数
        SUBTIME = 0;
        backDestroyWall = destroyWall;
        backGoBack = goBack;
        backSubVolicity = subVolicity;
        mGameContentView.initSurTime();
        mazeView.setIsPause(false);
        calScoreHandler.sendEmptyMessageDelayed(0, 1000);
        getmGameContentView().setEnabled(true);
    }

    public void onEffectClick(int option) {
        if (option == 0) {
            if (subVolicity > 0) {
                subVolicity--;
                mGameContentView.setButtonText(getResources().getText(R.string.sub_velocity)+"x"+subVolicity,0);
                SUBTIME += 4;
                if (SUBTIME == 4) {
                    Message msg = Message.obtain(calScoreHandler, SUBTIME);
                    msg.sendToTarget();
                }
                if (MazeView.TIME_GAP == 700) {
                    MazeView.TIME_GAP += 1300;
                }
            }
        } else if (option == 1) {
            if (destroyWall > 0) {
                destroyWall--;
                mazeView.setIsPause(true);
                mazeView.destroyWall(random.nextInt(4));
                mazeView.setIsPause(false);
                mGameContentView.setButtonText(getResources().getText(R.string.destroy_wall)+"x"+destroyWall,2);
            }
        } else {
            if (goBack > 0) {
                goBack--;
                mGameContentView.setButtonText(getResources().getText(R.string.go_back)+"x"+goBack,1);
                mazeView.getControlVariable().backNumber = 4;
            }
        }


    }

    /**
     * 设置当前得分
     *
     * @param add
     */

    public void setCurrentScore(int add) {
        curScore += add;
        topPanelFragment.setTv_CurTime_Text(curScore + "s");
    }

    /**
     * 处理时间计分的Handler
     */
    private static class CalcuteScoreHandler extends Handler {
        private WeakReference<MainActivity> mainActivity;

        public CalcuteScoreHandler(MainActivity main) {
            mainActivity = new WeakReference<>(main);
        }

        /**
         * what为0时用于时间的计数
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mainActivity.get().setCurrentScore(1);
                sendEmptyMessageDelayed(0, 1000);
                mainActivity.get().getmGameContentView().checkIfShouldAddSurprise();

            } else if (msg.what >= 1) {
                SUBTIME--;
                if (msg.what != 1) {
                    Message message = Message.obtain(this, msg.what - 1);
                    sendMessageDelayed(message, 1000);
                } else {
                    MazeView.TIME_GAP -= 1300;
                }
            }
        }
    }

    private boolean isOverOnce=true;

    /**
     * 游戏失败或者胜利后的调用
     *
     * @param isWin
     */
    public void gameOver(boolean isWin) {
        if (isOverOnce) {
            messageQueen.clear();
            calScoreHandler.removeMessages(0);
            mazeView.setIsPause(true);
            mazeView.removeMessage();
            mazeView.setVisibility(View.INVISIBLE);
            mazeView.setVisibility(View.VISIBLE);
            mGameContentView.stopCheckSurprise();
            Intent intent=new Intent(MainActivity.this,GameEndActivity.class);
            intent.putExtra("game_result",isWin);
            startActivityForResult(intent,1);
            if (isWin) {
                GameUtil.updateHistory(GlobeContext.getAppContext(), GlobeContext.pass, curScore);
            } else {
                destroyWall = backDestroyWall;
                goBack = backGoBack;
                subVolicity = backSubVolicity;
            }
        }
        else{
            pauseGame();
        }
    }

    /**
     * 答题Activity返回时用来处理是否增加效果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {
            if (resultCode == RESULT_OK) {
                mGameContentView.startGetAnimator(true);
                int index = random.nextInt(3);
                if (index == 0) {
                    subVolicity++;
                    mGameContentView.setButtonText(getResources().getText(R.string.sub_velocity) + "x" + subVolicity, 0);
                } else if (index == 1) {
                    goBack++;
                    mGameContentView.setButtonText(getResources().getText(R.string.go_back) + "x" + goBack, 1);
                } else if (index == 2) {
                    destroyWall++;
                    mGameContentView.setButtonText(getResources().getText(R.string.destroy_wall) + "x" + destroyWall, 2);
                }
            }
            else{
                mGameContentView.startGetAnimator(false);
            }
        }
        else if(requestCode==1){
            if(resultCode==RESULT_OK&&data!=null){
                GlobeContext.pass=1;
                isOverOnce=false;
                mazeView.setIsPause(false);
                mazeView.getControlVariable().isShouldRun=false;
                finish();
            }
            else if (resultCode == RESULT_OK) {
                GlobeContext.pass++;
                newGame(GlobeContext.pass);
            }
            else if(resultCode==RESULT_CANCELED){
                newGame(GlobeContext.pass);
            }

        }

    }

    public GameContentView getmGameContentView() {
        return mGameContentView;
    }
}
