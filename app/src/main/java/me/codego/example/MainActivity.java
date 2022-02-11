package me.codego.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import me.codego.view.DotLayout;
import me.codego.view.DotType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DotLayout dotLayout = (DotLayout) findViewById(R.id.dot_layout);
        dotLayout.setNumber(1);
        dotLayout.show(true);

        final DotLayout dotLayout1 = (DotLayout) findViewById(R.id.dot_layout_2);
        dotLayout1.show(true);

        final DotLayout dotLayout2 = (DotLayout) findViewById(R.id.dot_layout_3);
        dotLayout2.show(true, 1);

        final DotLayout dotLayout3 = (DotLayout) findViewById(R.id.dot_layout_4);
        dotLayout3.show(true, 67);

        final DotLayout dotLayout5 = (DotLayout) findViewById(R.id.dot_layout_5);
        dotLayout5.setNumber(100);
        dotLayout5.setDotType(DotType.PLUS);
        dotLayout5.show(true);

    }
}
