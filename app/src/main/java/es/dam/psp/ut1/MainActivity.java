package es.dam.psp.ut1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.os.Process;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import es.dam.psp.ut1.servicio.DescargaService;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> pedirPermiso =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    concedido -> Log.d(DescargaService.TAG,
                            "POST_NOTIFICATIONS concedido=" + concedido));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvPid = findViewById(R.id.tvPid);
        tvPid.setText("PID de la actividad: " + Process.myPid());
        Log.d(DescargaService.TAG, "MainActivity · PID=" + Process.myPid()
                + " · hilo=" + Thread.currentThread().getName());

        // Android 13+ (API 33): el permiso de notificaciones se pide en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            pedirPermiso.launch(Manifest.permission.POST_NOTIFICATIONS);
        }

        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnParar = findViewById(R.id.btnParar);

        btnIniciar.setOnClickListener(v ->
                ContextCompat.startForegroundService(this,
                        new Intent(this, DescargaService.class)));

        btnParar.setOnClickListener(v ->
                stopService(new Intent(this, DescargaService.class)));
    }
}