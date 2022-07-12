package com.ess.essandroidbaselibrary.Interface;

import android.bluetooth.BluetoothDevice;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 2/25/2019.
 */

public interface OnNewBluetoothDeviceConnectedListener
{
    public void onNewPrinterConnected(BluetoothDevice device);
}
