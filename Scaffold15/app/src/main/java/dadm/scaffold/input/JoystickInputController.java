package dadm.scaffold.input;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import dadm.scaffold.R;

public class JoystickInputController extends InputController {

    private int outerCircleCenterPositionX;
    private int outerCircleCenterPositionY;
    private int innerCircleCenterPositionX;
    private int innerCircleCenterPositionY;

    private int outerCircleRadius;
    private int innerCircleRadius;

    private Paint outerCirclePaint;
    private Paint innerCirclePaint;

    private Canvas canvas;

    private float startingPositionX;
    private float startingPositionY;

    private final double maxDistance;

    private TextView joystickCenter;

    public JoystickInputController(View view) {
        view.findViewById(R.id.joystick_main).setOnTouchListener(new JoystickTouchListener());
        view.findViewById(R.id.joystick_touch).setOnTouchListener(new FireButtonTouchListener());

        joystickCenter = view.findViewById(R.id.joystickCenter);

        int viewHeight = view.getHeight();
        int viewWidth = view.getWidth();

        double pixelFactor = viewHeight / 400d;
        maxDistance = 50*pixelFactor;

        //Círculo exterior
        outerCircleCenterPositionX = viewWidth - 100;
        outerCircleCenterPositionY = viewHeight - 100;
        outerCircleRadius = 80;

        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL);

        //Círculo interior
        innerCircleCenterPositionX = viewWidth - 100;
        innerCircleCenterPositionY = viewHeight - 100;
        innerCircleRadius = 40;

        innerCirclePaint = new Paint();
        innerCirclePaint.setColor(Color.BLUE);
        innerCirclePaint.setStyle(Paint.Style.FILL);

        canvas = new Canvas();
    }

    private class JoystickTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                startingPositionX = event.getX(0);
                startingPositionY = event.getY(0);
                draw(canvas);
            }
            else if (action == MotionEvent.ACTION_UP) {
                horizontalFactor = 0;
                verticalFactor = 0;
            }
            else if (action == MotionEvent.ACTION_MOVE) {
                // Get the proportion to the max
                horizontalFactor = (event.getX(0) - startingPositionX) / maxDistance;
                if (horizontalFactor > 1) {
                    horizontalFactor = 1;
                }
                else if (horizontalFactor < -1) {
                    horizontalFactor = -1;
                }
                verticalFactor = (event.getY(0) - startingPositionY) / maxDistance;
                if (verticalFactor > 1) {
                    verticalFactor = 1;
                }
                else if (verticalFactor < -1) {
                    verticalFactor = -1;
                }
            }
            return true;
        }
    }

    private class FireButtonTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getActionMasked();
            if (action == MotionEvent.ACTION_DOWN) {
                isFiring = true;
            }
            else if (action == MotionEvent.ACTION_UP) {
                isFiring = false;
            }
            return true;
        }
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(
                outerCircleCenterPositionX,
                outerCircleCenterPositionY,
                outerCircleRadius,
                outerCirclePaint
        );

        canvas.drawCircle(
                innerCircleCenterPositionX,
                innerCircleCenterPositionY,
                innerCircleRadius,
                innerCirclePaint
        );
    }
}

