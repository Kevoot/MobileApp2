package appdev2.gameofdeath;

import android.graphics.Canvas;

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
