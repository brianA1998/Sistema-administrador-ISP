package modelo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import db.DataBase;

public class Usuario {
	private long documento;
	private String nombre;
	private String apellido;
	private LocalDate nacimiento;
	private Direccion direccion;
	private long telefono;
	private String correo;
	private LocalDate ingreso;
	private float salario;
	private String usuario;

	/**
	 * crea un usuario con todos los datos
	 * 
	 * @param documento  numero de documento del usuario
	 * @param nombre     nombre del usuario
	 * @param apellido   apellido del usuario
	 * @param nacimiento fecha de nacimiento del usuario
	 * @param direccion  direccion donde vive el usuario
	 * @param telefono   numero de telefono del usuario
	 * @param correo     direccion de correo electronico del usuario
	 * @param ingreso    fecha de ingreso del usuario
	 * @param salario    salario que cobra el usuario
	 */
	public Usuario(long documento, String nombre, String apellido, LocalDate nacimiento, Direccion direccion,
			long telefono, String correo, LocalDate ingreso, float salario) {
		super();
		this.documento = documento;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nacimiento = nacimiento;
		this.direccion = direccion;
		this.telefono = telefono;
		this.correo = correo;
		this.ingreso = ingreso;
		this.salario = salario;
	}

	// crea un usuario sin datos;
	public Usuario() {
		this.documento = -1L;
		this.nombre = null;
		this.apellido = null;
		this.nacimiento = null;
		this.direccion = null;
		this.telefono = -1L;
		this.correo = null;
		this.ingreso = null;
		this.salario = -1;
	}

	/**
	 * @return the documento
	 */
	public long getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(long documento) {
		this.documento = documento;
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
	 * @return the apellido
	 */
	public String getApellido() {
		return apellido;
	}

	/**
	 * @param apellido the apellido to set
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * @return the nacimiento
	 */
	public LocalDate getNacimiento() {
		return nacimiento;
	}

	/**
	 * @param nacimiento the nacimiento to set
	 */
	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}

