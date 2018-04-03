﻿using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public enum PlayerType
{
    DEAD = 0,
    DEADNEXT = 1,
    ENEMY = 2,
    ENEMYNEXT = 3,
    PLAYER = 4,
    PLAYERNEXT = 5
}

public class Globals : MonoBehaviour {
    public static InitialCube[,] cubeGrid;
    public Transform prefab;
    public GameObject plane;

    public static bool completeTurnRequested = false;
    public static bool paintAllowed = false;

    // Next update in second
    private int nextUpdate = 1;

    // Use this for initialization
    void Start () {
        cubeGrid = new InitialCube[24, 24];
        for(var i = 0; i < 24; i++)
        {
            for(var j = 0; j < 24; j++)
            {
                cubeGrid[i, j] = new InitialCube();
                cubeGrid[i, j].prefab = Instantiate(prefab, new Vector3(i * 1.5f, 0.5f, j * 1.5f), Quaternion.identity);
            }
        }

        cubeGrid[1, 0].SetPlayerType(PlayerType.PLAYER);
        cubeGrid[2, 1].SetPlayerType(PlayerType.PLAYER);
        cubeGrid[0, 2].SetPlayerType(PlayerType.PLAYER);
        cubeGrid[1, 2].SetPlayerType(PlayerType.PLAYER);
        cubeGrid[2, 2].SetPlayerType(PlayerType.PLAYER);
    }

    // Update is called once per frame
    void Update () {
        if(completeTurnRequested)
        {
            step();
            completeTurnRequested = false;
        }
    }

    public void step()
    {
        InitialCube[,] future = new InitialCube[24, 24];

        foreach (var cell in cubeGrid)
        {
            Destroy(cell.prefab.gameObject);
        }

        for (var i = 0; i < 24; i++)
        {
            for (var j = 0; j < 24; j++)
            {
                future[i, j] = new InitialCube();
                future[i, j].prefab = Instantiate(prefab, new Vector3(i * 1.5f, 0.5f, j * 1.5f), Quaternion.identity);
            }
        }

        // Loop through every cell
        for (int l = 1; l < 24 - 1; l++)
        {
            for (int m = 1; m < 24 - 1; m++)
            {
                // finding no Of Neighbours that are alive
                int aliveNeighbours = 0;
                for (int i = -1; i <= 1; i++)
                     for (int j = -1; j <= 1; j++)
                        aliveNeighbours += cubeGrid[l + i, m + j].playerType != PlayerType.DEAD ? 1 : 0;

                // The cell needs to be subtracted from
                // its neighbours as it was counted before
                aliveNeighbours -= cubeGrid[l, m].playerType != PlayerType.DEAD ? 1: 0;

                // Implementing the Rules of Life

                // Cell is lonely and dies
                if ((cubeGrid[l, m].playerType != PlayerType.DEAD) && (aliveNeighbours < 2))
                    future[l, m].SetPlayerType(PlayerType.DEAD);

                // Cell dies due to over population
                else if ((cubeGrid[l, m].playerType != PlayerType.DEAD) && (aliveNeighbours > 3))
                    future[l, m].SetPlayerType(PlayerType.DEAD);

                // A new cell is born
                else if ((cubeGrid[l, m].playerType == PlayerType.DEAD) && (aliveNeighbours == 3))
                    future[l, m].SetPlayerType(PlayerType.ENEMY);

                // Remains the same
                else
                    future[l, m].SetPlayerType(cubeGrid[l, m].playerType);
                }
            }

        cubeGrid = future;

    }
}
