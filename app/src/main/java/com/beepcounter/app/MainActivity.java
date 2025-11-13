package com.beepcounter.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    
    private Button controlButton;
    private TextView countText;
    private TextView statusText;
    
    private BeepDetector beepDetector;
    private boolean isListening = false;
    private int beepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controlButton = findViewById(R.id.controlButton);
        countText = findViewById(R.id.countText);
        statusText = findViewById(R.id.statusText);

        updateUI();

        controlButton.setOnClickListener(v -> {
            if (checkPermission()) {
                toggleListening();
            } else {
                requestPermission();
            }
        });

        beepDetector = new BeepDetector(new BeepDetector.BeepListener() {
            @Override
            public void onBeepDetected() {
                runOnUiThread(() -> {
                    beepCount++;
                    updateUI();
                });
            }
        });
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toggleListening();
            } else {
                Toast.makeText(this, R.string.permission_required, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void toggleListening() {
        if (isListening) {
            stopListening();
        } else {
            startListening();
        }
    }

    private void startListening() {
        if (beepDetector.start()) {
            isListening = true;
            beepCount = 0;
            updateUI();
        } else {
            Toast.makeText(this, "Failed to start audio recording", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopListening() {
        beepDetector.stop();
        isListening = false;
        updateUI();
    }

    private void updateUI() {
        countText.setText(getString(R.string.beep_count, beepCount));
        
        if (isListening) {
            controlButton.setText(R.string.stop_listening);
            statusText.setText(R.string.status_listening);
        } else {
            controlButton.setText(R.string.start_listening);
            statusText.setText(R.string.status_ready);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (beepDetector != null) {
            beepDetector.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isListening) {
            stopListening();
        }
    }
}

