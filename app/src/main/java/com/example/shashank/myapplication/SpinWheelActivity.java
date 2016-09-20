package com.example.shashank.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.security.Policy;



public class SpinWheelActivity extends Activity {
    private void rotateDialer(float degrees) {
        matrix.postRotate(degrees, dialerWidth/2, dialerHeight/2);
        dialer.setImageMatrix(matrix);
    }


    private static Bitmap imageOriginal, imageScaled;
    private static Matrix matrix;
    private GestureDetector detector;
    private ImageView dialer;
    private int dialerHeight, dialerWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);
        detector = new GestureDetector(this, new MyGestureDetector());

        dialer = (ImageView) findViewById(R.id.imageView_chart);
        dialer.setOnTouchListener(new MyOnTouchListener());

        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.ath);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }


        dialer = (ImageView) findViewById(R.id.imageView_chart);
        dialer.setOnTouchListener(new MyOnTouchListener());
        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                // method called more than once, but the values only need to be initialized one time
                if (dialerHeight == 0 || dialerWidth == 0) {
                    dialerHeight = dialer.getHeight();
                    dialerWidth = dialer.getWidth();

                    // resize
                    Matrix resize = new Matrix();
                    //resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);



                    dialer.setImageBitmap(imageScaled);
                    dialer.setImageMatrix(matrix);
                    //Translate to center
                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);
                }
            }
        });

    }




    private class MyOnTouchListener implements View.OnTouchListener {

        private double startAngle;

        private double getAngle(float xTouch, float yTouch) {
            double x = xTouch - (dialerWidth / 2d);
            double y = dialerHeight - yTouch - (dialerHeight / 2d);

            switch (getQuadrant(x, y)) {
                case 1:
                    return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 2:
                    return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                case 3:
                    return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
                case 4:
                    return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
                default:
                    return 0;
            }
        }
        private int getQuadrant(double x, double y) {
            if (x >= 0) {
                return y >= 0 ? 1 : 4;
            } else {
                return y >= 0 ? 2 : 3;
            }
        }


        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;

                case MotionEvent.ACTION_UP:

                    break;
            }

            detector.onTouchEvent(event);

            return true;
        }
    }
    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            dialer.post(new FlingRunnable(velocityX + velocityY));
            return true;
        }
    }private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) > 5) {
                rotateDialer(velocity / 75);
                velocity /= 1.0666F;

                // post this instance again
                dialer.post(this);
            }
        }




        public void onGlobalLayout() {
            // method called more than once, but the values only need to be initialized one time
            if (dialerHeight == 0 || dialerWidth == 0) {
                dialerHeight = dialer.getHeight();
                dialerWidth = dialer.getWidth();

                // resize
                Matrix resize = new Matrix();
                resize.postScale((float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getWidth(), (float)Math.min(dialerWidth, dialerHeight) / (float)imageOriginal.getHeight());
                imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);

                // translate to the image vieanimation has to stop if you touch the dialer while the animation is running. For this it is only necessary to add a booleanw's center
                float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
                float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
                matrix.postTranslate(translateX, translateY);

                dialer.setImageBitmap(imageScaled);
                dialer.setImageMatrix(matrix);
            }
        }


        /**
         * @return The selected quadrant.
         */

    }private int getQuadrant(double x, double y) {
            if (x >= 0) {
                return y >= 0 ? 1 : 4;
            } else {
                return y >= 0 ? 2 : 3;
            }
        }

}
