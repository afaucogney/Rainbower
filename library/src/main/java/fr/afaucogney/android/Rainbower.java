package fr.afaucogney.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by afaucogney on 09/12/2016.
 */

public class Rainbower {

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // BUILDER
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Builder {
        Context context;
        int min, max;
        int maskResource = -1;
        Integer width, height;
        int lowLevelColor = RED, highLevelColor = MAGENTA;
        boolean maskAtScaled = true;
        int[] dataSet = null;

        public Rainbower build(Context context) {
            this.context = context;
            if (min < 0 || max <= min) {
                throw new IllegalArgumentException("Rainbower Min and Max must be positive, and max greater than min");
            }
            Rainbower rainbower = new Rainbower(context, min, max);
            if (dataSet == null) {
                throw new IllegalArgumentException("Rainbower must have a valid dataset");
            }
            rainbower.maskResource = maskResource;
            rainbower.width = width;
            rainbower.height = height;
            rainbower.lowerColor = lowLevelColor;
            rainbower.higherColor = highLevelColor;
            rainbower.maskAtScaled = maskAtScaled;
            rainbower.dataSet = dataSet;

            return rainbower;

        }

        public Builder fromImageViewSize(ImageView iv) {
            this.width = iv.getWidth();
            this.height = iv.getHeight();
            return this;
        }

        public Builder fromSize(Point size) {
            this.width = size.x;
            this.height = size.y;
            return this;
        }

        public Builder setDataBounds(int min, int max) {
            this.min = min;
            this.max = max;
            return this;
        }

        public Builder setLowLevelColor(int lowLevelColor) {
            this.lowLevelColor = lowLevelColor;
            return this;
        }

        public Builder setHighLevelColor(int highLevelColor) {
            this.highLevelColor = highLevelColor;
            return this;
        }

        public Builder withMask(@DrawableRes int maskResourceId) {
            this.maskResource = maskResourceId;
            return this;
        }

        public Builder setMaskAtScale(boolean maskAtScale) {
            this.maskAtScaled = maskAtScale;
            return this;
        }

        public Builder bindWithDataSet(int[] dataSet) {
            this.dataSet = dataSet;
            return this;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONST
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final int RED = 0, YELLOW = 60, GREEN = 120, CYAN = 180, BLUE = 240, MAGENTA = 300;

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATA
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    final Context context;
    final int min;
    int max;
    int lowerColor = RED, higherColor = MAGENTA;
    Bitmap result;
    int maskResource;
    int width, height;
    int[] dataSet = null;
    boolean maskAtScaled = true;
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public Rainbower(Context context, int min, int max) {
        this.context = context.getApplicationContext();
        this.min = min;
        this.max = max;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // FACADE
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void prepareCanvas() {
        if (min < 0 || max <= 0) {
            throw new IllegalArgumentException("Min and Max values must be positives");
        }
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than min");
        }
        Canvas canvas = createEmptyCanvas(width, height);
    }

    private void prepareData() {
        if (dataSet == null) {
            throw new IllegalArgumentException("Rainbower need a dataset not null");
        }
        if (dataSet.length == 0) {
            throw new IllegalArgumentException("Rainbower need a dataset not empty");
        }

    }

    private Bitmap getRainbowBitmap() {
        return getRainbowBitmap(width, height, dataSet);
    }

    private Bitmap applyMaskIfPossible(Bitmap rainbow) {
        if (maskResource == -1) {
            throw new IllegalArgumentException("Masking need existing resource");
        }
        return getMaskedRainbowBitmap(rainbow);
    }

    private void make() {
        prepareCanvas();
        prepareData();
        Bitmap rainbow = getRainbowBitmap();
        try {
            result = applyMaskIfPossible(rainbow);

        } catch (IllegalArgumentException e) {
            result = rainbow;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // ERROR HELPER
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    private void checkValue(int value) {
        if (value < min) {
            throw new IllegalArgumentException("Value (" + value + ") must not be lesser than Min (" + min + ")");
        }
        if (value > max) {
            throw new IllegalArgumentException("Value (" + value + ") must not be greqter than Max (" + max + ")");
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // HELPER
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    private Canvas createEmptyCanvas(int width, int height) {
        // Create a bitmap for the part of the screen that needs updating.
        result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        result.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return new Canvas(result);
    }

    private Bitmap getRainbowBitmap(int width, int height, int[] values) {

        Bitmap rainbow = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        rainbow.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        int atomicHeight = height / values.length;

        Canvas canvas = new Canvas(rainbow);
        canvas.save();
        for (int i = 0; i < values.length; i++) {
            Log.v("value", String.valueOf(values[i]));

            checkValue(values[i]);
            // 1er ligne
            if (i == 0) {
                CanvasUtils.drawHorizontalLine(canvas, atomicHeight / 2, width, getColorFromValue(values[i], min, max));
                canvas.translate(0, atomicHeight / 2);
            }
            // in between lines
            else if (i <= values.length - 1) {
                Rect r = new Rect(0, 0, width, atomicHeight);
                CanvasUtils.drawGradiantRectangle(canvas, r, getColorFromValue(values[i - 1], min, max), getColorFromValue(values[i], min, max));
                canvas.translate(0, atomicHeight);
            }
            // last line
            if (i == values.length - 1) {
                CanvasUtils.drawHorizontalLine(canvas, atomicHeight / 2, width, getColorFromValue(values[i], min, max));
            }
        }
        canvas.restore();

        return rainbow;
    }


    private int getColorFromValue(int value, int min, int max) {
        float val = (float) value / (max - min) * higherColor + lowerColor;
        return Color.HSVToColor(new float[]{val, 1f, 1f});
    }


    private Bitmap getMaskedRainbowBitmap(@NonNull Bitmap rainbow) {
        if (maskResource == -1) {
            throw new IllegalArgumentException("Masking need existing resource");
        } else {

            Canvas canvas = createEmptyCanvas(width, height);
            Bitmap mask = getMaskBitmap(maskResource, maskAtScaled);

            //You can change original image here and draw anything you want to be masked on it.

            Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas tempCanvas = new Canvas(result);

            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            tempCanvas.drawBitmap(rainbow, 0, 0, null);
            tempCanvas.drawBitmap(mask, 0, 0, paint);
            paint.setXfermode(null);

            //Draw result after performing masking
            canvas.drawBitmap(result, 0, 0, new Paint());
        }
        return result;
    }

    private Bitmap getMaskBitmap(int maskBitmapResourcem, boolean atScale) {
        Bitmap rawMask = BitmapFactory.decodeResource(context.getResources(), maskBitmapResourcem);
        if (atScale) {
            return Bitmap.createScaledBitmap(rawMask, width, height, true);
        } else {
            return rawMask;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public void bindWithImageView(ImageView imageView) {
        make();
        imageView.setImageBitmap(result);
    }

}
