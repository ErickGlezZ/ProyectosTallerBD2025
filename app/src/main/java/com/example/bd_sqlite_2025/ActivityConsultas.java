package com.example.bd_sqlite_2025;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import db.EscuelaBD;
import entities.Alumno;
import entities.CustomAdapter;

public class ActivityConsultas extends Activity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    ArrayList<Alumno> datos = null;

    SearchView cajaFiltro;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        cajaFiltro = findViewById(R.id.cajaBuscar);
        // Obtener el EditText interno del SearchView
        int id = cajaFiltro.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchEditText = cajaFiltro.findViewById(id);

        searchEditText.setFilters(new InputFilter[]{
                (source, start, end, dest, dstart, dend) -> {
                    if (source.toString().matches("[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ ]+")) {

                        return null;
                    }
                    /*
                    Toast.makeText(ActivityConsultas.this,
                            "Solo puedes escribir letras, números y espacios",
                            Toast.LENGTH_SHORT).show();

                     */
                    return "";

                }
        });


        cajaFiltro.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                // Aplicar filtro al adapter
                if (adapter != null) {
                    ((CustomAdapter) adapter).getFilter().filter(newText);
                }

                return true;
            }
        });

        recyclerView = findViewById(R.id.recycleview_alumnos);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        EscuelaBD bd = EscuelaBD.getAppDatabase(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                datos = (ArrayList<Alumno>) bd.alumnoDAO().obtenerAlumnos();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomAdapter(datos);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }).start();
    }
    public void regresar(View v) {
        finish();
    }
}



















