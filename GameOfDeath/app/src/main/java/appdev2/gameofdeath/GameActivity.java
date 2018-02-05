package appdev2.gameofdeath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import static android.os.Debug.waitForDebugger;
import static appdev2.gameofdeath.CellGridSurface.mInitialized;

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
            // TODO: Move initial Cell generation (aka level setup) to here
            // Need to pass value into CellGridSurface to do this. Not sure how just yet.
        }

        surface = findViewById(R.id.cellGridView);

        Button finishTurnButton = findViewById(R.id.game_container_finish_turn);
        finishTurnButton.setOnClickListener(new SurfaceView.OnClickListener() {

            @Override
            public void onClick(View v) {
                waitForDebugger();
                // TODO: This is for testing, will need to receive ENEMY or PLAYER for each round.
                surface.pause();
                surface.completeTurn(CellType.PLAYER);
                surface.resume();
            }
        });
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
