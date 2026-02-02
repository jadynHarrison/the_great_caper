package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        int numPlayers = getIntent().getIntExtra("numPlayers", 2);

        ImageView camera1 = findViewById(R.id.oncamera1);
        ImageView camera2 = findViewById(R.id.oncamera2);
        ImageView camera3 = findViewById(R.id.oncamera3);
        ImageView camera4 = findViewById(R.id.oncamera4);

//        camera1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Code that runs when the image is clicked
//                Toast.makeText(MainActivity.this, "Image clicked!", Toast.LENGTH_SHORT).show();
//            }
//        });

        //ImageView pawn = findViewById(R.id.camera1);

        //ViewGroup cameraOff2 = findViewById(R.id.cameraOffAndOn2);
        //ViewGroup MainBoard = findViewById(R.id.leftColumnLayout);

        camera1.setOnTouchListener(cameraDrag);


        camera2.setOnTouchListener(cameraDrag);


        camera3.setOnTouchListener(cameraDrag);

        camera4.setOnTouchListener(cameraDrag);


//        camera1.setOnTouchListener(new View.OnTouchListener() {
//            float dX, dY;
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//
//                    case MotionEvent.ACTION_DOWN:
//                        // Store the difference between view position and touch position
//                        dX = view.getX() - event.getRawX();
//                        dY = view.getY() - event.getRawY();
//                        return true;
//
//                    case MotionEvent.ACTION_MOVE:
//                        // Update the view’s position
//                        view.animate()
//                                .x(event.getRawX() + dX)
//                                .y(event.getRawY() + dY)
//                                .setDuration(0)
//                                .start();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        // Check if dropped inside layoutB
//                        if (isInsideMainBoard(view, MainBoard)) {
//
//                            // 1. Get target parent’s screen position BEFORE reparenting
//                            int[] parentLoc = new int[2];
//                            MainBoard.getLocationOnScreen(parentLoc);
//
//                            // 2. Compute new coordinates BEFORE removing the view
//                            float newX = event.getRawX() - parentLoc[0] + dX;
//                            float newY = event.getRawY() - parentLoc[1] + dY;
//
//                            // 3. Clamp to keep inside parent
//                            newX = Math.max(0, Math.min(newX, MainBoard.getWidth() - view.getWidth()));
//                            newY = Math.max(0, Math.min(newY, MainBoard.getHeight() - view.getHeight()));
//
//                            // Remove from old parent
//
//                            ViewGroup oldParent = (ViewGroup) view.getParent();
//                            oldParent.removeView(view);
//
//                            // Add to new parent
//                            MainBoard.addView(view);
//
//                            view.setX(newX);
//                            view.setY(newY);
//                            view.bringToFront();
//
//
//                            Log.d("DEBUG", "Parent after drop = " + ((ViewGroup)view.getParent()).getId());
//                        }
//
//                        return true;
//
//
//                    default:
//                        return false;
//                }
//            }
//        });

        }

        View.OnTouchListener cameraDrag = new  View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ViewGroup MainBoard = findViewById(R.id.leftColumnLayout);

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        // Store original parent only once
                        if (view.getTag(R.id.leftColumnLayout) == null) {
                            view.setTag(R.id.leftColumnLayout, view.getParent());
                        }

                        // Store original window coordinates only once
                        if (view.getTag(R.id.leftColumnLayout) == null) {
                            int[] loc = new int[2];
                            view.getLocationInWindow(loc);
                            view.setTag(R.id.leftColumnLayout, loc[0]);
                            view.setTag(R.id.leftColumnLayout, loc[1]);
                        }

                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();

                        Log.d("DEBUG", "Reset X = " + dX);
                        Log.d("DEBUG", "Reset Y = " + dY);

                        return true;

                    case MotionEvent.ACTION_MOVE:

                        view.setX(event.getRawX() + dX);
                        view.setY(event.getRawY() + dY);

                        return true;
                    case MotionEvent.ACTION_UP:
                        // Check if dropped inside layoutB
                        if (isInsideMainBoard(view, MainBoard)) {

                            // 1. Get target parent’s screen position BEFORE reparenting
                            int[] parentLoc = new int[2];
                            MainBoard.getLocationOnScreen(parentLoc);

                            // 2. Compute new coordinates BEFORE removing the view
                            float newX = event.getRawX() - parentLoc[0] ;//+ dX;
                            float newY = event.getRawY() - parentLoc[1] ;//+ dY;

                            // 3. Clamp to keep inside parent
                            newX = Math.max(50, Math.min(newX, MainBoard.getWidth() - view.getWidth()));
                            newY = Math.max(50, Math.min(newY, MainBoard.getHeight() - view.getHeight()));

                            // Remove from old parent
                            Log.d("DEBUG", "Parent before drop = " + ((ViewGroup) view.getParent()).getId());
                            ViewGroup oldParent = (ViewGroup) view.getParent();
                            oldParent.removeView(view);

                            // Add to new parent
                            MainBoard.addView(view);

                            view.setX(newX);
                            view.setY(newY);
                            view.bringToFront();

                            Log.d("DEBUG", "Parent after drop = " + ((ViewGroup) view.getParent()).getId());
                            Log.d("DEBUG", "New X = " + newX);
                            Log.d("DEBUG", "New Y = " + newY);
                        }
//                        else
//                        {
//                            float[] originalV = (float[]) view.getTag();
//
//                            int[] parentLocReset = new int[2];
//                            ((ViewGroup)view.getParent()).getLocationInWindow(parentLocReset);
//
//                            float resetXRst = originalV[0] - parentLocReset[0];
//                            float resetYRst = originalV[1] - parentLocReset[1];
//
//                            view.setX(resetXRst);
//                            view.setY(resetYRst);
//
//                            Log.d("DEBUG", "Reset X = " + resetXRst);
//                            Log.d("DEBUG", "Reset Y = " + resetYRst);
//
//                        }

                        return true;

                    default:
                        return false;
                }
            }
        };
    private boolean isInsideMainBoard(View child, ViewGroup parent) {
        int[] parentLoc = new int[2];
        int[] childLoc = new int[2];

        parent.getLocationInWindow(parentLoc);
        child.getLocationInWindow(childLoc);

        return childLoc[0] >= parentLoc[0] &&
                childLoc[1] >= parentLoc[1] &&
                childLoc[0] + child.getWidth() <= parentLoc[0] + parent.getWidth() &&
                childLoc[1] + child.getHeight() <= parentLoc[1] + parent.getHeight();
    }
    }


