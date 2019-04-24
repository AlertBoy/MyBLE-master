package com.clj.blesample.Fragment;

import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.ImageView;

import com.clj.blesample.R;
import com.clj.blesample.View.LeftBoard;
import com.clj.blesample.View.PaintBoard;
import com.clj.blesample.comm.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ControlFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {
    private boolean isTouched = false;

    private int degree_left = 0;
    private int degree_right = 0;
    private int height_mid = 0;


    private Button btn_left_up;
    private Button btn_left_down;

    private Button btn_mid_up;
    private Button btn_mid_down;


    private Button btn_right_up;
    private Button btn_right_down;


    private LeftBoard bar_left;
    private PaintBoard bar_right;
    private ImageView bar_mid;

    private Bitmap bitmap_left;
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
        prepareResource();
        return v;
    }

    private void prepareResource() {
        bitmap_left = ((BitmapDrawable) (getResources()
                .getDrawable(R.mipmap.logo2)))
                .getBitmap();
    }

    private View initView(View v) {
        bar_left = (getActivity()).findViewById(R.id.image_main);
        bar_right = (getActivity()).findViewById(R.id.img_right);
        bar_mid = (getActivity()).findViewById(R.id.image_mid);

        //left
        btn_left_up = v.findViewById(R.id.btn_left_up);
        btn_left_up.setOnTouchListener(this);

        btn_left_down = v.findViewById(R.id.btn_left_down);
        btn_left_down.setOnTouchListener(this);

        //mid
        btn_mid_up = v.findViewById(R.id.btn_mid_up);
        btn_mid_up.setOnTouchListener(this);

        btn_mid_down = v.findViewById(R.id.btn_mid_down);
        btn_mid_down.setOnTouchListener(this);

        //right
        btn_right_up = v.findViewById(R.id.btn_right_up);
        btn_right_up.setOnTouchListener(this);

        btn_right_down = v.findViewById(R.id.btn_right_down);
        btn_right_down.setOnTouchListener(this);


        return v;
    }


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


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isTouched)
                return false;
            updateAddOrSubtract(view.getId());    //手指按下时触发不停的发送消息
            isTouched = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            stopAddOrSubtract();    //手指抬起时停止发送
            isTouched = false;
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
                    setDegreeForLeft(true);
                    break;
                case R.id.btn_left_down:
                    setDegreeForLeft(false);
                    break;
                case R.id.btn_mid_up:
                    height_mid += 4;
                    setMidPosition();
                    break;
                case R.id.btn_mid_down:
                    height_mid -= 4;
                    setMidPosition();
                    break;
                case R.id.btn_right_up:
                    setDegreeForRight(true);
                    break;
                case R.id.btn_right_down:

                    setDegreeForRight(false);
                    break;
            }
        }

        private void setDegreeForLeft(boolean is_up) {

            if (is_up)
                degree_left += 4;
            else
                degree_left -= 4;
            if (degree_left > 60)
                degree_left = 60;
            if (degree_left < 0)
                degree_left = 0;
            bar_left.SetDegree(degree_left);
        }

        private void setDegreeForRight(boolean is_up) {

            if (is_up)
                degree_right -= 4;
            else
                degree_right += 4;
            if (degree_right > 0)
                degree_right = 0;
            if (degree_right < -60)
                degree_right = -60;
            bar_right.SetDegree(degree_right);
        }
    };

    //设置中间条高度
    private void setMidPosition() {
        if (height_mid < 0)
            return;
        bar_mid.setPadding(0, 0, 0, height_mid);
    }


}