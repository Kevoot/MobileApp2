package appdev2.gameofdeath;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Kevin on 1/25/2018.
 */

public class Cell {
    public CellType type;

    public Cell() {
        this.type = CellType.DEAD;
    }

    public Paint getTypeColor() {
        Paint cellPaint = new Paint();
        cellPaint.setStrokeWidth(2);
        cellPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        switch(this.type) {
            case DEAD:
                cellPaint.setColor(Color.TRANSPARENT);
            case PLAYER:
                cellPaint.setColor(Color.GREEN);
            case ENEMY:
                cellPaint.setColor(Color.RED);
            case BLOCKED:
                cellPaint.setColor(Color.TRANSPARENT);
            default:
                cellPaint.setColor(Color.TRANSPARENT);
        }
        return cellPaint;
    }
}