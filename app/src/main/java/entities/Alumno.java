package entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Alumno {

    @PrimaryKey
    @NonNull
    public String numControl;

    @NonNull
    @ColumnInfo(name = "Nombre")
    public String nombre;
    public String apellido;

    public Alumno(@NonNull String numControl, @NonNull String nombre) {
        this.numControl = numControl;
        this.nombre = nombre;
    }

    @NonNull
    public String getNumControl() {
        return numControl;
    }

    public void setNumControl(@NonNull String numControl) {
        this.numControl = numControl;
    }

    @NonNull
    public String getNombre() {
        return nombre;
    }

    public String getApellido() {return apellido;}

    //public boolean getId() {return idAlumno;}

    public void setNombre(@NonNull String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Alumno " +  '\n' +
        "Numero de Control= '" + numControl + '\'' + '\n' +
                "Nombre= '" + nombre + '\'';
    }


}
