package entities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bd_sqlite_2025.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> implements Filterable {

    private ArrayList<Alumno> originalList;   // Lista completa de Room
    private ArrayList<Alumno> filteredList;   // Lista mostrada

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textview_alumno);
        }

        public TextView getTextView() { return textView; }
    }

    public CustomAdapter(ArrayList<Alumno> dataset) {
        this.originalList = new ArrayList<>(dataset);
        this.filteredList = new ArrayList<>(dataset);
    }

    public void actualizarDatos(ArrayList<Alumno> nuevaLista) {
        this.originalList.clear();
        this.originalList.addAll(nuevaLista);

        this.filteredList.clear();
        this.filteredList.addAll(nuevaLista);

        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_recycleview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        holder.getTextView().setText(filteredList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // ---------------------------------
    //   FILTRO SOLO POR NUMCONTROL y NOMBRE
    // ---------------------------------
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String query = constraint.toString().toLowerCase().trim();
                ArrayList<Alumno> temp = new ArrayList<>();

                // Si está vacío, mostramos todo
                if (query.isEmpty()) {
                    temp.addAll(originalList);
                } else {
                    for (Alumno a : originalList) {

                        String nombre = a.getNombre() == null ? "" : a.getNombre();
                        String numControl = a.getNumControl() == null ? "" : a.getNumControl();

                        if (nombre.toLowerCase().contains(query) ||
                                numControl.toLowerCase().contains(query))
                        {
                            temp.add(a);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = temp;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredList.clear();  // Vaciar la lista mostrada
                filteredList.addAll((ArrayList<Alumno>) results.values);  // Cargar nuevos datos

                notifyDataSetChanged();  // Refrescar RecyclerView


            }

        };
    }
}
