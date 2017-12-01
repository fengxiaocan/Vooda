package com.evil.check;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * @项目名： xiaofang
 * @包名： com.xiaofang.controlsystem.ui.activity
 * @创建者: Noah.冯
 * @时间: 9:53
 * @描述： vooda
 */

public class VoodaActivity extends Activity
        implements View.OnClickListener, OutTimeDialog.CheckCodeListener{

    private Button        mBtInput;
    private OutTimeDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vooda);
        initView();
    }

    private void initView(){
        mBtInput = (Button) findViewById(R.id.bt_input);
        mBtInput.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.bt_input){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                PackageManager pm = getPackageManager();
                boolean permission = (PackageManager.PERMISSION_GRANTED == pm
                        .checkPermission(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                getPackageName()));
                if(permission){
                    showDialog();
                }else{
                    requestPermissions(
                            new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE},
                            0x8231);
                }
            }else{
                showDialog();
            }
        }
    }

    private void showDialog(){
        if(mDialog == null){
            mDialog = new OutTimeDialog(this);
            mDialog.setCheckCodeListener(this);
        }
        mDialog.show();
    }

    @Override
    public void error(String error){
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void success(){
        Toast.makeText(this,"内测码成功，请重启APP",Toast.LENGTH_SHORT).show();
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
