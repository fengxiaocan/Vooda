package com.evil.check;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.evil.check.CheckOutTime.sCreateTimeName;
import static com.evil.check.CheckOutTime.sNewOutTimeName;

/**
 * @name： Vooda
 * @package： com.evil.check
 * @author: Noah.冯 QQ:1066537317
 * @time: 11:10
 * @version: 1.1
 * @desc： TODO
 */

class OutTimeDialog extends Dialog{

    private EditText          mEditText;
    private CheckCodeListener mCheckCodeListener;


    public OutTimeDialog(@NonNull Context context){
        super(context);
        init();
    }

    public OutTimeDialog(@NonNull Context context,int themeResId){
        super(context,themeResId);
        init();
    }

    protected OutTimeDialog(@NonNull Context context,boolean cancelable,
                            @Nullable OnCancelListener cancelListener){
        super(context,cancelable,cancelListener);
        init();
    }

    public void setCheckCodeListener(CheckCodeListener checkCodeListener){
        mCheckCodeListener = checkCodeListener;
    }

    private void init(){
        View view = View.inflate(getContext(),R.layout.dialog_out_time,null);
        setContentView(view);

        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager wm = window.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();

        //一定要在setContentView之后调用，否则无效
        window.setLayout((int) (width - width * 0.15),
                         ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        mEditText = (EditText) view.findViewById(R.id.et_input);
        Button bt = (Button) view.findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String trim = mEditText.getText().toString().trim();
                if(trim == null || "".equals(trim)){
                    Toast.makeText(getContext(),"请输入内测码",Toast.LENGTH_SHORT)
                         .show();
                }else{
                    try{
                        String publickey = getContext().getResources()
                                                       .getString(
                                                               R.string.publickey);
                        String time = KeyStoreUtils
                                .decryptByPublicKey(trim,publickey);

                        String[] split = time.split("&%&");
                        //创建时间
                        String packageName = split[0];
                        if(!packageName.equals(getContext().getPackageName())){
                            mCheckCodeListener.error("非法的内测码");
                            dismiss();
                            mEditText.setText("");
                            return;
                        }
                        long createTime = Long.valueOf(split[1]);
                        //校验过期时间
                        long outTime = Long.valueOf(split[2]);
                        //最新时间
                        long newTime = Long.valueOf(split[3]);

                        long code = Long.valueOf(split[4]);

                        long l = createTime % 100 + outTime % 20;

                        if(l != code){
                            mCheckCodeListener.error("非法的内测码");
                            dismiss();
                        }else{
                            if(System.currentTimeMillis() > newTime){
                                mCheckCodeListener.error("内测码已过期");
                            }else{
                                SharedPreferences sp = getContext()
                                        .getSharedPreferences(
                                                CheckOutTime.sName,
                                                Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putLong(sCreateTimeName,createTime);
                                edit.putLong(sNewOutTimeName,newTime);
                                edit.commit();
                                mCheckCodeListener.success();
                            }
                            dismiss();
                        }
                    }catch(Exception e){
                        mCheckCodeListener.error("非法的内测码");
                        dismiss();
                    }
                }
                mEditText.setText("");
            }
        });
    }

    public interface CheckCodeListener{

        void error(String error);

        void success();
    }
}
