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
import static appdev2.gameofdeath.CellGridSurface.whichBox;

public class GameActivity extends AppCompatActivity {
    private int m_levelNumber;
    private CellGridSurface surface;
    int steps = 5;
    public static boolean boolPaste = false;
    public static RecyclerView recyclerView;

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
        surface.setOnTouchListener(CellGridSurface.mPaintHandler);
        Button finishTurnButton = findViewById(R.id.game_container_finish_turn);
        finishTurnButton.setOnClickListener(new SurfaceView.OnClickListener() {

            @Override
            public void onClick(View v) {
                //waitForDebugger();
                // TODO: This is for testing, will need to receive ENEMY or PLAYER for each round.
                //surface.pause();
                surface.completeTurn(CellType.PLAYER, steps);
            }
        });

        // Need some bitmaps

        //temp bitmap
        Bitmap b = Bitmap.createBitmap(200, 200,  Bitmap.Config.ARGB_8888);
        b.eraseColor(Color.RED);


        Bitmap c = Bitmap.createBitmap(200, 200,  Bitmap.Config.ARGB_8888);
        c.eraseColor(Color.CYAN);

        Bitmap d = Bitmap.createBitmap(200, 200,  Bitmap.Config.ARGB_8888);
        d.eraseColor(Color.GREEN);

        Bitmap e = Bitmap.createBitmap(200, 200,  Bitmap.Config.ARGB_8888);
        e.eraseColor(Color.BLUE);


        //
        List<Bitmap> SeedList = new ArrayList<>();
        SeedList.add(b);
        SeedList.add(c);
        SeedList.add(d);
        SeedList.add(e);
        SeedList.add(b);
        recyclerView = (RecyclerView) findViewById(R.id.controlPanel);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final GamePanelAdapter mAdapter = new GamePanelAdapter(SeedList, new ClickListener() {
            @Override
            public void onPositionClicked(int position) {
                if(!boolPaste){
                    surface.setOnTouchListener(CellGridSurface.mPasteHandler);
                    whichBox = "Box " + Integer.toString(position);
                    boolPaste = true;
                } else {
                    surface.setOnTouchListener(CellGridSurface.mPaintHandler);
                    whichBox = "";
                    boolPaste = false;
                }
                recyclerView.performClick();

            }

            @Override
            public void onLongClicked(int position) {

            }
        });
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


    public static void ClickRecycler(){
        recyclerView.findViewHolderForAdapterPosition(0).itemView.performClick();
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
