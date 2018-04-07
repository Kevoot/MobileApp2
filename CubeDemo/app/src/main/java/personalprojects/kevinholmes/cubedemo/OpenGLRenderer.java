package personalprojects.kevinholmes.cubedemo;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kevin on 3/27/2018.
 */

class OpenGLRenderer implements GLSurfaceViewExt.Renderer {

    private float mCubeRotation;
    private int zoomLevel = -1;

    private Cube[][] mCubes = new Cube[24][24];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_NICEST);

        for(int i = 0; i < 24; i++) {
            for(int j = 0; j < 6; j++) {
                mCubes[i][j] = new Cube(2);
            }
        }

        for(int i = 0; i < 24; i++) {
            for(int j = 6; j < 18; j++) {
                mCubes[i][j] = new Cube(0);
            }
        }

        for(int i = 0; i < 24; i++) {
            for(int j = 18; j < 24; j++) {
                mCubes[i][j] = new Cube(1);
            }
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearDepthf(2.0f);
        gl.glLoadIdentity();

        // gl.glTranslatef(-12.0f, -12.0f, -80.0f);
        if(zoomLevel == -1) {
            gl.glTranslatef(0.0f, 0.0f, -127.0f);
            zoomLevel = 0;
        }
        gl.glTranslatef(-30.0f, -26.0f, -90.0f - GLSurfaceViewExt.rotateX);

        // gl.glRotatef(mCubeRotation, GLSurfaceViewExt.rotateX, 0.0f, 0.0f);
        drawGrid(gl);
        gl.glLoadIdentity();
        // gl.glTranslatef(0.0f, -12 * 2.5f, 0.0f);

        // mCubeRotation -= 0.15f;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        /*gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();*/
        /*
		 * Set our projection matrix. This doesn't have to be done
		 * each time we draw, but usually a new projection needs to
		 * be set when the viewport is resized.
		 */
        gl.glViewport(0, 0, width, height);
        // To prevent divide by zero
        float aspect = (float)width / height;
        // Set the viewport (display area) to cover the entire window.
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection, with aspect ratio matches viewport
        // Select projection matrix.
        gl.glMatrixMode(GL10.GL_PROJECTION);
        // Reset projection matrix.
        gl.glLoadIdentity();
        // Use perspective projection.
        GLU.gluPerspective(gl, 70, aspect, 0.1f, 200.f);
        // Select model-view matrix.
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        // Reset.
        gl.glLoadIdentity();
    }


    public void drawGrid(GL10 gl) {
        for(int y = 0; y < 24; y++) {
            for(int x = 0; x < 24; x++) {
                gl.glTranslatef(2.5f, 0.0f, 0.0f);
                mCubes[x][y].draw(gl);
            }
            gl.glTranslatef(-24 * 2.5f, 2.5f, 0.0f);
        }
    }
}
