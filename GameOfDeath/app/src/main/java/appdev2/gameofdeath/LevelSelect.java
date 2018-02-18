package appdev2.gameofdeath;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LevelSelect extends AppCompatActivity {
    private final String m_levelString = "Level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_select);

        Button Level1Button = (Button)findViewById(R.id.level_select_level_1_button);
        Level1Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                // Passes in level arguments to the new activity
                Bundle args = new Bundle();
                args.putInt(m_levelString, 1);
                i.putExtras(args);

                startActivity(i);
            }
        });
    }
}
