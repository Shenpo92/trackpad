package com.moodelizer.trackpad;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements OnTouchListener {
  private float maxWidth;
  private float maxHeight;
  private int[] coordinate = new int[2];

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.point_layout);

    final RelativeLayout board = findViewById(R.id.board);
    ViewTreeObserver vto = board.getViewTreeObserver(); // Listener for changes in the board
    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        maxHeight = board.getHeight(); // Get the maximum height of the trackpad area
        maxWidth = board.getWidth(); // Get the maximum width of the trackpad area
        board.getLocationOnScreen(coordinate); // Get the on screen coordinate of X and Y of top left corner of the trackpad [0]: X & [1]: Y
        ViewTreeObserver obs = board.getViewTreeObserver();
        obs.removeOnGlobalLayoutListener(this);
      }
    });
    View image = findViewById(R.id.black_round);
    image.setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View draggableView, MotionEvent event) {
    View xAxis = findViewById(R.id.x_axis);
    View yAxis = findViewById(R.id.y_axis);
    TextView xCoord = findViewById(R.id.x_coord);
    TextView yCoord = findViewById(R.id.y_coord);
    int cCenter = draggableView.getHeight() / 2; // Distance between the side of the draggable view and its center


    switch (event.getAction()) {
      case MotionEvent.ACTION_MOVE:
        // Check if the draggable view is between the trackpad's maximum height and minimum height
        if ((event.getRawY() > coordinate[1] + cCenter) && (event.getRawY() < coordinate[1] + maxHeight - cCenter)) {
          draggableView.setY(event.getRawY() - coordinate[1] - cCenter);
          xAxis.setY((event.getRawY() - (coordinate[1] + maxHeight / 2)));

          // - Take the draggable view's actual Y coordinate and add cCircle to get the coordinate from the center of the view.
          // - Divide it by 1000 to get a coordinate between 0 and 1.
          // - Round it to 2 decimal places.
          double roundedY = Math.round((((draggableView.getY() + cCenter) / 1000) * 100.0)) / 100.0;
          String yCoordText = getString(R.string.Y) + Double.toString(roundedY);
          yCoord.setText(yCoordText);
        }
        if ((event.getRawX() > coordinate[0] + cCenter) && (event.getRawX() < coordinate[0] + maxWidth - cCenter)) {
          draggableView.setX(event.getRawX() - coordinate[0] - cCenter);
          yAxis.setX((event.getRawX() - (coordinate[0] + maxWidth / 2)));

          // - Take the draggable view's actual X coordinate and add cCircle to get the coordinate from the center of the view.
          // - Divide it by 1000 to get a coordinate between 0 and 1.
          // - Round it to 2 decimal places.
          double roundedX = Math.round(((double) ((draggableView.getX() + cCenter) / 1000)) * 100.0) / 100.0;
          String xCoordText = getString(R.string.X) + Double.toString(roundedX);
          xCoord.setText(xCoordText);
        }
        break;
    }
    return true;
  }
}

