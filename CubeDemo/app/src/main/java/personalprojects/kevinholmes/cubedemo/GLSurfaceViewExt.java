package personalprojects.kevinholmes.cubedemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kevin on 3/27/2018.
 */

public class GLSurfaceViewExt extends GLSurfaceView {
    public static float rotateX = 0.0f, rotateY = 0.0f, rotateZ = 0.0f;

    public GLSurfaceViewExt(Context context) {
        super(context);
        OnTouchListener listener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rotateX += 0.2f;
                return true;
            }
        };
        this.setOnTouchListener(listener);
    }
}
