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

    public CellGridLoopThread(CellGridSurface view) {
        this.view = view;
    }

    public void setRunning(boolean run) {
        running = run;
    }


    @Override
    public void run() {
        while (running) {
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
