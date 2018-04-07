using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PaintButton : MonoBehaviour {
    public Button yourButton;
    public GameObject MainCamScriptObj;

    // Use this for initialization
    void Start () {
        Button btn = yourButton.GetComponent<Button>();
        btn.onClick.AddListener(TaskOnClick);
    }
	
    void TaskOnClick()
    {
        if (!Globals.paintAllowed)
        {
            Globals.paintAllowed = true;
            GameObject.Find("Paint").GetComponentInChildren<Text>().text = "Finish Painting";
            MainCamScriptObj.SetActive(false);
        }
        else
        {
            Globals.paintAllowed = false;
            GameObject.Find("Paint").GetComponentInChildren<Text>().text = "Paint";
            MainCamScriptObj.SetActive(true);
        }
    }
}
