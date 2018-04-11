using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class GifScript : MonoBehaviour {
    int TileX = 3;
    int TileY = 3;
    public float FPS = 5;
    public Sprite[] Frames;
    public GameObject m_button;
    
    void Update () {
        if (Frames.Length == 0)
            return;

        float index = Time.time * FPS;
        index = index % Frames.Length;
        m_button.GetComponent<Image>().sprite = Frames[(int)index];
    
	}
}
