package appdev2.gameofdeath;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.HashMap;

public class SeedFactory {

    //map between seed name and seed itself
    private HashMap<SeedType, Seed> mSeedMap;

    private Seed mDefaultSeed;

    public SeedFactory(){
        mSeedMap = new HashMap<>();

        //a default seed that is just a stable block
        int[][] defaultGrid = {
                {1,1},
                {1,1}
        };
        String defaultName = "Default Seed";
        String defaultImageFile = "default.jpg";
        mDefaultSeed = GenerateSeed(defaultName, defaultImageFile, defaultGrid);

        /***************************************************************
        *The Following is the creation of the different seeds we want. *
        ****************************************************************/

        //Example Seed - Angle Spaceship
        int[][] seed1Grid = {
                {0,0,1},
                {1,0,1},
                {0,1,1}};
        SeedType seed1Type = SeedType.SPACESHIP_ANGLE;
        String seed1Name = "Angle Spaceship";
        String seed1ImageFile = "seed1.jpg";

        Seed seed1 = GenerateSeed(seed1Name, seed1ImageFile, seed1Grid);
        mSeedMap.put(seed1Type, seed1);

        // Straight Spaceship
        int[][] seed2Grid = {
                {0,1,1,1,1},
                {1,0,0,0,1},
                {0,0,0,0,1},
                {1,0,0,1,0}};
        SeedType seed2Type = SeedType.SPACESHIP_STRT;
        String seed2Name = "Straight Spaceship";
        String seed2ImageFile = "seed2.jpg";

        Seed seed2 = GenerateSeed(seed2Name, seed2ImageFile, seed2Grid);
        mSeedMap.put(seed2Type,seed2);

        // Still Diamond
        int[][] seed3Grid = {
                {0,1,0},
                {1,0,1},
                {0,1,0}};
        SeedType seed3Type = SeedType.STILL_DMND;
        String seed3Name = "Stationary Diamond";
        String seed3ImageFile = "seed3.jpg";

        Seed seed3 = GenerateSeed(seed3Name, seed3ImageFile, seed3Grid);
        mSeedMap.put(seed3Type, seed3);
    }

    public Seed getSeed(SeedType sType){
        if(mSeedMap.containsKey(sType)){
            return mSeedMap.get(sType);
        }
        else{
            return mDefaultSeed;
        }
    }

    private Seed GenerateSeed(String name, String imageFile, int[][] grid) {
        File file = new File(imageFile);
        Bitmap seedImage = BitmapFactory.decodeFile(file.getAbsolutePath());

        int seedX = grid.length;
        int seedY = grid[0].length;
        Cell[][] seedGrid = new Cell[seedX][seedY];

        Cell cell = new Cell();
        for(int x = 0; x < grid.length; x++){
            for(int y = 0; y < grid[x].length; y++){
                if(grid[x][y] == 1){
                    cell.type = CellType.PLAYER;
                }
                seedGrid[x][y] = cell;
            }
        }

        Seed seed = new Seed(name, seedImage, seedGrid);

        return seed;
    }
}
