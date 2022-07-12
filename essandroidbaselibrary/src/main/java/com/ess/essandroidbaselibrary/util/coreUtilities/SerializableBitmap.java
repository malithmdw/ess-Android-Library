package com.ess.essandroidbaselibrary.util.coreUtilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 12/3/2018.
 */

public class SerializableBitmap implements Serializable
{
    private static final long serialVersionUID = 100L;
    private Bitmap bitmap;

    public SerializableBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        if (bitmap == null)
        {
            // Mark the object is null.
            out.writeBoolean(false);
            return;
        }
        else
        {
            out.writeBoolean(true);
            byte[] opt = ImageUtilities.getByteArrayOfJPEGImage(bitmap);
            out.write(opt, 0, opt.length);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        if (in.readBoolean() == false)
        {
            // Check whether the object is null.
            bitmap = null;
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int b;
        while ((b = in.read()) != -1)
        {
            baos.write(b);
        }

        byte[] bitmapBytes = baos.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
    }
}
