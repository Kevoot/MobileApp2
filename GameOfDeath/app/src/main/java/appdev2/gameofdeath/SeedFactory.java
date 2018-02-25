package appdev2.gameofdeath;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.util.HashMap;

public class SeedFactory {

    //map between seed name and seed itself
    private HashMap<String, Seed> mSeedMap;

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

        //Example Seed - Basic Spaceship
        int[][] seed1Grid = {
                {0,0,1},
                {1,0,1},
                {0,1,1}};
        String seed1Name = "Basic Spaceship";
        String seed1ImageFile = "seed1.jpg";

        Seed seed1 = GenerateSeed(seed1Name, seed1ImageFile, seed1Grid);

        mSeedMap.put(seed1Name, seed1);
    }

    public Seed getSeed(String name){
        if(mSeedMap.containsKey(name)){
            return mSeedMap.get(name);
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
