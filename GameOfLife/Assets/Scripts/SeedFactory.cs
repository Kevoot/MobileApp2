using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SeedFactory
{
    private Dictionary<SeedType,Seed> _seedDict;

	public SeedFactory()
    {
        _seedDict = new Dictionary<SeedType, Seed>();

        int[,] seedArray1 = { { 0, 1, 0 }, { 1, 0, 1 }, { 0, 1, 0 } };
        Seed seed1 = new Seed(seedArray1);
        _seedDict.Add(SeedType.DIAMOND, seed1);

        int[,] seedArray2 = { { 1, 0, 0 }, { 1, 0, 1 }, { 1, 1, 0 } };
        Seed seed2 = new Seed(seedArray2);
        _seedDict.Add(SeedType.GLIDER_R, seed2);

        int[,] seedArray3 = { { 1, 1, 0 }, { 1, 0, 1 }, { 1, 0, 0 } };
        Seed seed3 = new Seed(seedArray3);
        _seedDict.Add(SeedType.GLIDER_R, seed3);

        int[,] seedArray4 = { { 0,1,0,0,1 }, { 1,0,0,0,0 }, { 1,0,0,0,1 }, { 1,1,1,1,0 } };
        Seed seed4 = new Seed(seedArray4);
        _seedDict.Add(SeedType.SPACESHIP, seed4);
    }

    //Returns the seed based on the type you want.
    public Seed getSeed(SeedType type)
    {
        if(_seedDict.ContainsKey(type))
        {
            return _seedDict[type];
        }
        else
        {
            return _seedDict[SeedType.DIAMOND];
        }
    }
}