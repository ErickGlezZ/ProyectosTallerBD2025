package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import db.EscuelaBD;
import entities.Alumno;

public class ActivityAltas extends Activity {

    EditText cajaNumControl, cajaNombre;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);

        cajaNumControl = findViewById(R.id.caja_num_control_altas);
        cajaNombre = findViewById(R.id.caja_nombre_altas);

        cajaNombre.setFilters(new InputFilter[]{
                soloLetrasFilter
        });

    }

    public void agregarAlumno(View v){
        String nc = cajaNumControl.getText().toString();
        String n = cajaNombre.getText().toString();

        if (nc.isEmpty() || n.isEmpty()) {
            Toast.makeText(this, "No se permiten campos vacíos", Toast.LENGTH_SHORT).show();
            return;
        }


        if (!nc.matches("\\d+")) {
            Toast.makeText(this, "El número de control solo debe contener números", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!n.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            Toast.makeText(this, "Solo debe contener letras", Toast.LENGTH_SHORT).show();
            return;
        }
        Alumno alumno = new Alumno(nc, n);
        EscuelaBD bd = EscuelaBD.getAppDatabase(getBaseContext());
        //Looper y Handler
        new Thread(() -> {

            try {

                bd.alumnoDAO().agregarAlumno(alumno);

                // Si no hubo error:
                runOnUiThread(() -> {
                    Toast.makeText(this, "Inserción correcta", Toast.LENGTH_SHORT).show();
                    cajaNumControl.setText("");
                    cajaNombre.setText("");
                    cajaNumControl.requestFocus();
                });

            } catch (Exception e) {


                if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Ese número de control ya existe", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this, "Error al insertar: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }

            }

        }).start();
    }

    private InputFilter soloLetrasFilter = (source, start, end, dest, dstart, dend) -> {


        String permitidos = "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+";

        for (int i = start; i < end; i++) {
            String c = String.valueOf(source.charAt(i));

            if (!c.matches(permitidos)) {
                return "";
            }
        }

        return null;
    };


    public void restablecerCampos(View v) {
        if (cajaNumControl.getText().toString().isEmpty() &&
                cajaNombre.getText().toString().isEmpty()) {

            Toast.makeText(this, "No hay elementos por restablecer", Toast.LENGTH_SHORT).show();
            return;
        }

        cajaNumControl.setText("");
        cajaNombre.setText("");
        cajaNumControl.requestFocus();
    }



    public void regresar(View v) {
        finish();
    }
}


