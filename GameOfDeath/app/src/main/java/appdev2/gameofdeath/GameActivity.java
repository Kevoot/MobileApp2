package appdev2.gameofdeath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kevin on 1/29/2018.
 */

public class GameActivity extends AppCompatActivity {
    private int m_levelNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_container);

        Bundle args = getIntent().getExtras();
        if(args != null) {
            m_levelNumber = args.getInt("Level");
        }
    }
}
