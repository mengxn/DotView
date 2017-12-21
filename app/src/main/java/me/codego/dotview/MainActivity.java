package me.codego.dotview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.codego.view.DotLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DotLayout dotLayout = (DotLayout) findViewById(R.id.dot_layout);
        dotLayout.setNumber(67);
        dotLayout.show(true);

        final DotLayout dotLayout2 = (DotLayout) findViewById(R.id.dot_layout_2);
        dotLayout2.show(true);

        final DotLayout dotLayout3 = (DotLayout) findViewById(R.id.dot_layout_3);
        dotLayout3.setNumber(35);
        dotLayout3.show(true);
    }
}
