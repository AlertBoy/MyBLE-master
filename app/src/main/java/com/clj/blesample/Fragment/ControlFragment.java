package com.clj.blesample.Fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.clj.blesample.ControlActivity;
import com.clj.blesample.R;
import com.clj.blesample.View.PaintBoard;
import com.clj.blesample.comm.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ControlFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private Button btn_left_up;
    private Button btn_right_up;
    private Button btn_right_down;
    private ImageView image_main;
    private PaintBoard paintBoard_right;
    private Matrix matrix = new Matrix();
    private ScheduledExecutorService scheduledExecutor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_control, null);
        initView(v);
        return v;
    }

    private View initView(View v) {
        image_main = (getActivity()).findViewById(R.id.image_main);
        paintBoard_right = (getActivity()).findViewById(R.id.img_right);


        btn_left_up = v.findViewById(R.id.btn_left_up);
        btn_right_up = v.findViewById(R.id.btn_right_up);
        btn_right_down = v.findViewById(R.id.btn_right_down);
        btn_right_down.setOnTouchListener(this);
        btn_left_up.setOnClickListener(this);
        btn_left_up.setOnTouchListener(this);
        btn_right_up.setOnTouchListener(this);
        return v;
    }

    int progress = 0;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_up:

                Utils.Toast(getContext(), "hello");
                break;
            case R.id.btn_left_down:

                break;
            case R.id.btn_mid_up:

                break;
            case R.id.btn_mid_down:

                break;
            case R.id.btn_right_up:

                break;
            case R.id.btn_right_down:

                break;

        }
    }

    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.btn_left_up:
                Bitmap bitmap = ((BitmapDrawable) (getResources()
                        .getDrawable(R.mipmap.logo)))
                        .getBitmap();

                // 设置图片旋转的角度
                matrix.setRotate(progress += 10);
                bitmap = Bitmap.createBitmap(
                        bitmap,
                        0,
                        0,
                        bitmap.getWidth(),
                        bitmap.getHeight(),
                        matrix,
                        true);
                image_main.setImageBitmap(bitmap);
                break;

        }
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            updateAddOrSubtract(view.getId());    //手指按下时触发不停的发送消息
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            stopAddOrSubtract();    //手指抬起时停止发送
        }
        return true;

    }

    private void updateAddOrSubtract(int viewId) {
        final int vid = viewId;
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = vid;
                handler.sendMessage(msg);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);    //每间隔100ms发送Message
    }

    private void stopAddOrSubtract() {
        if (scheduledExecutor != null) {
            scheduledExecutor.shutdownNow();
            scheduledExecutor = null;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int viewId = msg.what;
            switch (viewId) {
                case R.id.btn_left_up:
                    Bitmap bitmap = ((BitmapDrawable) (getResources()
                            .getDrawable(R.mipmap.logo1)))
                            .getBitmap();


                    matrix.setRotate(progress += 10);
                    bitmap = Bitmap.createBitmap(
                            bitmap,
                            0,
                            0,
                            bitmap.getWidth(),
                            bitmap.getHeight(),
                            matrix,
                            true);
                    image_main.setImageBitmap(bitmap);
                    break;
                case R.id.btn_right_up:
                    paintBoard_right.SetDegreen(progress += 5);
                    break;
                case R.id.btn_right_down:
                    paintBoard_right.SetDegreen(progress -= 5);
                    break;
            }
        }
    };


}
