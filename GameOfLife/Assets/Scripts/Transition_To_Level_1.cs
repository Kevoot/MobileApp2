using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Transition_To_Level_1 : MonoBehaviour {
    public Button yourButton;
    public GameObject Level_Select_UI;
    public GameObject Game_UI;
    public GameObject Staging;
    public GameObject cam;

    // Use this for initialization
    void Start () {
        Button btn = yourButton.GetComponent<Button>();
        btn.onClick.AddListener(TaskOnClick);
    }

    void TaskOnClick()
    {
        Game_UI.SetActive(true);
        Level_Select_UI.SetActive(false);
        Staging.SetActive(true);
    }
}
