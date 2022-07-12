package com.ess.essandroidbaselibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanBarCodeActivity extends AppCompatActivity
{
    public static final String EXTRA_QR_CODE_RESULT_STRING = "EXTRA_RESULT_STRING";

    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private Button btnAction;
    private String intentData = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bar_code);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
    }

    private void initViews()
    {
        txtBarcodeValue = (TextView) findViewById(R.id.barcode_activity_states_textView);
        surfaceView = (SurfaceView) findViewById(R.id.barcode_activity_surfaceView);
        btnAction = (Button) findViewById(R.id.barcode_activity_button);


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
                }
            }
        });
    }

    private void initialiseDetectorsAndSources()
    {
        Toast.makeText(getApplicationContext(), "Reading QR code", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                //.setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .setFacing(CameraSource.CAMERA_FACING_BACK)//
                .setRequestedFps(35.0f)//
                .setRequestedPreviewSize(960, 960)//
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                try
                {
                    if (ActivityCompat.checkSelfPermission(ScanBarCodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    {
                        cameraSource.start(surfaceView.getHolder());
                    }
                    else
                    {
                        ActivityCompat.requestPermissions(ScanBarCodeActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release()
            {
                Toast.makeText(getApplicationContext(), "To prevent out of memory, scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections)
            {
                final SparseArray<Barcode> barCodes = detections.getDetectedItems();

                if (barCodes.size() != 0)
                {
                    Toast.makeText(getApplicationContext(), barCodes.valueAt(0).rawValue, Toast.LENGTH_SHORT).show();

                    arrangeResultAndExit(barCodes.valueAt(0).rawValue);

//                    txtBarcodeValue.post(new Runnable() {
//                        @Override
//                        public void run()
//                        {
//
//                            if (barCodes.valueAt(0).rawValue != null)
//                            {
//                                txtBarcodeValue.removeCallbacks(null);
//                                intentData = barCodes.valueAt(0).email.address;
//                                txtBarcodeValue.setText(intentData);
//                                btnAction.setText("ADD CONTENT TO THE MAIL");
//                            }
//                            else
//                            {
//                                btnAction.setText("LAUNCH URL");
//                                intentData = barCodes.valueAt(0).displayValue;
//                                txtBarcodeValue.setText(intentData);
//                            }
//                        }
//                    });
                }
            }
        });
    }


    @Override
    protected void onPause()
    {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void arrangeResultAndExit(String result)
    {
        // Go to main page.
        Intent intent = new Intent();
        intent.putExtra(EXTRA_QR_CODE_RESULT_STRING, result); //value should be your string from the edittext.
        setResult(RESULT_OK, intent); //The data you want to send back
        finish();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        super.onBackPressed();
        return true;
    }
}

