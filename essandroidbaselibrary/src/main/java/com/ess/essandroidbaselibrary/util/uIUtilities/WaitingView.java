package com.ess.essandroidbaselibrary.util.uIUtilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.ess.essandroidbaselibrary.R;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 6/1/2018.
 */

public class WaitingView
{
    private static final WaitingView INSTANCE = new WaitingView();

    private Activity mActivity;
    private boolean isShow;
    private ProgressDialog progressDialog;

    public static WaitingView getInstance()
    {
        return INSTANCE;
    }

    private WaitingView()
    {
    }

    /**
     * @param activity
     * @return
     */
    public void showWaitingActivity(Activity activity)
    {
        if (activity != null)
        {
            this.mActivity = activity;
            this.isShow = true;

            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    progressDialog = ProgressDialog.show(mActivity, null, null, false, false);

                    try
                    {
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                    catch (NullPointerException ex)
                    {
                    }

                    progressDialog.setContentView(R.layout.waiting_view);
                }
            });
        }
    }

    public void setCancelable(boolean isCancelable)
    {
        if (progressDialog != null)
        {
            progressDialog.setCancelable(isCancelable);
        }
    }

    public void closeWaitingActivity()
    {
        if (isShow && progressDialog != null)
        {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    progressDialog.dismiss();
                }
            });

            this.isShow = false;
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener)
    {
        if (progressDialog != null)
        {
            progressDialog.setOnCancelListener(onCancelListener);
        }
    }
}
