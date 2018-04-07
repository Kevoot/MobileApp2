using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Transition_To_Level_Select : MonoBehaviour {
    public Button yourButton;
    public GameObject Level_Select_UI;
    public GameObject Title_UI;

    // Use this for initialization
    void Start()
    {
        Button btn = yourButton.GetComponent<Button>();
        btn.onClick.AddListener(TaskOnClick);
    }

    void TaskOnClick()
    {
        Level_Select_UI.SetActive(true);
        Title_UI.SetActive(false);

    }
}
