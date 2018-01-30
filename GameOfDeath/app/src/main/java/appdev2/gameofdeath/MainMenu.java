package appdev2.gameofdeath;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button beginButton = (Button)findViewById(R.id.main_menu_begin_button);
        beginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LevelSelect.class);
                startActivity(i);
            }
        });

        Button exitButton = (Button)findViewById(R.id.main_menu_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Doesn't really exit the application, but android doesn't
                // allow for programmatically closing a program, just
                // suspending to memory.
                moveTaskToBack(true);
            }
        });

        Button creditButton = (Button)findViewById(R.id.main_menu_credit_button);
        creditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CreditActivity.class);
                startActivity(i);
            }
        });
    }
}
