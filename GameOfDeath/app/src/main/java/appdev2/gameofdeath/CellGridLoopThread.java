package appdev2.gameofdeath;

/**
 * Created by Kevin on 1/25/2018.
 */


import android.graphics.Canvas;

/**
 * Created by Kevin on 12/14/2017.
 */

public class CellGridLoopThread extends Thread {
    private CellGridSurface view;
    private boolean running;
    //used to keep track of time since last update, only need to update every few ms.
    //may need to increase value
    long mLastTime = -1;
    long mUpdateTime = 20;


    public CellGridLoopThread(CellGridSurface view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }


    @Override
    public void run() {
        while (running) {
            if(mLastTime < 0)
            {
                mLastTime = System.currentTimeMillis();
            }
            else
            {
                if(System.currentTimeMillis() - mLastTime < mUpdateTime)
                {
                    return; //nothing to do, been less than 10ms.
                }
                else
                {
                    mLastTime = System.currentTimeMillis();
                }
            }
            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.draw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
