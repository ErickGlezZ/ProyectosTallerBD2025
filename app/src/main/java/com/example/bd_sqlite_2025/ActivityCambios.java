package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import db.EscuelaBD;
import entities.Alumno;
import entities.CustomAdapter;

public class ActivityCambios extends Activity {

    EditText cajaNumControl, cajaNombre;
    RecyclerView rvListaCambios;
    CustomAdapter adapter;
    ArrayList<Alumno> listaAlumnos = new ArrayList<>();

    EscuelaBD bd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        cajaNumControl = findViewById(R.id.cajaNumControlCambios);
        cajaNombre = findViewById(R.id.cajaNombreCambios);
        cajaNombre.setEnabled(false);
        rvListaCambios = findViewById(R.id.recyclerCambios);

        rvListaCambios.setLayoutManager(new LinearLayoutManager(this));

        bd = EscuelaBD.getAppDatabase(getBaseContext());

        cargarLista();

        cajaNombre.setFilters(new InputFilter[]{
                soloLetrasFilter
        });
    }



    private void cargarLista() {

        new Thread(() -> {

            List<Alumno> data = bd.alumnoDAO().obtenerAlumnos();

            listaAlumnos.clear();
            listaAlumnos.addAll(data);

            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new CustomAdapter(listaAlumnos);
                    rvListaCambios.setAdapter(adapter);
                } else {
                    adapter.actualizarDatos(listaAlumnos);
                }
            });

        }).start();
    }



    public void buscarAlumno(View v) {
        String nc = cajaNumControl.getText().toString().trim();

        if (nc.isEmpty()) {
            Toast.makeText(this, "Ingresa un número de control", Toast.LENGTH_SHORT).show();
            return;
        }



        new Thread(() -> {

            Alumno alumno = bd.alumnoDAO().buscarAlumnoPorNumControl(nc);

            runOnUiThread(() -> {
                if (alumno != null) {
                    cajaNombre.setText(alumno.getNombre());
                    cajaNombre.setEnabled(true);
                } else {
                    cajaNombre.setText("");
                    cajaNombre.setEnabled(false);
                    Toast.makeText(this, "Alumno NO encontrado", Toast.LENGTH_SHORT).show();
                }
            });

        }).start();
    }



    public void editarAlumno(View v) {

        String nc = cajaNumControl.getText().toString().trim();
        String nombre = cajaNombre.getText().toString().trim();


        if (nc.isEmpty()) {
            Toast.makeText(this, "Ingresa un número de control", Toast.LENGTH_SHORT).show();
            return;
        }


        if (nombre.isEmpty()) {
            Toast.makeText(this, "Asegurate de llenar los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            Toast.makeText(this, "Solo debe contener letras", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {

            Alumno alumno = bd.alumnoDAO().buscarAlumnoPorNumControl(nc);

            if (alumno == null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Alumno NO existe, no se puede editar", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            alumno.setNombre(nombre);
            bd.alumnoDAO().actualizarAlumno(alumno);

            runOnUiThread(() -> {
                Toast.makeText(this, "Alumno actualizado correctamente", Toast.LENGTH_SHORT).show();
                restablecerCampos(null);
                cargarLista();
            });

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
