package appdev2.gameofdeath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static appdev2.gameofdeath.CellGridSurface.mInitialized;

/**
 * Created by Kevin on 1/29/2018.
 */

public class GameActivity extends AppCompatActivity {
    private int m_levelNumber;
    private CellGridSurface surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_container);

        Bundle args = getIntent().getExtras();
        if(args != null) {
            m_levelNumber = args.getInt("Level");
        }

        surface = findViewById(R.id.cellGridView);
    }

    @Override
    public void onPause() {
        super.onPause();
        exitGame();
    }

    @Override
    public void onStop() {
        super.onStop();
        exitGame();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exitGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitGame();
    }

    private void exitGame() {
        surface.pause();
        mInitialized = false;
        this.finish();
    }
}