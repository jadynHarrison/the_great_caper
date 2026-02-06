package com.example.myapplication;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * External Citation
 * Date: 1 Feb 2026
 * Problem: could not get the image to drag and drop onto game board
 * Resource: gerardo esquivel
 * Solution: used the drag and drop pdf provided + copilot
 * to figure out the components needed for actions down, move, and up
 */
public class BoardDragTouchListener implements View.OnTouchListener {

    // handler = listener = responds to an object action (drag and drop)
    private float dX, dY;

    private final ViewGroup mainBoard;

    public BoardDragTouchListener(ViewGroup mainBoard) {
        this.mainBoard = mainBoard;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction()) {
            // action down = when you first press the image
            case MotionEvent.ACTION_DOWN:
                if (view.getTag(R.id.tag_original_parent) == null) {
                    view.setTag(R.id.tag_original_parent, view.getParent());
                }

                if (view.getTag(R.id.tag_original_x) == null || view.getTag(R.id.tag_original_y) == null) {
                    view.setTag(R.id.tag_original_x, view.getX());
                    view.setTag(R.id.tag_original_y, view.getY());
                }

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();
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

                if (isInsideMainBoard(event, mainBoard)) {
                    int[] parentLoc = new int[2];
                    mainBoard.getLocationOnScreen(parentLoc);

                    float newX = event.getRawX() - parentLoc[0];
                    float newY = event.getRawY() - parentLoc[1];

                    newX = Math.max(0, Math.min(newX, mainBoard.getWidth() - view.getWidth()));
                    newY = Math.max(0, Math.min(newY, mainBoard.getHeight() - view.getHeight()));

                    ViewGroup oldParent = (ViewGroup) view.getParent();
                    oldParent.removeView(view);

                    mainBoard.addView(view);

                    view.setX(newX);
                    view.setY(newY);
                    view.bringToFront();
                }
                // this else and if statement will snap the pieces back when dropped outside board
                else {
                    ViewGroup originalParent = (ViewGroup) view.getTag(R.id.tag_original_parent);
                    Float originalX = (Float) view.getTag(R.id.tag_original_x);
                    Float originalY = (Float) view.getTag(R.id.tag_original_y);

                    if (originalParent != null && originalX != null && originalY != null) {
                        ViewGroup currentParent = (ViewGroup) view.getParent();
                        currentParent.removeView(view);
                        originalParent.addView(view);

                        view.setX(originalX);
                        view.setY(originalY);
                        view.bringToFront();
                    }
                }

                // Special-case rule for pawn drop: convert to dot and resize
                if (view.getId() == R.id.yellow_Pawn) {
                    ((ImageView) view).setImageResource(R.drawable.yellow_dot);
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

    // helper method = checks whether the image is inside the game board's bounds
    private boolean isInsideMainBoard(MotionEvent event, ViewGroup parent) {
        int[] parentLoc = new int[2];
        parent.getLocationOnScreen(parentLoc);

        float x = event.getRawX();
        float y = event.getRawY();

        return x >= parentLoc[0] &&
                x <= parentLoc[0] + parent.getWidth() &&
                y >= parentLoc[1] &&
                y <= parentLoc[1] + parent.getHeight();
    }
}