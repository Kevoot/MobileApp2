using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Seed
{
    public int[,] Grid { get { return _grid; } }

    int[,] _grid;

    public Seed(int[,] input)
    {
        this._grid = input;
    }

    //Turns the integer based array into usable Cube objects
    public InitialCube[,] getCubeGrid()
    {
        int xLength = _grid.GetLength(0);
        int yLength = _grid.GetLength(1);
        InitialCube[,] cubeGrid = new InitialCube[xLength, yLength];

        for(int x = 0; x < xLength; x++)
        {
            for(int y = 0; y < yLength; y++)
            {
                if(_grid[x,y] == 1)
                {
                    cubeGrid[x, y] = new InitialCube(PlayerType.PLAYER);
                }
                else
                {
                    cubeGrid[x, y] = new InitialCube(PlayerType.DEAD);
                }
            }
        }

        return cubeGrid;
    }
}
