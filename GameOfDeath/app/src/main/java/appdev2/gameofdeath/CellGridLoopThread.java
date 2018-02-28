package appdev2.gameofdeath;

import android.graphics.Canvas;

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


    public void setFPS(long newUpdateTime) {mUpdateTime = newUpdateTime;}
    public long getFPS() {return mUpdateTime;}

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