	/**
	 * @return the direccion
	 */
	public Direccion getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the telefono
	 */
	public long getTelefono() {
		return telefono;
	}
	
	

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return the ingreso
	 */
	public LocalDate getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(LocalDate ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the salario
	 */
	public float getSalario() {
		return salario;
	}

	/**
	 * @param salario the salario to set
	 */
	public void setSalario(float salario) {
		this.salario = salario;
	}

	/**
	 * inserta un nuevo usuario en la base de datos
	 * 
	 * @param user user name del usuario
	 * @param pass contraseña del usuario
	 * @return true si funciono o false si ocurrio un error
	 */
	public boolean insert(String user, String pass) {
		// consulta
		DataBase db = new DataBase();
		String sql = "INSERT INTO usuario(usuario_id, usuario_documento, usuario_nombre, usuario_apellido, "
				+ "usuario_nacimiento, usuario_domicilio, ciudad_id, usuario_telefono, usuario_correo, "
				+ "usuario_fecha_ingreso, usuario_salario, usuario_usuario, usuario_password, usuario_estado) "
				+ "VALUES (NULL,?,?,?,?,?,?,?,?,?,?,?,?,1);";

		db.setQuery(sql);

		db.setParametro(1, documento);
		db.setParametro(2, nombre);
		db.setParametro(3, apellido);

		// verifica los parametros nulos
		if (nacimiento == null)
			db.setNull(4, Types.DATE);
		else
			db.setParametro(4, Date.valueOf(nacimiento));

		if (direccion == null || direccion.getDomicilio() == null)
			db.setNull(5, Types.VARCHAR);
		else
			db.setParametro(5, direccion.getDomicilio());

		db.setParametro(6, Direccion.getCiudadId(direccion.getCiudad()));

		if (telefono == -1L)
			db.setNull(7, Types.BIGINT);
		else
			db.setParametro(7, telefono);

		if (correo == null)
			db.setNull(8, Types.VARCHAR);
		else
			db.setParametro(8, correo);

		if (ingreso == null)
			db.setNull(9, Types.DATE);
		else
			db.setParametro(9, Date.valueOf(ingreso));

		if (salario == -1)
			db.setNull(10, Types.DECIMAL);
		else
			db.setParametro(10, salario);

		db.setParametro(11, user);
		db.setParametro(12, pass);
		// ejecuta el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * actualiza los datos de un usuario en la base de datos
	 * 
	 * @param user usuario a modificar
	 * @param pass contraseña del usuario
	 * @return true si funciono el update o false si no funciono
	 */
	public boolean update(String user, String pass) {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE usuario SET  usuario_nombre = ?"
				+ ", usuario_apellido = ?, usuario_telefono = ?, usuario_correo = ?,"
				+ " usuario_salario = ?, usuario_password = ?, usuario_usuario = ?, usuario_nacimiento = ?, ciudad_id = ?"
				+ "  WHERE usuario_documento = ? ;";

		db.setQuery(sql);

		// verifica los parametros nulos

		// Parametros que no pueden ser null
		db.setParametro(1, nombre);
		db.setParametro(2, apellido);
		db.setParametro(6, pass);
		db.setParametro(7, user);
		db.setParametro(10, documento);

		if (nacimiento == null)
			db.setNull(8, Types.DATE);
		else
			db.setParametro(8, Date.valueOf(nacimiento));

		if (direccion == null || direccion.getCiudad() == null)
			db.setNull(9, Types.INTEGER);
		else
			db.setParametro(9, Direccion.getCiudadId(direccion.getCiudad()));

		if (telefono == -1L)
			db.setNull(3, Types.BIGINT);
		else
			db.setParametro(3, telefono);

		if (correo == null)
			db.setNull(4, Types.VARCHAR);
		else
			db.setParametro(4, correo);

		if (salario == -1)
			db.setNull(5, Types.DECIMAL);
		else
			db.setParametro(5, salario);

		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Obtiene todos los datos de un usuario en especifico
	 * 
	 * @param documento documento de un usuario a obtener todos los datos
	 */
	public Usuario(long documento) {
		String sql = "SELECT U.usuario_nombre,U.usuario_apellido,U.usuario_nacimiento,U.usuario_domicilio,U.usuario_telefono,U.usuario_correo,U.usuario_salario,U.usuario_usuario,U.usuario_fecha_ingreso,C.ciudad_nombre"
				+ " FROM usuario U INNER JOIN ciudad C ON U.ciudad_id = C.ciudad_id " + " WHERE usuario_documento = ?";
		DataBase database = new DataBase();

		database.setQuery(sql); // consulta sql
		database.setParametro(1, documento);
		ResultSet rs = database.executeQuery();// retorna resulset

		try {
			if (rs.next()) { // existe usuario
				this.direccion = new Direccion();
				this.documento = documento;
				this.nombre = rs.getString(1);
				this.apellido = rs.getString(2);
				this.nacimiento = (rs.getDate(3) == null) ? null : rs.getDate(3).toLocalDate();
				direccion.setDomicilio(rs.getString(4));
				this.telefono = (rs.getLong(5) == 0) ? 0 : rs.getLong(5);
				this.correo = rs.getString(6);
				this.salario = (rs.getLong(7) == 0) ? 0 : rs.getLong(7);
				this.usuario = rs.getString(8);
				this.ingreso = (rs.getDate(9) == null) ? null : rs.getDate(9).toLocalDate();
				direccion.setCiudad(rs.getString(10));

			} else { // no existe el usuario
				this.nombre = null;
				this.apellido = null;
				this.documento = -1L;
				this.nacimiento = null;
				this.direccion = null;
				this.telefono = -1L;
				this.correo = null;
				this.salario = -1L;
				this.usuario = null;
				this.ingreso = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Este metodo cambia el estado de habilitado (1) a estado (0) que es bloqueado
	 * 
	 * @param documento documento del usuario a cambiar el estado
	 * @return true si funciono o false si no funciono
	 */
	public boolean cambiarEstado(long documento) {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE usuario SET usuario_estado = '0' WHERE usuario_documento = ?;";

		db.setQuery(sql);
		db.setParametro(1, documento);
		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

}
