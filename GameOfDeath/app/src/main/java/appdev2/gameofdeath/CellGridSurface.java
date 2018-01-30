package appdev2.gameofdeath;

/**
 * Created by Kevin on 1/25/2018.
 */
/* TODO: This crashes out if you hit the back button. When this activity
 * is destroyed, the Runnable needs to be destroyed
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
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

    // View size (in # of pixels)
    public int mViewSizeX, mViewSizeY;

    // Adjustment ratio from view size to grid size (for touch handling)
    // public int xAdjust, yAdjust;

    // Number of ticks per turn
    public int mTickRate = 1;

    // Actual grid containing 0 or 1's indicating dead or alive cell
    public Cell [][] mCellGrid;

    // Pasting Grid
    public Cell[][] mPasteGrid;

    // Cells per grid in X and Y directions, as well as sizes in pixels
    private int xCellsCount, yCellsCount, cellWidth, cellHeight;

    // Delay in milliseconds between each simulation step
    public int mDelay;

    // horizontal and vertical grid-based line locations
    public int[] horizontalLineLocations;
    public int[] verticalLineLocations;

    // Handler for running simulation loop
    public final Handler mHandler = new Handler();

    // TODO: Modify this so it only runs through the specified number of ticks before it
    // stops looping.
    Runnable mRunnable = new Runnable() {
        public void run() {
            mHandler.removeCallbacks(this);
            DrawGrid();
            mHandler.postDelayed(this, mDelay);
        }
    };

    // Will be used for custom backdrops
    private Bitmap mCurrentBg;
    // Will be used for pasting a preview
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

        // For adjusting size of grid
        xCellsCount = 20;
        yCellsCount = 36;

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
            // Drawing must be handled in a separate thread or it locks the UI
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                cellThread.setRunning(true);
                cellThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }

        });

        // TODO: This needs reworking, should be easier now that the cells width/heigh are defined
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

    // For transferring cells from a paste fragment.
    // TODO: This will need to be reworked.
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

    // Initialize grid size and start the runnable/thread for drawing
    public void initGrid() {
        mViewSizeY = this.getHeight();
        mViewSizeX = this.getWidth();

        // If either of the dimension don't get populated, can't begin grid init.
        if(mViewSizeX == 0 || mViewSizeY == 0) {
            return;
        }

        cellHeight = mViewSizeY / yCellsCount;
        cellWidth = mViewSizeX / xCellsCount;

        // Create a grid of cells and a grid of colors for those cells
        // mCellGrid = new Cell[mGridSizeX][mGridSizeY];
        mCellGrid = new Cell[xCellsCount][yCellsCount];

        // initialize all to dead at first via default constructor
        for(int i = 0; i < mCellGrid.length; i++) {
            for(int j = 0; j < mCellGrid[i].length; j++) {
                mCellGrid[i][j] = new Cell();
            }
        }

        mHandler.postDelayed(mRunnable, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!mInitialized) {
            initGrid();
            mInitialized = true;
        }

        // Used for testing cells on the canvas - will be deleted once things
        // are further along
        mCellGrid[0][0].type = CellType.PLAYER;
        mCellGrid[xCellsCount - 1][yCellsCount - 1].type = CellType.PLAYER;
        mCellGrid[xCellsCount - 1][yCellsCount - 3].type = CellType.PLAYER;
        mCellGrid[xCellsCount - 2][yCellsCount - 2].type = CellType.ENEMY;

        // Paint each cell according to their internal color
        for(int i = 0; i < xCellsCount; i++) {
            for(int j = 0; j < yCellsCount; j++) {
                canvas.drawRect(i*cellWidth, j*cellHeight, (i*cellWidth) + cellWidth,
                        (j*cellHeight) + cellHeight, mCellGrid[i][j].getTypeColor());
            }
        }
    }

    // This constantly resets enemy, player, dead, or blocked cells so they can be repainted
    // and their old status doesn't show instead. Could probably use a renaming
    public void DrawGrid() {
        mCurrentBg = Bitmap.createBitmap(mViewSizeX, mViewSizeY, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mCurrentBg);

        Paint deadPaint = new Paint();
        deadPaint.setAntiAlias(true);
        deadPaint.setColor(Color.TRANSPARENT);
        // Draw background
        canvas.drawRect(0, 0, mViewSizeX, mViewSizeY, deadPaint);

        setBackground(null);
        setBackgroundDrawable(new BitmapDrawable(mCurrentBg));
    }

    // Steps of the simulation
    // TODO: Will need to modify this to only change similar colors nearby (check cell type)
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

    public void pause() {
        cellThread.setRunning(false);
        while (cellThread.isAlive()) {
            // Wait
        }
        mHandler.removeCallbacks(mRunnable);
    }
    public void resume() {
        mHandler.postDelayed(mRunnable, mDelay);
    }

    // Needed so android studio wont yell at me for doing the touch listener
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    // Allows for drawing transparent pasting grid over the current Surface
    // TODO: this is still the same implementation as the old one, and will need reworking
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

