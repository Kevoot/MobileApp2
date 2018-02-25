package appdev2.gameofdeath;

import android.graphics.Bitmap;

public class Seed {

    //Image to be displayed in the tray at the bottom of the play screen
    private Bitmap mPreviewBitmap;

    //The actual cells that make up the seed
    private Cell[][] mSeedGrid;

    //Name that describes the seed
    private String mName;

    public Seed(String name, Bitmap preview, Cell[][] grid){
        mName = name;
        mPreviewBitmap = preview;
        mSeedGrid = grid;
    }

    public String getName(){
        return mName;
    }

    public Bitmap getImage(){
        return mPreviewBitmap;
    }

    public Cell[][] getGrid(){
        return mSeedGrid;
    }
}
