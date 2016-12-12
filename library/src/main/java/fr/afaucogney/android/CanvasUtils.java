package fr.afaucogney.android;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

/**
 * Created by afaucogney on 12/12/2016.
 */

public class CanvasUtils {

    public static void drawHorizontalLine(Canvas canvas, int height, int width, int color) {
        Rect r = new Rect(0, 0, width, height);
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(color);

        canvas.drawRect(r, p);
    }


    public static void drawGradiantRectangle(Canvas canvas, Rect rect, int color1, int color2) {
        LinearGradient gradient = new LinearGradient(0, rect.top, 0, rect.bottom, color1, color2, Shader.TileMode.CLAMP);
        Paint p = new Paint();
        p.setDither(true);
        p.setShader(gradient);
        canvas.drawRect(rect, p);

    }
}
