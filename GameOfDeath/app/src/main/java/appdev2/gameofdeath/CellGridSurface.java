package appdev2.gameofdeath;

/**
 * Created by Kevin on 1/25/2018.
 */
/* TODO: This crashes out if you hit the back button. When this activity
 * is destroyed, the Runnable needs to be destroyed
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;

import java.util.Random;

import static android.os.Debug.waitForDebugger;
import static appdev2.gameofdeath.Cell.getTypeColorFromType;
import static appdev2.gameofdeath.CellType.DEAD;

public class CellGridSurface extends SurfaceView {
    // Value for any void space (should use background setting instead of painting
    // as opposed to paint each individual cell with this)

    // Only one of these should be loaded at any given time.
    // Attach any given one of them to the mHandler
    public static OnTouchListener mPasteHandler;
    public static OnTouchListener mPaintHandler;

    // Indicates if the initial Grid has it's cellGrid initialized;
    public static boolean mInitialized = false;

    // View size (in # of pixels)
    public int mViewSizeX, mViewSizeY;

    // Adjustment ratio from view size to grid size (for touch handling)
    // public int xAdjust, yAdjust;

    // Number of ticks per turn
    public int mTickRate = 1;

    // Actual grid containing 0 or 1's indicating dead or alive cell
    public static Cell [][] mCellGrid;

    // Pasting Grid
    public Cell[][] mPasteGrid;


    // Cells per grid in X and Y directions, as well as sizes in pixels
    private int xCellsCount, yCellsCount, cellWidth, cellHeight;

    // Delay in milliseconds between each simulation step
    int mDelay = 0;
    int stepDelay = 500; // how long between steps
    long mLastTime = -1;
    long mUpdateTime = 500;

    // Paint for the grid lines
    Paint linePaint = new Paint();

    //preview vals
    int mPreviewX;
    int  mPreviewY;
    public boolean drawPreview = false;
    public boolean isDebug = true;
    public Cell[][] mPreviewGrid;
    int seed1Width = 2;
    int seed1Height = 3;

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
    private Bitmap mPreviewBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.seed1);;

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

        // Settings for the grid lines
        linePaint.setStrokeWidth(2);
        linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linePaint.setColor(Color.WHITE);


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
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Paste initial preview over grid
                            drawPreview = true;
                            mPreviewX = (int) event.getX();
                            mPreviewY = (int) event.getY();
                            if(mPreviewY < mViewSizeY/2)
                                mPreviewY = mViewSizeY/2;

                            break;
                        case MotionEvent.ACTION_MOVE:
                            // Drag it around
                            drawPreview = true;
                            mPreviewX = (int) event.getX();
                            mPreviewY = (int) event.getY();
                            if(mPreviewY < mViewSizeY/2)
                                mPreviewY = mViewSizeY/2;

                            mPreviewX = mPreviewX - (mPreviewX % cellWidth);
                            mPreviewY = mPreviewY - (mPreviewY % cellHeight);

                            break;
                        case MotionEvent.ACTION_UP:
                            // Copy cells into grid
                            //need to implement, will
                            drawPreview = false;
                            copyToGrid(mPreviewX / cellWidth, mPreviewY / cellHeight);
                            v.performClick();
                            break;
                        default:
                            break;
                    }
                    CellGridSurface.super.postInvalidate();
                    synchronized (getHolder()) {
                        // x and y = top left coordinate of surface
                        // Will have to create a bitmap and draw to it as above during each event
                        // as they are handled

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

        // TODO: This needs reworking, should be easier now that the cells width/heigh are defined
        mPaintHandler = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Canvas c = null;

                try {
                    int paintX = 0, paintY = 0;
                    c = getHolder().lockCanvas();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Paste initial preview over grid
                            // drawPreview = true;
                            paintX = (int) event.getX();
                            paintY = (int) event.getY();
                            // Check X's
                            if(paintX <= 0) {
                                paintX = 1;
                            }
                            else if(paintX >= mViewSizeX) {
                                paintX = mViewSizeX - 1;
                            }
                            // Check Y's
                            if(paintY < mViewSizeY/2) {
                                paintY = mViewSizeY / 2;
                            }
                            else if(paintY >= mViewSizeY - cellHeight) {
                                paintY = mViewSizeY - cellHeight;
                            }

                            c.drawRect(paintX, paintY, paintX + cellWidth,
                                        paintY + cellHeight, Cell.getTypeColorFromType(CellType.PLAYER));
                            setPaintCellPlayer(paintX, paintY);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            // Do nothing, since we only want to click cells to color,
                            // never dragging around
                            break;
                        case MotionEvent.ACTION_UP:
                            c.drawRect(paintX, paintY, paintX + cellWidth,
                                    paintY + cellHeight, Cell.getTypeColorFromType(CellType.PLAYER));
                            setPaintCellPlayer(paintX, paintY);
                            v.performClick();
                            break;
                        default:
                            break;
                    }
                    CellGridSurface.super.postInvalidate();
                    synchronized (getHolder()) {
                        // x and y = top left coordinate of surface
                        // Will have to create a bitmap and draw to it as above during each event
                        // as they are handled

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

    //copy the seed to the grid
    //will have an 2d array for each seed that's filled in with player/
    public void copyToGrid(int leftX, int topY) {
        //check it won't go out of bounds
        if (leftX + seed1Width > mCellGrid.length || topY + seed1Height > mCellGrid[0].length)
            return;
        //copy seed grid to cell grid, preserving cells already on the grid.
        for(int i = leftX; i < leftX + seed1Width; i++) { // i = x
            for(int j = topY; j < topY + seed1Height; j++) { // j = y
                if(mPreviewGrid[i - leftX][j - topY].getType() == CellType.PLAYER)
                    mCellGrid[i][j] = mPreviewGrid[i-leftX][j-topY];
            }
        }
        return;
    }

    public void setPaintCellPlayer(int leftX, int topY) {
        /*if(leftX >= mViewSizeX)
            leftX = mViewSizeX -1;
        if(topY >= mViewSizeY - cellHeight)
            topY = mViewSizeY - cellHeight - 1;*/
        mCellGrid[leftX/cellWidth][topY/cellHeight].type = CellType.PLAYER;
    }

    public void updateFPS(long newUpdateTime) {
        cellThread.setFPS(newUpdateTime);
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

    public void completeTurn(final CellType type, int steps) {
        for (int i = 0; i < steps; i++) {
            // postDelayed fixes this because it tells the GPU to draw on next cycle otherwise, it has already passed the draw by now. Kinda odd, but it is what it is.. lol
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    Canvas c = null;
                    step(type);
                    invalidate();

                    try {
                        c = getHolder().lockCanvas();
                        synchronized (getHolder()) {
                            draw(c);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (c != null) {
                            getHolder().unlockCanvasAndPost(c);
                        }
                    }
                }
            }, stepDelay*i);
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

        mPreviewGrid = new Cell[seed1Width][seed1Height];
        // initialize all to dead at first via default constructor
        for(int i = 0; i < mCellGrid.length; i++) {
            for(int j = 0; j < mCellGrid[i].length; j++) {
                mCellGrid[i][j] = new Cell();
            }
        }
        for(int i = 0; i < mPreviewGrid.length; i++) {
            for(int j = 0; j < mPreviewGrid[i].length; j++) {
                mPreviewGrid[i][j] = new Cell();
            }
        }

        // Testing cells
        mCellGrid[0][0].type = CellType.PLAYER;
        mCellGrid[0][1].type = CellType.PLAYER;
        mCellGrid[0][2].type = CellType.PLAYER;
        mCellGrid[2][0].type = CellType.PLAYER;
        mCellGrid[1][0].type = CellType.PLAYER;
        mCellGrid[3][3].type = CellType.PLAYER;
        mCellGrid[5][0].type = CellType.PLAYER;
        mCellGrid[5][1].type = CellType.PLAYER;
        mCellGrid[5][2].type = CellType.PLAYER;
        mCellGrid[5][3].type = CellType.PLAYER;

        mCellGrid[4][0].type = CellType.ENEMY;
        mCellGrid[4][1].type = CellType.ENEMY;
        mCellGrid[4][2].type = CellType.ENEMY;
        mCellGrid[4][3].type = CellType.ENEMY;


        mPreviewGrid[seed1Width - 1][seed1Height - 1].type = CellType.PLAYER;
        mPreviewGrid[seed1Width - 1][seed1Height - 3].type = CellType.PLAYER;
        mPreviewGrid[seed1Width - 2][seed1Height - 2].type = CellType.PLAYER;



        mHandler.postDelayed(mRunnable, 250);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!mInitialized) {
            initGrid();
            mInitialized = true;
        }

        if(drawPreview) {
            canvas.drawBitmap(mPreviewBitmap, mPreviewX, mPreviewY, null);
        }

        if(isDebug)
        {
            Paint dbPaint = new Paint();
            dbPaint.setStyle(Paint.Style.FILL);
            dbPaint.setTextSize(80);
            dbPaint.setColor(Color.WHITE);
            canvas.drawText("CellWidth: " + (Integer.toString(cellWidth)), 700, 100, dbPaint);
            canvas.drawText("Cell Height: " + (Integer.toString(cellHeight)), 700, 200, dbPaint);
            canvas.drawText("X: " + (Integer.toString(mPreviewX)), 700, 300, dbPaint);
            canvas.drawText("Y: " + (Integer.toString(mPreviewY)), 700, 400, dbPaint);
        }

        // Paint each cell according to their internal color
        for(int i = 0; i < xCellsCount; i++) { // i = x
            for(int j = 0; j < yCellsCount; j++) { // j = y
                if(mCellGrid[i][j] == null) {
                    canvas.drawRect(i*cellWidth, j*cellHeight, (i*cellWidth) + cellWidth,
                            (j*cellHeight) + cellHeight, getTypeColorFromType(DEAD));
                }
                canvas.drawRect(i*cellWidth, j*cellHeight, (i*cellWidth) + cellWidth,
                        (j*cellHeight) + cellHeight, mCellGrid[i][j].getTypeColor());
            }
        }

        // Now draw lines for the overall grid
        for(int i = 1; i < xCellsCount; i++) {
            for(int j = 1; j < yCellsCount; j++) {
                canvas.drawLine(i * cellWidth, 1, i * cellWidth,
                        mViewSizeY - 1, linePaint);
                canvas.drawLine(1, j * cellHeight, mViewSizeX - 1,
                        j * cellHeight, linePaint);
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
        deadPaint.setColor(Color.BLACK);
        // Draw background
        canvas.drawRect(0, 0, mViewSizeX, mViewSizeY, deadPaint);

        setBackground(null);
        setBackgroundDrawable(new BitmapDrawable(mCurrentBg));
    }

    // Steps of the simulation
    // TODO: Will need to modify this to only change similar colors nearby (check cell type)
    public void step(CellType type) {
        Cell[][] future = new Cell[xCellsCount][yCellsCount];
        // Have to initialize each

        // Loop through every cell and initialize all to dead
        for(int i = 0; i < future.length; i++) {
            for(int j = 0; j < future[i].length; j++) {
                future[i][j] = new Cell();
            }
        }


        for (int l = 1; l < xCellsCount - 1; l++)
        {
            for (int m = 1; m < yCellsCount - 1; m++)
            {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                    for (int j = -1; j <= 1; j++)
                        if(mCellGrid[l + i][m + j].type == type) {
                            aliveNeighbours += 1;
                        }
                // The cell needs to be subtracted from
                // its neighbours as it was counted before

                // aliveNeighbours = countNeighbors(mCellGrid, l, m, mCellGrid[l][m].type);
                if(mCellGrid[l][m].type == type) {
                    aliveNeighbours -= 1;
                }

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((mCellGrid[l][m].type == type) && (aliveNeighbours < 2))
                    future[l][m].type = DEAD;

                    // Cell dies due to over population
                else if ((mCellGrid[l][m].type == type) && (aliveNeighbours > 3))
                    future[l][m].type = DEAD;

                    // A new cell is born
                else if ((mCellGrid[l][m].type == DEAD) && (aliveNeighbours == 3))
                    future[l][m].type = type;

                    // Remains the same
                else
                    future[l][m] = mCellGrid[l][m];
            }
        }

        DestroyOpposingCells(future);

        mCellGrid = future;
    }

    private int countNeighbors(final Cell[][] generation, final int row, final int col, final CellType type) {
        int numNeighbors = 0;

        // Look NW
        if ((row - 1 >= 0) && (col - 1 >= 0)) {
            numNeighbors = generation[row - 1][col - 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look W
        if ((row >= 0) && (col - 1 >= 0)) {
            numNeighbors = generation[row][col - 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look SW
        if ((row + 1 < generation.length) && (col - 1 >= 0)) {
            numNeighbors = generation[row + 1][col - 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look S
        if ((row + 1 < generation.length) && (col < generation[0].length)) {
            numNeighbors = generation[row + 1][col].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look SE
        if ((row + 1 < generation.length) && (col + 1 < generation[0].length)) {
            numNeighbors = generation[row + 1][col + 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look E
        if ((row < generation.length) && (col + 1 < generation[0].length)) {
            numNeighbors = generation[row][col + 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look NE
        if ((row - 1 >= 0) && (col + 1 < generation[0].length)) {
            numNeighbors = generation[row - 1][col + 1].type == type ? numNeighbors + 1 : numNeighbors;
        }
        // Look N
        if ((row - 1 >= 0) && (col < generation[0].length)) {
            numNeighbors = generation[row - 1][col].type == type ? numNeighbors + 1 : numNeighbors;
        }

        return numNeighbors;
    }

    // Destroy any directly cells (perpendicular and diagonal)
    // Priority given to perpendicular, vertically opposing cells
    private void DestroyOpposingCells(final Cell[][] grid) {
        // Check rest of cells
        for (int i = 1; i < xCellsCount - 1; i++) {
            for (int j = 1; j < yCellsCount - 1; j++) {
                // Check Vertical, will get above and below
                if(checkOppositions(grid[i][j], grid[i][j-1])) {
                    grid[i][j].type = CellType.DEAD;
                    grid[i][j-1].type = CellType.DEAD;
                }
                // Check horizontals
                else if(checkOppositions(grid[i][j], grid[i][j-1])) {
                    grid[i][j].type = CellType.DEAD;
                    grid[i][j-1].type = CellType.DEAD;
                }
                // Check top-left and bottom-right
                else if(checkOppositions(grid[i][j], grid[i-1][j-1])) {
                    grid[i][j].type = CellType.DEAD;
                    grid[i][j-1].type = CellType.DEAD;
                }
                // Check top-right and bottom-left
                else if(checkOppositions(mCellGrid[i][j], mCellGrid[i+1][j-1])) {
                    grid[i][j].type = CellType.DEAD;
                    grid[i][j-1].type = CellType.DEAD;
                }
                if(((grid[i][j].type == CellType.PLAYER && grid[i][j-1].type == CellType.ENEMY) ||
                        (mCellGrid[i][j].type == CellType.ENEMY && mCellGrid[i][j-1].type == CellType.PLAYER))) {
                    mCellGrid[i][j].type = CellType.DEAD;
                    mCellGrid[i][j-1].type = CellType.DEAD;
                }
            }
        }
    }

    private boolean checkOppositions(Cell p1, Cell p2) {
        return ((p1.type == CellType.PLAYER && p2.type == CellType.ENEMY) ||
                (p1.type == CellType.ENEMY && p2.type == CellType.PLAYER));
    }

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

