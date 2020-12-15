package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;

import db.DataBase;

public class Preventa {
	private String nombre;
	private int idPreventa;
	private int idObservacion;
	private LocalDate fechaConsulta;
	private long telefono;
	private String barrio;
	private String ciudad;
	private LocalDateTime fechaObservacion;
	private String observacion;

	/**
	 * Crea una preventa con los datos especificados
	 * 
	 * @param nombre        nombre del cliente que realiza la consulta
	 * @param idPreventa    id del cliente que realizo la consulta
	 * @param fechaConsulta fecha en la cual el cliente consulto
	 */
	public Preventa(String nombre, int idPreventa, LocalDate fechaConsulta) {
		super();
		this.nombre = nombre;
		this.idPreventa = idPreventa;
		this.fechaConsulta = fechaConsulta;
	}

	/**
	 * Crea una preventa sin datos
	 */
	public Preventa() {

	}

	/**
	 * Crea una consulta con los datos especificados
	 * 
	 * @param fechaObservacion fecha que se realizo la observacion
	 * @param observacion      observacion que se realiza
	 */
	public Preventa(String observacion, LocalDateTime fechaObservacion) {
		super();
		this.fechaObservacion = fechaObservacion;
		this.observacion = observacion;

	}

	/**
	 * Crea una preventa sin datos
	 */
	public Preventa(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @return the fechaObservacion
	 */
	public LocalDateTime getFechaObservacion() {
		return fechaObservacion;
	}

	/**
	 * @param fechaObservacion the fechaObservacion to set
	 */
	public void setFechaObservacion(LocalDateTime fechaObservacion) {
		this.fechaObservacion = fechaObservacion;
	}

	/**
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * Crea una preventa con los datos especificados
	 * 
	 * @param nombre
	 * @param telefono
	 * @param barrio
	 * @param ciudad
	 */
	public Preventa(String nombre, long telefono, String barrio, String ciudad) {
		this.nombre = nombre;
		this.telefono = telefono;
		this.barrio = barrio;
		this.ciudad = ciudad;
	}

	/**
	 * Inserta una nueva preventa en la base de datos
	 * 
	 * @return true si funciono o false si ocurrio un error
	 */
	public boolean insert() {
		int idCiudad = Direccion.getCiudadId(ciudad);
		int idBarrio = Direccion.getBarrioId(barrio, ciudad);

		// consulta
		DataBase db = DataBase.getInstancia();
		String sql = "INSERT IGNORE INTO preventa (preventa_id, preventa_nombre, preventa_fecha, preventa_telefono, ciudad_id, barrio_id) "
				+ " VALUES (?, ?, CURRENT_DATE(), ?, ?, ?);";

		db.setQuery(sql);

		if (idPreventa == 0) {
			db.setNull(1, Types.INTEGER);
		} else {
			db.setParametro(1, idPreventa);
		}

		db.setParametro(2, nombre);
		db.setParametro(3, telefono);
		db.setParametro(4, idCiudad);
		db.setParametro(5, idBarrio);

		// Ejecuto el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Inserta una observacion en la base de datos
	 * 
	 * @return true si funciono la insercion de los datos o false si no funciono
	 */
	public boolean insertarObservacion() {
		// consulta
		DataBase db = DataBase.getInstancia();
		db.setTransaction();
		// insercion de consulta
		String sql = "INSERT INTO consulta (consulta_fecha, consulta_observacion) VALUES (CURRENT_TIMESTAMP(), ?);";
		db.setQuery(sql);
		db.setParametro(1, observacion);

		// obtiene el id generado
		try {
			ResultSet rs = db.executeGetKeys();
			if (rs.next())
				idObservacion = rs.getInt(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// insercion de trabla intermedia
		sql = "INSERT INTO preventa_consulta (consulta_id, preventa_id) VALUES (?, ?);";
		db.setQuery(sql);
		db.setParametro(1, idObservacion);
		db.setParametro(2, idPreventa);
		db.execute();

		boolean resultado = db.commit();
		db.close();
		return resultado;
	}

	/**
	 * @return the idObservacion
	 */
	public int getIdObservacion() {
		return idObservacion;
	}

	/**
	 * @param idObservacion the idObservacion to set
	 */
	public void setIdObservacion(int idObservacion) {
		this.idObservacion = idObservacion;
	}

	/**
	 * @return the telefono
	 */
	public long getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the barrio
	 */
	public String getBarrio() {
		return barrio;
	}

	/**
	 * @param barrio the barrio to set
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @return the idPreventa
	 */
	public int getIdPreventa() {
		return idPreventa;
	}

	/**
	 * @param idPreventa the idPreventa to set
	 */
	public void setIdPreventa(int idPreventa) {
		this.idPreventa = idPreventa;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the fechaConsulta
	 */
	public LocalDate getFechaConsulta() {
		return fechaConsulta;
	}

	/**
	 * @param fechaConsulta the fechaConsulta to set
	 */
	public void setFechaConsulta(LocalDate fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

}
