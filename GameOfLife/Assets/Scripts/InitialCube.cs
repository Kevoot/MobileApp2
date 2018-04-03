using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class InitialCube {
    public int x, y;
    public PlayerType playerType;
    public Transform prefab;

	public InitialCube()
    {
        playerType = PlayerType.DEAD;
    }

    public InitialCube(PlayerType type)
    {
        this.playerType = type;
    }
	
	// Update is called once per frame
	void Update () {
		
	}

    public void SetPlayerType(PlayerType player)
    {
        var rend = prefab.gameObject.GetComponent<Renderer>();
        this.playerType = player;

        if(player == PlayerType.DEAD)
        {
            rend.material.SetColor("_Color", new Color(0.0f, 0.0f, 0.0f, 0.4f));
        }
        else if(player == PlayerType.DEADNEXT)
        {
            rend.material.SetColor("_Color", Color.gray);
        }
        else if(player == PlayerType.ENEMY)
        {
            rend.material.SetColor("_Color", Color.red);
        }
        else if(player == PlayerType.ENEMYNEXT)
        {
            rend.material.SetColor("_Color", Color.magenta);
        }
        else if(player == PlayerType.PLAYER)
        {
            rend.material.SetColor("_Color", Color.green);
        }
        else if(player == PlayerType.PLAYERNEXT)
        {
            rend.material.SetColor("_Color", Color.blue);
        }
        else
        {
            rend.material.SetColor("_Color", Color.black);
        }
    }
}
