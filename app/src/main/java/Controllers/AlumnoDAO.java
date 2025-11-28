package Controllers;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Alumno;

@Dao
public interface AlumnoDAO {

    //-------------- ALTAS ----------------
    @Insert
    public void agregarAlumno(Alumno alumno);

    //-------------- BAJAS ----------------
    @Delete
    public void eliminarAlumno(Alumno alumno);

    @Query("DELETE FROM alumno WHERE numControl=:nc")
    public void eliminarAlumnoPorNumControl(String nc);

    @Query("SELECT * FROM alumno WHERE numControl = :nc")
    Alumno buscarAlumnoPorNumControl(String nc);

    //-------------- CAMBIOS ----------------
    @Update
    public void actualizarAlumno(Alumno alumno);

    //@Query("UPDATE alumno SET nombre=:n, primerAp=:pa WHERE numControl=:nc")
    @Query("UPDATE alumno SET nombre=:n WHERE numControl=:nc")
    public void actualizarAlumnoPorNumControl(String n, String nc);

    //-------------- CONSULTAS ----------------
    @Query("SELECT * FROM alumno")
    public List<Alumno> obtenerAlumnos();

    @Query("SELECT * FROM alumno WHERE nombre=:n")
    public List<Alumno> mostrarPorNombre(String n);

}
