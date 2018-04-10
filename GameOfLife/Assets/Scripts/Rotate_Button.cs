using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Rotate_Button : MonoBehaviour {
    public Button yourButton;
    public GameObject cam;
    public GameObject plane;
    private int state = 0; // 0 = front, 1 = left, 2 = back, 3 = right

    public GameObject BottonCamPosObject;
    public GameObject RightCamPosObject;
    public GameObject TopCamPosObject;
    public GameObject LeftCamPosObject;

    private Vector3 startLocation;
    private Vector3 endLocation;

    private Vector3 startRotation;
    private Vector3 endRotation;


    // Use this for initialization
    void Start () {

    }

    void Update()
    {
        if(startLocation != endLocation && startRotation != endRotation)
        {
            cam.transform.position = Vector3.Lerp(cam.transform.position, endLocation, Time.deltaTime * 3f);
            cam.transform.eulerAngles = Vector3.Lerp(cam.transform.eulerAngles, endRotation, Time.deltaTime * 3f);
        }
    }

    public void TaskOnClick()
    {
        Debug.Log("Button Clicked");
        state = (state + 1) % 4;
        switch(state)
        {
            // Bottom
            case 0:
                startLocation = cam.transform.position;
                endLocation = BottonCamPosObject.transform.position;
                startRotation = cam.transform.eulerAngles;
                endRotation = new Vector3(BottonCamPosObject.transform.eulerAngles.x, BottonCamPosObject.transform.eulerAngles.y, BottonCamPosObject.transform.eulerAngles.z);
                break;
            // Left
            case 1:
                startLocation = cam.transform.position;
                endLocation = LeftCamPosObject.transform.position;
                startRotation = cam.transform.eulerAngles;
                endRotation = new Vector3(LeftCamPosObject.transform.eulerAngles.x, LeftCamPosObject.transform.eulerAngles.y, LeftCamPosObject.transform.eulerAngles.z);
                break;
            // Top
            case 2:
                startLocation = cam.transform.position;
                endLocation = TopCamPosObject.transform.position;
                startRotation = cam.transform.eulerAngles;
                endRotation = new Vector3(TopCamPosObject.transform.eulerAngles.x, TopCamPosObject.transform.eulerAngles.y, TopCamPosObject.transform.eulerAngles.z);
                break;
            // Right
            case 3:
                startLocation = cam.transform.position;
                endLocation = RightCamPosObject.transform.position;
                startRotation = cam.transform.eulerAngles;
                endRotation = new Vector3(RightCamPosObject.transform.eulerAngles.x, RightCamPosObject.transform.eulerAngles.y, RightCamPosObject.transform.eulerAngles.z);
                break;
        }
    }
}
