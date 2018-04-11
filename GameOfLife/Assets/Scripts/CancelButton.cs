using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class CancelButton : MonoBehaviour {

    public Button yourButton;
    public GameObject Seed_Menu_UI;
    public GameObject Staging;
    public GameObject Game_UI;
    public GameObject cam1;
    public GameObject cam2;

    // Use this for initialization
    void Start()
    {
        Button btn = yourButton.GetComponent<Button>();
        btn.onClick.AddListener(TaskOnClick);
    }

    void TaskOnClick()
    {
        Seed_Menu_UI.SetActive(false);
        Staging.SetActive(true);
        Game_UI.SetActive(true);
        cam1.SetActive(true);
        cam2.SetActive(false);
    }
}
