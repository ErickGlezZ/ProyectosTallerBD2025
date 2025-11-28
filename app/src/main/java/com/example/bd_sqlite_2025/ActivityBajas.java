package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
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

public class ActivityBajas extends Activity {

    EditText cajaNumControl, cajaNombre;
    RecyclerView rvListaBajas;
    CustomAdapter adapter;
    ArrayList<Alumno> listaAlumnos = new ArrayList<>();

    EscuelaBD bd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        cajaNumControl = findViewById(R.id.cajaNumControlBajas);
        cajaNombre = findViewById(R.id.cajaNombreBajas);
        rvListaBajas = findViewById(R.id.recyclerBajas);

        rvListaBajas.setLayoutManager(new LinearLayoutManager(this));

        bd = EscuelaBD.getAppDatabase(getBaseContext());

        cargarLista();
    }



    private void cargarLista() {

        new Thread(() -> {

            List<Alumno> data;
            try {
                data = bd.alumnoDAO().obtenerAlumnos();
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            listaAlumnos.clear();
            listaAlumnos.addAll(data);

            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new CustomAdapter(listaAlumnos);
                    rvListaBajas.setAdapter(adapter);
                } else {
                    adapter.actualizarDatos(listaAlumnos);
                }
            });

        }).start();
    }



    private boolean validarNumControl(String nc) {

        if (nc.isEmpty()) {
            Toast.makeText(this, "Ingresa un número de control", Toast.LENGTH_SHORT).show();
            return false;
        }

        /*
        if (!nc.matches("\\d+")) {
            Toast.makeText(this, "El número de control solo debe contener números", Toast.LENGTH_SHORT).show();
            return false;
        }
        */

        return true;
    }



    public void buscarAlumno(View v) {
        String nc = cajaNumControl.getText().toString().trim();

        if (!validarNumControl(nc)) return;

        new Thread(() -> {
            Alumno alumno;

            try {
                alumno = bd.alumnoDAO().buscarAlumnoPorNumControl(nc);
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error buscando el alumno", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            runOnUiThread(() -> {
                if (alumno != null) {
                    cajaNombre.setText(alumno.getNombre());
                } else {
                    cajaNombre.setText("");
                    Toast.makeText(this, "Alumno NO encontrado", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }



    public void eliminarAlumno(View v) {
        String nc = cajaNumControl.getText().toString().trim();

        if (!validarNumControl(nc)) return;

        new Thread(() -> {

            Alumno alumno;

            try {
                alumno = bd.alumnoDAO().buscarAlumnoPorNumControl(nc);
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al verificar el alumno", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            if (alumno == null) {
                runOnUiThread(() ->
                        Toast.makeText(this, "No se puede eliminar: Alumno NO existe", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            try {
                bd.alumnoDAO().eliminarAlumnoPorNumControl(nc);
            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
                );
                return;
            }

            runOnUiThread(() -> {
                Toast.makeText(this, "Alumno eliminado correctamente", Toast.LENGTH_SHORT).show();
                restablecerCampos(null);
                cargarLista();
            });

        }).start();
    }



    public void restablecerCampos(View v) {
        if (cajaNumControl.getText().isEmpty() || cajaNombre.getText().isEmpty()){
            Toast.makeText(this, "No hay elementos por restablecer", Toast.LENGTH_SHORT).show();
        }
        cajaNumControl.setText("");
        cajaNombre.setText("");
        cajaNumControl.requestFocus();
    }



    public void regresar(View v) {
        finish();
    }
}
