package com.ess.essandroidbaselibrary.util.coreUtilities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import java.io.ByteArrayOutputStream;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 9/27/2018.
 */

public class ImageUtilities
{
    /**
     * imageView.setImageDrawable(result);
     *
     * @param image
     * @return
     */
    public static RoundedBitmapDrawable getRoundedStrokedBitmap(Activity activity, Bitmap image, int borderColor)
    {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int borderWidthHalf = 10;//10pxl
        int imageSquareWidth = Math.min(imageHeight, imageWidth);
        int newImageSquareWidth = imageSquareWidth + borderWidthHalf;
        int imageRadius = imageSquareWidth / 2;

        int x = borderWidthHalf + imageSquareWidth - imageWidth;
        int y = borderWidthHalf + imageSquareWidth - imageHeight;

        Bitmap roundedBitmap = Bitmap.createBitmap(newImageSquareWidth, newImageSquareWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);

        canvas.drawBitmap(image, x, y, null);

        // Draw border
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidthHalf * 2);
        borderPaint.setColor(borderColor);

        canvas.drawCircle(canvas.getWidth()/2, canvas.getWidth()/2, newImageSquareWidth/2, borderPaint);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), roundedBitmap);
        roundedBitmapDrawable.setCornerRadius(imageRadius);
        roundedBitmapDrawable.setAntiAlias(true);

        return roundedBitmapDrawable;
    }

    public static RoundedBitmapDrawable getRoundedBitmap(Activity activity, Bitmap image)
    {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int imageSquareWidth = Math.min(imageHeight, imageWidth);
        int imageRadius = imageSquareWidth / 2;

        int x = imageSquareWidth - imageWidth;
        int y = imageSquareWidth - imageHeight;

        Bitmap squareBitmap = Bitmap.createBitmap(imageSquareWidth, imageSquareWidth, Bitmap.Config.ARGB_8888);

        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(activity.getResources(), squareBitmap);
        roundedBitmapDrawable.setCornerRadius(imageRadius);
        roundedBitmapDrawable.setAntiAlias(true);

        return roundedBitmapDrawable;
    }

    public static byte[] getByteArrayOfJPEGImage(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }
}
