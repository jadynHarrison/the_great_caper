package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //int paintCtr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        int numPlayers = getIntent().getIntExtra("numPlayers", 2);

        //paintCtr =0;

        // creating variable name(s) using the id name from cameras 1-4 [not the red ones]
        ImageView camera1 = findViewById(R.id.oncamera1);
        ImageView camera2 = findViewById(R.id.oncamera2);
        ImageView camera3 = findViewById(R.id.oncamera3);
        ImageView camera4 = findViewById(R.id.oncamera4);

        // creating variable names(s) using the id name from the paintings 1-9
        ImageView p1 = findViewById(R.id.painting1);
        ImageView p2 = findViewById(R.id.painting2);
        ImageView p3 = findViewById(R.id.painting3);
        ImageView p4 = findViewById(R.id.painting4);
        ImageView p5 = findViewById(R.id.painting5);
        ImageView p6 = findViewById(R.id.painting6);
        ImageView p7 = findViewById(R.id.painting7);
        ImageView p8 = findViewById(R.id.painting8);
        ImageView p9 = findViewById(R.id.painting9);

        //creating variable name for the yellow pawn
        ImageView yellowPawn = findViewById(R.id.yellow_Pawn);

//        yellow_Pawn.setOnClickListener(pawnOnClick);
//        yellow_Pawn.setOnTouchListener(cameraDrag);

        camera1.setOnTouchListener(objectDrag);
        camera2.setOnTouchListener(objectDrag);
        camera3.setOnTouchListener(objectDrag);
        camera4.setOnTouchListener(objectDrag);

        p1.setOnTouchListener(objectDrag);
        p2.setOnTouchListener(objectDrag);
        p3.setOnTouchListener(objectDrag);
        p4.setOnTouchListener(objectDrag);
        p5.setOnTouchListener(objectDrag);
        p6.setOnTouchListener(objectDrag);
        p7.setOnTouchListener(objectDrag);
        p8.setOnTouchListener(objectDrag);
        p9.setOnTouchListener(objectDrag);

        // allows for the yellow pawn to be dragged onto the board
        yellowPawn.setOnTouchListener(objectDrag);
    }
//    /**
//     * External Citation
//     * Date: 1 Feb 2026
//     * Problem: could not get the image to drag and drop onto game board
//     * Resource:gerardo esquivel
//     * Solution: used the drag and drop pdf provided + copilot
//     * to figure out the components needed for actions down, move, and up
       *
//     */

    View.OnTouchListener objectDrag = new  View.OnTouchListener() {
        // handler = listener = responds to an object action (drag and drop)
        float dX, dY;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            // calls for the layout with the board and the 9 paintings
            ViewGroup MainBoard = findViewById(R.id.leftColumnLayout);

            switch (event.getAction()) {
                // action down = when you first press the image
                case MotionEvent.ACTION_DOWN:
                    // Store original parent(layout) [where image originally was]
                    // get.parent = get the layout before
                    // [1st layout (grandpa), 2nd layout(parent), 3rd layout(child)]
                    if (view.getTag(R.id.leftColumnLayout) == null) {
                        view.setTag(R.id.leftColumnLayout, view.getParent());
                    }

                    // Store original window coordinates only once [looks at the entire screen]
                    //
                    if (view.getTag(R.id.leftColumnLayout) == null) {
                        // creates an array of 2 positions
                        // what stores the coordinates of the window
                        int[] loc = new int[2];
                        view.getLocationInWindow(loc);
                        view.setTag(R.id.leftColumnLayout, loc[0]);
                        view.setTag(R.id.leftColumnLayout, loc[1]);
                    }
                    // calculates the horizontal offset(distance between a and b)
                    // a = where the image initially is placed
                    // b = position at the moment you start touching the image
                    dX = view.getX() - event.getRawX();

                    // calculates the vertical offset of these 2 positions
                    dY = view.getY() - event.getRawY();

                    // how to debug
                    //Log.d("DEBUG", "Reset X = " + dX);
                    // Log.d("DEBUG", "Reset Y = " + dY);

                    return true;

                // event that actually moves the image
                case MotionEvent.ACTION_MOVE:

                    //getRawX() = fingers position on the entire screen
                    // updates the coordinates of the image = moves
                    view.setX(event.getRawX() + dX);
                    view.setY(event.getRawY() + dY);

                    return true;

                // event that finalizes what happens to the piece
                case MotionEvent.ACTION_UP:
                    // checks to see if image is inside the game board
                    if (isInsideMainBoard(view, MainBoard)) {

                        // 1. gets the coordinates of the parent screen BEFORE reparenting
                        int[] parentLoc = new int[2];
                        MainBoard.getLocationOnScreen(parentLoc);

                        // 2. computes new coordinates BEFORE removing the image
                        // [converts the screen coordinates into coordinates inside the game board]
                        float newX = event.getRawX() - parentLoc[0] ;
                        float newY = event.getRawY() - parentLoc[1] ;

                        // 3. forces the image to stay within a max. and min. range --> to keep inside parent
                        // image can't go outside game board
                        newX = Math.max(50, Math.min(newX, MainBoard.getWidth() - view.getWidth()));
                        newY = Math.max(50, Math.min(newY, MainBoard.getHeight() - view.getHeight()));

                        // removes from old parent (a.k.a orignal layout of image)
                        ViewGroup oldParent = (ViewGroup) view.getParent();
                        oldParent.removeView(view);

                        // adds to new parent (a.k.a the layout of where the image was placed)
                        // layout of game board
                        MainBoard.addView(view);

                        view.setX(newX);
                        view.setY(newY);
                        view.bringToFront();
                    }
                    // if this is the yellow pawn, change it to a dot
                    // keeps the game board less crowded
                    if (view.getId() == R.id.yellow_Pawn) {
                        ((ImageView)view).setImageResource(R.drawable.yellow_dot);

                        // shrinks it to dot size
                        ViewGroup.LayoutParams params = view.getLayoutParams();
                        params.width = 80;
                        params.height = 80;
                        view.setLayoutParams(params);
                    }
                    return true;

                default:
                    return false;
            }
        }
    };
    // helper method = checks whether the image is inside the game board's bounds
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

// this method changes the image yellow pawn --> green pawn when clicked [can be used for when the cameras are disactivated]
//    View.OnClickListener pawnOnClick = new View.OnClickListener()
//    {
//        @Override
//        public void onClick(View v)
//        {
//            ImageView pawn = findViewById(R.id.pawn);
//            pawn.setImageResource(R.drawable.cameraworking);
//
//            int currentImage = R.drawable.yellowpawn;
//            if(currentImage == R.drawable.yellowpawn) {
//                pawn.setImageResource(R.drawable.greenpawn);
//            }
//            else {pawn.setImageResource(R.drawable.yellowpawn); }
//
//            Toast.makeText(MainActivity.this, "Image clicked!", Toast.LENGTH_SHORT).show();
//        }
//    };
