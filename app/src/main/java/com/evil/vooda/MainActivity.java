package com.evil.vooda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.evil.check.CheckOutTime;
import com.evil.check.VoodaActivity;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckOutTime.checkTime(20 * 1000L,new CheckOutTime.TimeOutCallback(){
            @Override
            public void method(boolean t){
                if(t){
                    startActivity(
                            new Intent(MainActivity.this,VoodaActivity.class));
                    finish();
                }
            }
        });
    }

    public void open(View view){
        startActivity(new Intent(this,VoodaActivity.class));
    }
}
