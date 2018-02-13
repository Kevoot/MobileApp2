package appdev2.gameofdeath;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

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

        // Need some bitmaps

        //temp bitmap
        Bitmap b = Bitmap.createBitmap(200, 200,  Bitmap.Config.ARGB_8888);
        b.eraseColor(Color.RED);

        //
        List<Bitmap> SeedList = new ArrayList<>();
        SeedList.add(b);
        SeedList.add(b);
        SeedList.add(b);
        SeedList.add(b);
        SeedList.add(b);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.controlPanel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GamePanelAdapter mAdapter = new GamePanelAdapter(SeedList);
        recyclerView.setAdapter(mAdapter);

        // Need bitmaps

//        Button finishTurnButton = findViewById(R.id.game_container_finish_turn);
//        finishTurnButton.setOnClickListener(new SurfaceView.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                waitForDebugger();
//                // TODO: This is for testing, will need to receive ENEMY or PLAYER for each round.
//                surface.pause();
//                surface.completeTurn(CellType.PLAYER);
//                surface.resume();
//            }
//        });
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
