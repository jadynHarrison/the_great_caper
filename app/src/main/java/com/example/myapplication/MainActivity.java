package com.example.myapplication;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private BoardDragTouchListener objectDrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        int numPlayers = getIntent().getIntExtra("numPlayers", 2);

        // These must exist in your XML inside leftColumnLayout
        GridLayout boardGrid = findViewById(R.id.boardGrid);

        // TEMP DEBUG: show grid overlay
        boardGrid.setBackgroundColor(0x2200FF00);

        ViewGroup piecesLayer = findViewById(R.id.piecesLayer);


        ImageView boardImage = findViewById(R.id.gameBoardImageView);

        boardImage.post(() -> {
            if (boardImage.getDrawable() == null) return;

            // This matrix maps the drawable into the ImageView based on scaleType (fitCenter)
            android.graphics.Matrix m = boardImage.getImageMatrix();
            android.graphics.drawable.Drawable d = boardImage.getDrawable();

            android.graphics.RectF drawableRect = new android.graphics.RectF(
                    0, 0,
                    d.getIntrinsicWidth(),
                    d.getIntrinsicHeight()
            );

            android.graphics.RectF displayedRect = new android.graphics.RectF();
            m.mapRect(displayedRect, drawableRect);

            // displayedRect is in the ImageView's local coordinates
            int left = Math.round(displayedRect.left);
            int top = Math.round(displayedRect.top);
            int width = Math.round(displayedRect.width());
            int height = Math.round(displayedRect.height());

            // Apply rectangle to GridLayout
            FrameLayout.LayoutParams gridParams = (FrameLayout.LayoutParams) boardGrid.getLayoutParams();
            gridParams.width = width;
            gridParams.height = height;
            gridParams.leftMargin = left;
            gridParams.topMargin = top;
            boardGrid.setLayoutParams(gridParams);

            // Apply same rectangle to piecesLayer
            FrameLayout.LayoutParams pieceParams = (FrameLayout.LayoutParams) piecesLayer.getLayoutParams();
            pieceParams.width = width;
            pieceParams.height = height;
            pieceParams.leftMargin = left;
            pieceParams.topMargin = top;
            piecesLayer.setLayoutParams(pieceParams);
        });

        // one listener instance, used by all draggable pieces
        objectDrag = new BoardDragTouchListener(piecesLayer, boardGrid);

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

        // Attach drag listener to everything draggable (no copy/paste spam)
        View[] draggables = {
                camera1, camera2, camera3, camera4,
                p1, p2, p3, p4, p5, p6, p7, p8, p9,
                yellowPawn
        };

        for (View v : draggables) {
            v.setOnTouchListener(objectDrag);
        }
    }
}