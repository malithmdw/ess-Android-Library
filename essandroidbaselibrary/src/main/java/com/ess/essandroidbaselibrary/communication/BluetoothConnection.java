package com.ess.essandroidbaselibrary.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.ParcelUuid;

import com.ess.essandroidbaselibrary.Interface.OnNewBluetoothDeviceConnectedListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Malith on 4/9/2018.
 */

public class BluetoothConnection
{
    ///////////////////////////////////////// - Singletom
    private static BluetoothConnection INSTANCE;

    public static BluetoothConnection getInstance()
    {
        if(INSTANCE == null)
        {
            INSTANCE = new BluetoothConnection();
        }
        return INSTANCE;
    }
    ///////////////////////////////////////// - Singletom

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice connectedBluetoothDevice;

    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Thread thread;
    private byte[] readBuffer;
    private int readBufferPosition;
    private volatile boolean stopWorker;
    private ArrayList<OnNewBluetoothDeviceConnectedListener> newPrinterConnectedListeners = new ArrayList<>();

    private void BluetoothConnection()
    {
    }

    public boolean isBluetoothTurnedOn()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled())
        {
            return true;
        }

        return false;
    }

    public void bluetoothTurnOn()
    {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();

            while (!bluetoothAdapter.isEnabled())
            {
                //wait until bluetooth connection
            }
        }
    }

    public void bluetoothTurnOff()
    {
        if(isBluetoothTurnedOn())
        {
            bluetoothAdapter.disable();
        }
    }

    public boolean tryToConnectDevice(String deviceUUID)
    {
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        if (devices != null && !devices.isEmpty())
        {
            for (BluetoothDevice device : devices)
            {
                if (deviceUUID.equals(device.getName()))
                {
                    boolean connected = openBluetoothPrinter(device);

                    if (connected)
                    {
                        connectedBluetoothDevice = device;

                        // Update connected printer details
                        for (OnNewBluetoothDeviceConnectedListener listener : this.newPrinterConnectedListeners)
                        {
                            listener.onNewPrinterConnected(device);
                        }

                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<String> getAllAvailableBluetoothDeviceNames()
    {
        ArrayList<String> list = new ArrayList<>();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        if (devices != null && !devices.isEmpty())
        {
            for (BluetoothDevice device : devices)
            {
                list.add(device.getName());
            }
        }
        return list;
    }

    public ArrayList<BluetoothDevice> getAllAvailableBluetoothDevices()
    {
        ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

        if (devices != null && !devices.isEmpty())
        {
            for (BluetoothDevice device : bluetoothAdapter.getBondedDevices())
            {
                bluetoothDevices.add(device);
            }
        }
        return bluetoothDevices;
    }

    public boolean isPrinterConnected()
    {
        return (bluetoothSocket != null && bluetoothSocket.isConnected() && outputStream != null);
    }

    public String getConnectedDeviceDisplayName()
    {
        if(connectedBluetoothDevice != null)
        {
            return connectedBluetoothDevice.getName();
        }
        return "";
    }

    /**
     * Lengthy operation to iterate and try to connect each UUID.
     * @param device
     * @return
     */
    private boolean openBluetoothPrinter(BluetoothDevice device)
    {
        if (device != null && device.getUuids() != null)
        {
            for (ParcelUuid uuid: device.getUuids())
            {
                try
                {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(uuid.getUuid());
                    bluetoothSocket.connect();

                    outputStream = bluetoothSocket.getOutputStream();
                    inputStream = bluetoothSocket.getInputStream();
                    break;
                }
                catch (IOException ex)
                {
                }
                catch (Exception e)
                {
                }
            }

            //beginListenData();

            return (bluetoothSocket != null && bluetoothSocket.isConnected());
        }

        return false;
    }

    private void beginListenData()
    {
        try
        {
            final Handler handler = new Handler();
            final byte delimiter = 10;
            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted() && !stopWorker)
                    {
                        try
                        {
                            int byteAvailable = inputStream.available();
                            if (byteAvailable > 0)
                            {
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for (int i = 0; i < byteAvailable; i++)
                                {
                                    byte b = packetByte[i];
                                    if(b == delimiter)
                                    {
                                        byte[] encodeByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodeByte,0,
                                                encodeByte.length
                                        );

                                        final String data = new String(encodeByte,"US-ASCII");
                                        readBufferPosition = 0;

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run()
                                            {
                                                //lblPrintername.setText(data);
                                            }
                                        });
                                    }
                                    else
                                    {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            stopWorker = true;
                        }
                    }
                }
            });
            thread.start();
        }
        catch (Exception e)
        {

        }
    }

    public void printData(String data)
    {
        if (bluetoothSocket != null && bluetoothSocket.isConnected() && outputStream != null)
        {
            try
            {
                outputStream.write(data.getBytes());
            }
            catch (IOException e)
            {}
        }
    }

    public void printData(byte[] b) throws IOException
    {
        if (bluetoothSocket != null && bluetoothSocket.isConnected() && outputStream != null)
        {
            outputStream.write(b);
        }
    }

    public void destruct()
    {
        try
        {
            stopWorker = true;

            if (bluetoothSocket != null)
            {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }

            if (bluetoothAdapter != null)
            {
                bluetoothAdapter.cancelDiscovery();
                bluetoothAdapter = null;
            }

            if (connectedBluetoothDevice != null)
            {
                connectedBluetoothDevice = null;
            }

            if (outputStream != null)
            {
                outputStream.close();
                outputStream = null;
            }

            if (inputStream != null)
            {
                inputStream.close();
                inputStream = null;
            }

            if (thread != null)
            {
                thread.interrupt();
                thread = null;
            }

            finalize();

        }
        catch (IOException e)
        {
        }
        catch (Exception ex)
        {
        }
        catch (Throwable ignored) {
        }
    }
}
