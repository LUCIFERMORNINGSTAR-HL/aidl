package com.example.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    IColorInterface colorService = null; // The AIDL Interface
    Button btnChangeColor;

    // Connection monitor
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Convert the raw binder to our Java Interface
            colorService = IColorInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            colorService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChangeColor = findViewById(R.id.btnChangeColor); // Make sure you have this ID in XML!

        // 1. Bind to the Service
        Intent intent = new Intent("com.example.aidl.COLOR_SERVICE");
        intent.setPackage("com.example.aidl"); // Pointing to ourselves for this demo
        bindService(intent, connection, BIND_AUTO_CREATE);

        // 2. Button Click Listener
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (colorService != null) {
                    try {
                        // Call the server (Service) to get the color
                        int color = colorService.getRandomColor();
                        // Update UI
                        v.setBackgroundColor(color);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}