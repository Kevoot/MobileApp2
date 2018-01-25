package appdev2.gameofdeath;

/**
 * Created by Kevin on 1/25/2018.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class CellGridSurface extends SurfaceView {
    // Value for any void space (should use background setting instead of painting
    // as opposed to paint each individual cell with this)

    // Only one of these should be loaded at any given time.
    // Attach any given one of them to the mHandler
    public final OnTouchListener mPasteHandler;

    // Indicates if the initial Grid has it's cellGrid initialized;
    public static boolean mInitialized = false;

    // Cell radius (half an adjustment)
    public int mCellRadius;

    // View size (in # of pixels)
    public int mViewSizeX, mViewSizeY;

    // Grid size (in # of cells)
    public int mGridSizeX, mGridSizeY;

    // Adjustment ratio from view size to grid size (for touch handling)
    public int xAdjust, yAdjust;

    // Number of ticks per turn
    public int mTickRate = 1;

    // Actual grid containing 0 or 1's indicating dead or alive cell
    public Cell [][] mCellGrid;

    // Pasting Grid
    public Cell[][] mPasteGrid;

    // Delay in milliseconds between each simulation step
    public int mDelay;

    // Handler for running simulation loop
    public final Handler mHandler = new Handler();

    // Runnable to attach to handler
    // Unsure if we'll need this, except for ticks possibly
    // At bare minimum will need to be modified to watch for end of turn, and then only
    // proceed through a set number of ticks
    /* final Runnable mRunnable = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(this);
            step();
            DrawGrid();
            mHandler.postDelayed(this, mDelay);
        }
    };*/

    private Bitmap mCurrentBg;
    private Bitmap mPreviewBitmap;

    private SurfaceHolder holder;
    private CellGridLoopThread cellThread;

    public CellGridSurface(Context context, AttributeSet attrs) {
        // public CellGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cellThread = new CellGridLoopThread(this);
        holder = getHolder();

        // Initially set to 1 second between each step
        mDelay = 1000;

        // TODO: Figure out this based on initial grid size;
        mCellRadius = 0;

        // Responsible for starting drawing thread
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = true;
                cellThread.setRunning(false);
                while (retry) {
                    try {
                        cellThread.join();
                        retry = false;
                    } catch (InterruptedException e) { }
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                cellThread.setRunning(true);
                cellThread.start();
                // Canvas c = holder.lockCanvas(null);
                // onDraw(c);
                // holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

        });

        //Added Kevin & James - Pasting to grid
        mPasteHandler = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mPreviewBitmap == null) throw new Error("No Preview Bitmap found!");
                Canvas c = null;
                try {
                    c = getHolder().lockCanvas();
                    int x = 0;
                    int y = 0;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Paste initial preview over grid
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // Drag it around
                            break;
                        case MotionEvent.ACTION_UP:
                            // Copy cells into grid
                            v.performClick();
                            break;
                        default:
                            break;
                    }
                    synchronized (getHolder()) {
                        // x and y = top left coordinate of surface
                        // Will have to create a bitmap and draw to it as above during each event
                        // as they are handled
                        //c.drawBitmap(tempBg, x, y, mAliveCellPaint);
                        draw(c);
                    }
                }
                finally {
                    if (c != null) {
                        getHolder().unlockCanvasAndPost(c);
                    }
                }
                return true;
            }
        };
    }

    //Added Kevin - Transfer the pasting grid onto active grid
    public void PasteCellsToGrid(Cell[][] cells, int xOffset, int yOffset) {
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[0].length; j++) {
                if(i + xOffset + 2 >= mCellGrid.length ||
                        xOffset + i < 0 ||
                        yOffset + j < 0 ||
                        j + yOffset + 2 >= mCellGrid[0].length) continue;
                mCellGrid[i + xOffset + 2][j + yOffset + 2] = cells[i][j];
            }
        }
    }

    //Added Kevin - initialize the grid with random cells
    public void initGrid() {
        mViewSizeY = this.getHeight();
        mViewSizeX = this.getWidth();

        // If either of the dimension don't get populated, can't begin grid init.
        if(mViewSizeX == 0 || mViewSizeY == 0) {
            return;
        }

        // Actual adjusted grid dimensions
        mGridSizeX = mViewSizeX / xAdjust;
        mGridSizeY = mViewSizeY / yAdjust;

        // Create a grid of cells and a grid of colors for those cells
        mCellGrid = new Cell[mGridSizeX][mGridSizeY];

        // Uncomment once mHandler is running at ticks per turn, and can watch for
        // user turn completion
        // completing end of turn
        // mHandler.postDelayed(mRunnable, 1000);
        this.mInitialized = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!this.mInitialized) {
            initGrid();
            this.mInitialized = true;
        }

        // TODO: Modify this to redraw background before drawing cells
        // Paint paint = new Paint();
        // mCurrentBg = Bitmap.createBitmap(mViewSizeX, mViewSizeY, Bitmap.Config.ARGB_8888);
        // paint.setAntiAlias(true);
        // paint.setColor(Color.GREEN);
        // canvas.drawRect(0, 0, mViewSizeX, mViewSizeY, mDeadCellPaint);

        // Paint each cell according to their internal color
        for(int i = 0; i < mGridSizeX; i++) {
            for(int j = 0; j < mGridSizeY; j++) {
                canvas.drawCircle(i * xAdjust, j * yAdjust, mCellRadius, mCellGrid[i][j].getTypeColor());
            }
        }
    }

    // Steps of the simulation
    // Will have to modify this to allow for separate iteration of types
    /*public void step() {
        boolean[][] future = new boolean[mGridSizeX][mGridSizeY];

        // Loop through every cell
        for (int l = 1; l < mGridSizeX - 1; l++)
        {
            for (int m = 1; m < mGridSizeY - 1; m++)
            {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        aliveNeighbours += mCellGrid[l + i][m + j] ? 1 : 0;

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= mCellGrid[l][m] ? 1 : 0;

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((mCellGrid[l][m] == true) && (aliveNeighbours < 2))
                    future[l][m] = false;

                    // Cell dies due to over population
                else if ((mCellGrid[l][m] == true) && (aliveNeighbours > 3))
                    future[l][m] = false;

                    // A new cell is born
                else if ((mCellGrid[l][m] == false) && (aliveNeighbours == 3))
                    future[l][m] = true;

                    // Remains the same
                else
                    future[l][m] = mCellGrid[l][m];
            }
        }

        mPrevCellGrid = mCellGrid;
        mCellGrid = future;
    }*/

    // TODO: Uncomment these once mRunnable is set up for turn based game
    /* public void pause() {
        mHandler.removeCallbacks(mRunnable);
    } */
    /* public void resume() {
        mHandler.postDelayed(mRunnable, mDelay);
    }*/

    // Needed so android studio wont yell at me for doing the touch listener
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    // Allows for drawing transparent pasting grid over the current Surface
    private Bitmap overlay(Bitmap bmp1, Bitmap bmp2, int x, int y) {
        Bitmap bmOverlay = Bitmap.createBitmap(bmp1.getWidth(), bmp1.getHeight(), bmp1.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(bmp1, new Matrix(), null);

        // double xNewAdjust = (double)mPasteGrid.mCreatedGridSizeX / (double)mGridSizeX;
        // double yNewAdjust = (double)mPasteGrid.mCreatedGridSizeY / (double)mGridSizeY;

        // Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp2, (int)(bmp2.getWidth() * xNewAdjust), (int)(bmp2.getHeight() * yNewAdjust), false);

        BitmapDrawable bd = new BitmapDrawable(bmp2);
        bd.setAlpha(50);
        canvas.drawBitmap(bd.getBitmap(), x, y, null);
        return bmOverlay;
    }
}

