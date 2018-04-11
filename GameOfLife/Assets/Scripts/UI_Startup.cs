using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class UI_Startup : MonoBehaviour {
    public GameObject Level_Select_UI;
    public GameObject Game_UI;
    public GameObject Staging;
    public GameObject Seed_Menu_UI;
    public GameObject cam1;

	// Use this for initialization
	void Start () {

    }
	
	// Update is called once per frame
	void Update () {
        Level_Select_UI.SetActive(false);
        Game_UI.SetActive(false);
        Staging.SetActive(false);
        Seed_Menu_UI.SetActive(false);
        //cam1.SetActive(false);
    }
}
