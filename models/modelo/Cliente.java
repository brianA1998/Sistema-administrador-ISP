package modelo;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cliente {
	private String nombre;
	private String apellido;
	private long documento;
	private Direccion direccion;
	private long telefono;
	private long celular;
	private String correo;
	private int numeroAbonado;
	private LocalDate ingreso;
	private LocalDate nacimiento;
	private String tipoCliente;
	private String situacion;
	private String lugar;
	private String puesto;

	private String iva;
	private String twitter;
	private String web;
	private String instagram;
	private String facebook;
	private String linkedin;

	/**
	 * Crea un cliente con los datos especificados
	 * 
	 * @param nombre     nombre del cliente
	 * @param apellido   apellido del cliente
	 * @param documento  documento del cliente
	 * @param domicilio  domicilio donde vive el cliente
	 * @param manzana    manzana donde vive el cliente
	 * @param monoblock  monoblock donde vive el cliente
	 * @param telefono   telefono del cliente
	 * @param celular    celular del cliente
	 * @param correo     correo del cliente
	 * @param nacimiento nacimiento fecha nacimiento del cliente
	 * @param ingreso    ingreso fecha que se cargo el cliente al sistema
	 */
	public Cliente(String nombre, String apellido, long documento, Direccion direccion, long telefono, long celular,
			String correo, LocalDate nacimiento, LocalDate ingreso, String situacion, String lugar, String puesto,
			String iva, String twitter, String web, String instagram, String facebook, String linkedin,
			String tipoCliente) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.documento = documento;
		this.telefono = telefono;
		this.celular = celular;
		this.correo = correo;
		this.nacimiento = nacimiento;
		this.ingreso = ingreso;
		this.direccion = direccion;
		this.situacion = situacion;
		this.lugar = lugar;
		this.puesto = puesto;
		this.iva = iva;
		this.twitter = twitter;
		this.web = web;
		this.instagram = instagram;
		this.facebook = facebook;
		this.linkedin = linkedin;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Creaun cliente con los datos de un cliente especificado
	 * 
	 * @param nombre     nombre del cliente
	 * @param apellido   apellido del cliente
	 * @param documento  documento del cliente
	 * @param direccion  direccion del cliente
	 * @param telefono   telefono del cliente
	 * @param celular    celular del cliente
	 * @param correo     correo del cliente
	 * @param nacimiento nacimiento del cliente
	 * @param ingreso    ingreso del cliente
	 * @param situacion  situacion del cliente
	 * @param lugar      lugar del cliente
	 * @param puesto     puesto del cliente
	 * @param iva        iva del cliente
	 * @param twitter    twitter del cliente
	 * @param web        web del cliente
	 * @param instagram  instagram del cliente
	 * @param facebook   facebook del cliente
	 * @param linkedin   linkedin del cliente
	 */

	public Cliente(String nombre, String apellido, long documento, Direccion direccion, long telefono, long celular,
			String correo, LocalDate nacimiento, LocalDate ingreso, String situacion, String lugar, String puesto,
			String iva, String twitter, String web, String instagram, String facebook, String linkedin) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.documento = documento;
		this.telefono = telefono;
		this.celular = celular;
		this.correo = correo;
		this.nacimiento = nacimiento;
		this.ingreso = ingreso;
		this.direccion = direccion;
		this.situacion = situacion;
		this.lugar = lugar;
		this.puesto = puesto;
		this.iva = iva;
		this.twitter = twitter;
		this.web = web;
		this.instagram = instagram;
		this.facebook = facebook;
		this.linkedin = linkedin;

	}

	/**
	 * Crea un cliente con los datos especificados
	 * 
	 * @param nombre    nombre de la empresa
	 * @param cuit      cuit de la empresa
	 * @param direccion direccion de la empresa
	 * @param telefono  telefono de la empresa
	 * @param celular   celular de la empresa
	 * @param correo    correo de la empresa
	 * @param ingreso   ingreso fecha que se cargo la empresa al sistema
	 * @param iva       iva de la empresa
	 * @param twitter   twitter de la empresa
	 * @param web       web de la empresa
	 * @param instagram instagram de la empresa
	 * @param facebook  facebook de la empresa
	 * @param linkedin  linkedin de la empresa
	 */
	public Cliente(String razonSocial, long cuit, Direccion direccion, long telefono, long celular, String correo,
			LocalDate ingreso, String iva, String twitter, String web, String instagram, String facebook,
			String linkedin, String tipoCliente) {
		super();
		this.nombre = razonSocial;
		this.documento = cuit;
		this.telefono = telefono;
		this.celular = celular;
		this.correo = correo;
		this.ingreso = ingreso;
		this.direccion = direccion;
		this.iva = iva;
		this.twitter = twitter;
		this.web = web;
		this.instagram = instagram;
		this.facebook = facebook;
		this.linkedin = linkedin;
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Crea un cliente con los datos especificados
	 * 
	 * @param nombre    nombre de la empresa
	 * @param cuit      cuit de la empresa
	 * @param direccion direccion de la empresa
	 * @param telefono  telefono de la empresa
	 * @param celular   celular de la empresa
	 * @param correo    correo de la empresa
	 * @param ingreso   ingreso fecha que se cargo la empresa al sistema
	 * @param iva       iva de la empresa
	 * @param twitter   twitter de la empresa
	 * @param web       web de la empresa
	 * @param instagram instagram de la empresa
	 * @param facebook  facebook de la empresa
	 * @param linkedin  linkedin de la empresa
	 */
	public Cliente(String razonSocial, long cuit, Direccion direccion, long telefono, long celular, String correo,
			LocalDate ingreso, String iva, String twitter, String web, String instagram, String facebook,
			String linkedin) {
		super();
		this.nombre = razonSocial;
		this.documento = cuit;
		this.telefono = telefono;
		this.celular = celular;
		this.correo = correo;
		this.ingreso = ingreso;
		this.direccion = direccion;
		this.iva = iva;
		this.twitter = twitter;
		this.web = web;
		this.instagram = instagram;
		this.facebook = facebook;
		this.linkedin = linkedin;

	}

	/**
	 * Obtiene todos los datos de un cliente especifico
	 * 
	 * @param documento documento de un cliente a obtener todos los datos
	 */
	public Cliente(long documento) {
		String sql = "SELECT c.cliente_nombre,c.cliente_apellido,c.cliente_nacimiento,c.cliente_domicilio,"
				+ "c.cliente_manzana,c.cliente_monoblock,c.cliente_departamento,c.cliente_celular,"
				+ "c.cliente_telefono,c.cliente_correo,c.cliente_numero_abonado,c.cliente_fecha_alta,"
				+ "tt.tipo_trabajo_nombre,c.cliente_trabajo_puesto,c.cliente_trabajo_lugar,ti.tipo_iva_categoria,c.cliente_sitio_web,"
				+ "c.cliente_twitter,c.cliente_instagram,c.cliente_facebook,c.cliente_linkedin ,ci.ciudad_nombre"
				+ ", b.barrio_nombre FROM cliente c  INNER JOIN ciudad ci ON c.ciudad_id = ci.ciudad_id "
				+ "	LEFT JOIN barrio b ON c.barrio_id = b.barrio_id "
				+ " LEFT JOIN tipo_iva ti ON ti.tipo_iva_id = c.tipo_iva_id "
				+ " LEFT JOIN tipo_trabajo tt ON c.tipo_trabajo_id = tt.tipo_trabajo_id "
				+ " WHERE c.cliente_documento = ?";
		DataBase db = new DataBase();

		db.setQuery(sql);
		db.setParametro(1, documento);

		ResultSet rs = db.executeQuery();

		try {
			if (rs.next()) { // existe cliente
				this.direccion = new Direccion();
				this.documento = documento;
				this.nombre = rs.getString(1);
				this.apellido = rs.getString(2);

				if (rs.getDate(3) != null)
					this.nacimiento = rs.getDate(3).toLocalDate();

				direccion.setDomicilio(rs.getString(4));
				direccion.setManzana((rs.getInt(5) == 0) ? 0 : rs.getInt(5));
				direccion.setMonoblock((rs.getInt(6) == 0) ? 0 : rs.getInt(6));
				direccion.setDepartamento(rs.getString(7));
				this.celular = rs.getLong(8);
				this.telefono = (rs.getLong(9) == 0) ? 0 : rs.getLong(9);
				this.correo = rs.getString(10);
				this.numeroAbonado = rs.getInt(11);
				this.ingreso = rs.getDate(12).toLocalDate();
				this.situacion = rs.getString(13);
				this.puesto = rs.getString(14);

				this.lugar = rs.getString(15);

				this.iva = rs.getString(16);
				this.web = rs.getString(17);
				this.twitter = rs.getString(18);
				this.instagram = rs.getString(19);
				this.facebook = rs.getString(20);
				this.linkedin = rs.getString(21);
				direccion.setCiudad(rs.getString(22));
				direccion.setBarrio(rs.getString(23));

			} else { // no existe cliente
				this.nombre = null;
				this.apellido = null;
				this.documento = -1L;
				this.nacimiento = null;
				this.direccion = null;
				this.celular = -1L;
				this.telefono = -1L;
				this.correo = null;
				this.numeroAbonado = -1;
				this.ingreso = null;
				this.lugar = null;
				this.situacion = null;
				this.puesto = null;
				this.iva = null;
				this.web = null;
				this.twitter = null;
				this.instagram = null;
				this.facebook = null;
				this.linkedin = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Crea un cliente sin datos
	 */
	public Cliente() {
		this.nombre = null;
		this.apellido = null;
		this.documento = -1L;
		this.direccion = null;
		this.telefono = -1L;
		this.celular = -1L;
		this.correo = null;
		this.numeroAbonado = -1;
	}

	/**
	 * @return the situacion
	 */
	public String getSituacion() {
		return situacion;
	}

	/**
	 * @param situacion the situacion to set
	 */
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	/**
	 * @return the lugar
	 */
	public String getLugar() {
		return lugar;
	}

	/**
	 * @param lugar the lugar to set
	 */
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	/**
	 * @return the puesto
	 */
	public String getPuesto() {
		return puesto;
	}

	/**
	 * @param puesto the puesto to set
	 */
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	/**
	 * @return the iva
	 */
	public String getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(String iva) {
		this.iva = iva;
	}

	/**
	 * @return the twitter
	 */
	public String getTwitter() {
		return twitter;
	}

	/**
	 * @param twitter the twitter to set
	 */
	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	/**
	 * @return the web
	 */
	public String getWeb() {
		return web;
	}

	/**
	 * @param web the web to set
	 */
	public void setWeb(String web) {
		this.web = web;
	}

	/**
	 * @return the instagram
	 */
	public String getInstagram() {
		return instagram;
	}

	/**
	 * @param instagram the instagram to set
	 */
	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	/**
	 * @return the facebook
	 */
	public String getFacebook() {
		return facebook;
	}

	/**
	 * @param facebook the facebook to set
	 */
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	/**
	 * @return the linkedin
	 */
	public String getLinkedin() {
		return linkedin;
	}

	/**
	 * @param linkedin the linkedin to set
	 */
	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public LocalDate getIngreso() {
		return ingreso;
	}

	public void setIngreso(LocalDate ingreso) {
		this.ingreso = ingreso;
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

	public LocalDate getNacimiento() {
		return nacimiento;
	}

	public void setNacimiento(LocalDate nacimiento) {
		this.nacimiento = nacimiento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public long getDocumento() {
		return documento;
	}

	public void setDocumento(long documento) {
		this.documento = documento;
	}

	public long getTelefono() {
		return telefono;
	}

	public void setTelefono(long telefono) {
		this.telefono = telefono;
	}

	public long getCelular() {
		return celular;
	}

	public void setCelular(long celular) {
		this.celular = celular;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public int getNumeroAbonado() {
		return numeroAbonado;
	}

	public void setNumeroAbonado(int numeroAbonado) {
		this.numeroAbonado = numeroAbonado;
	}

	/**
	 * inserta un nuevo cliente en la base de datos
	 * 
	 * @return true si funciono o false si ocurrio un error
	 */
	public boolean insert() {

		int idBarrio = -1;
		int idCiudad = Direccion.getCiudadId(direccion.getCiudad());
		int idtipoCliente = getIdTipoCliente(getTipoCliente());
		int idSituacionLaboral = getIdTipoTrabajo(getSituacion());
		int idIva = getIdIvaCliente(getIva());
		asignarNumero(idCiudad);

		if (direccion.getBarrio() != null)
			idBarrio = Direccion.getBarrioId(direccion.getBarrio(), direccion.getCiudad());

		// consulta
		DataBase db = DataBase.getInstancia();

		String sql = "INSERT INTO cliente (cliente_documento, cliente_nombre, cliente_apellido, cliente_nacimiento, cliente_domicilio, cliente_manzana, cliente_monoblock, cliente_departamento, cliente_celular, cliente_telefono, cliente_correo, cliente_numero_abonado,"
				+ " cliente_fecha_alta, barrio_id, ciudad_id, tipo_trabajo_id, cliente_trabajo_lugar, cliente_trabajo_puesto, tipo_iva_id, cliente_sitio_web, cliente_twitter, cliente_instagram, cliente_facebook, cliente_linkedin, cliente_tipo_id, cliente_estado) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,CURRENT_DATE() , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 2)";

		db.setQuery(sql);

		db.setParametro(1, documento);
		db.setParametro(2, nombre);

		// VERIFICACION DE PARAMETROS NULOS
		if (apellido == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, apellido);

		if (nacimiento == null)
			db.setNull(4, Types.DATE);
		else
			db.setParametro(4, Date.valueOf(nacimiento));

		db.setParametro(5, direccion.getDomicilio());// no puede ser null por ende direccion != null

		if (direccion.getManzana() == -1)
			db.setNull(6, Types.INTEGER);
		else
			db.setParametro(6, direccion.getManzana());

		if (direccion.getMonoblock() == -1)
			db.setNull(7, Types.INTEGER);
		else
			db.setParametro(7, direccion.getMonoblock());

		if (direccion.getDepartamento() == null)
			db.setNull(8, Types.VARCHAR);
		else
			db.setParametro(8, direccion.getDepartamento());

		db.setParametro(9, celular); // no puede ser null

		if (telefono == -1L)
			db.setNull(10, Types.BIGINT);
		else
			db.setParametro(10, telefono);

		if (correo == null)
			db.setNull(11, Types.VARCHAR);
		else
			db.setParametro(11, correo);

		db.setParametro(12, numeroAbonado);

		if (idBarrio == -1)
			db.setNull(13, Types.INTEGER);
		else
			db.setParametro(13, idBarrio);

		db.setParametro(14, idCiudad);

		if (idSituacionLaboral == -1)
			db.setNull(15, Types.INTEGER);
		else
			db.setParametro(15, idSituacionLaboral);

		if (lugar == null)
			db.setNull(16, Types.VARCHAR);
		else
			db.setParametro(16, lugar);

		if (puesto == null)
			db.setNull(17, Types.VARCHAR);
		else
			db.setParametro(17, puesto);

		if (idIva == -1)
			db.setNull(18, Types.INTEGER);
		else
			db.setParametro(18, idIva);

		if (web == null)
			db.setNull(19, Types.VARCHAR);
		else
			db.setParametro(19, web);

		if (twitter == null)
			db.setNull(20, Types.VARCHAR);
		else
			db.setParametro(20, twitter);

		if (instagram == null)
			db.setNull(21, Types.VARCHAR);
		else
			db.setParametro(21, instagram);

		if (facebook == null)
			db.setNull(22, Types.VARCHAR);
		else
			db.setParametro(22, facebook);

		if (linkedin == null)
			db.setNull(23, Types.VARCHAR);
		else
			db.setParametro(23, linkedin);

		if (idtipoCliente == -1)
			db.setNull(24, Types.INTEGER);
		else
			db.setParametro(24, idtipoCliente);

		// Ejecuto el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;

	}

	/**
	 * @return the tipoCliente
	 */
	public String getTipoCliente() {
		return tipoCliente;
	}

	/**
	 * @param tipoCliente the tipoCliente to set
	 */
	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	/**
	 * Permite actualizar los datos de cliente con los datos especificados
	 * 
	 * @param documento documento del cliente a modificar los datoss
	 * @return true si funciono la actualizacion de datos o false si no funciono
	 */
	public boolean update(long documento) {
		int idBarrio = -1;
		int idCiudad = Direccion.getCiudadId(direccion.getCiudad());
		int idIva = getIdIvaCliente(getIva());
		int idTipoTrabajo = getIdTipoTrabajo(getSituacion());

		if (direccion.getBarrio() != null)
			idBarrio = Direccion.getBarrioId(direccion.getBarrio(), direccion.getCiudad());
		// Consulta db
		DataBase db = new DataBase();

		String sql = "UPDATE cliente SET cliente_nombre = ? ,cliente_apellido = ?,cliente_nacimiento = ?,"
				+ "cliente_domicilio = ?,cliente_manzana = ?,cliente_monoblock = ?,cliente_departamento = ?,cliente_celular = ?,cliente_telefono = ?,"
				+ "cliente_correo = ?,barrio_id = ?,ciudad_id = ?,tipo_trabajo_id = ?,cliente_trabajo_lugar = ?,"
				+ "cliente_trabajo_puesto = ?,tipo_iva_id = ?,cliente_sitio_web = ?,cliente_twitter = ?,cliente_instagram = ?,cliente_facebook = ?,cliente_linkedin = ?"
				+ " WHERE cliente_documento = ? ;";

		db.setQuery(sql);

		// verifica los parametros nulos

		// Parametros que no pueden ser null

		db.setParametro(1, nombre);

		if (apellido == null)
			db.setNull(2, Types.VARCHAR);
		else
			db.setParametro(2, apellido);

		if (nacimiento == null)
			db.setNull(3, Types.DATE);
		else
			db.setParametro(3, Date.valueOf(nacimiento));

		db.setParametro(4, direccion.getDomicilio());

		if (direccion.getManzana() == -1)
			db.setNull(5, Types.INTEGER);
		else
			db.setParametro(5, direccion.getManzana());

		if (direccion.getMonoblock() == -1)
			db.setNull(6, Types.INTEGER);
		else
			db.setParametro(6, direccion.getMonoblock());

		if (direccion.getDepartamento() == null)
			db.setNull(7, Types.VARCHAR);
		else
			db.setParametro(7, direccion.getDepartamento());

		db.setParametro(8, celular);

		if (telefono == -1L)
			db.setNull(9, Types.BIGINT);
		else
			db.setParametro(9, telefono);

		if (correo == null)
			db.setNull(10, Types.VARCHAR);
		else
			db.setParametro(10, correo);

		if (idBarrio == -1)
			db.setNull(11, Types.INTEGER);
		else
			db.setParametro(11, idBarrio);

		db.setParametro(12, idCiudad);

		if (idTipoTrabajo == -1)
			db.setNull(13, Types.INTEGER);
		else
			db.setParametro(13, idTipoTrabajo);

		if (lugar == null)
			db.setNull(14, Types.VARCHAR);
		else
			db.setParametro(14, lugar);

		if (puesto == null)
			db.setNull(15, Types.VARCHAR);
		else
			db.setParametro(15, puesto);

		if (idIva == -1)
			db.setNull(16, Types.INTEGER);
		else
			db.setParametro(16, idIva);

		if (web == null)
			db.setNull(17, Types.VARCHAR);
		else
			db.setParametro(17, web);

		if (twitter == null)
			db.setNull(18, Types.VARCHAR);
		else
			db.setParametro(18, twitter);

		if (instagram == null)
			db.setNull(19, Types.VARCHAR);
		else
			db.setParametro(19, instagram);

		if (facebook == null)
			db.setNull(20, Types.VARCHAR);
		else
			db.setParametro(20, facebook);

		if (linkedin == null)
			db.setNull(21, Types.VARCHAR);
		else
			db.setParametro(21, linkedin);

		db.setParametro(22, documento);

		// Ejecuto el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;

	}

	/**
	 * Llena el combo de estado de instalacion
	 * 
	 * @return el item
	 */
	public static ObservableList<String> llenarEstado() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("instalado", "pendiente");
		return items;
	}

	/**
	 * Llena el combo de estado de comunicacion
	 * 
	 * @return el item
	 */
	public static ObservableList<String> llenarEstadoCliente() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("Habilitado", "Deshabilitado");
		return items;
	}

	/**
	 * Llena el combo de situacion laboral
	 * 
	 * @return el item
	 */
	public static ObservableList<String> llenarSituacionLaboral() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("Monotributista", "Relacion Dependencia");
		return items;
	}

	/**
	 * Llena el combo deuda
	 * 
	 * @return el item
	 */
	public static ObservableList<String> llenarDeuda() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("1 MES", "2 MESES", "3 MESES");
		return items;
	}

	/**
	 * LLena el combo de iva del cliente
	 * 
	 * @return el item
	 */
	public static ObservableList<String> llenarIvaCliente() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT tipo_iva_categoria FROM tipo_iva ORDER BY tipo_iva_categoria";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {

			// agrega las ciudades
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * Recupera los tipos de cliente de la base de datos
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarTipoCliente() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT cliente_tipo_nombre FROM cliente_tipo ORDER BY cliente_tipo_nombre";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			// agrega las ciudades
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * Permite obtener el id de Item Iva
	 * 
	 * @param descripcion descripcion a buscar el id
	 * @return el id del item descripcion
	 */
	public int getIdIvaCliente(String trabajo_iva) {
		try {
			String sql = "SELECT tipo_iva_id FROM tipo_iva WHERE tipo_iva_categoria = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, trabajo_iva);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int idIvaCliente = rs.getInt(1);
				rs.close();
				return idIvaCliente;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permite obtener el id del tipo de trabajo del cliente
	 * 
	 * @param situacionLaboral situacion laboral del cliente
	 * @return retorna id del tipo de trabajo del cliente
	 */
	public int getIdTipoTrabajo(String situacionLaboral) {

		try {
			String sql = "SELECT tipo_trabajo_id FROM tipo_trabajo WHERE tipo_trabajo_nombre = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, situacionLaboral);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int idSituacionLaboral = rs.getInt(1);
				rs.close();
				return idSituacionLaboral;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permite obtener el id de un tipo de cliente en especifico
	 * 
	 * @param tipoCliente tipo de cliente a recuperar el id
	 * @return el id del cliente
	 */
	public static int getIdTipoCliente(String tipoCliente) {
		try {
			String sql = "SELECT cliente_tipo_id FROM cliente_tipo WHERE cliente_tipo_nombre = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, tipoCliente);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int id_tipoCliente = rs.getInt(1);
				rs.close();
				return id_tipoCliente;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Recupera las ciudades de la base de datos y las agrega al combo de ciudades
	 */
	public static ObservableList<String> llenarCiudades() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT ciudad_nombre FROM ciudad ORDER BY ciudad_nombre";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			items.add("");
			// agrega las ciudades
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * Permite cargar la ciudad en la que pertenece el cliente en el combobox
	 * 
	 * @param documento documento del cliente que se quiere ver en que ciudad
	 *                  pertenece
	 * 
	 * @return
	 */
	public static ObservableList<String> llenarCiudadCliente(long documento) {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT c.ciudad_nombre FROM ciudad c INNER JOIN cliente cl ON c.ciudad_id = cl.ciudad_id WHERE cl.cliente_documento = ?";

		db.setParametro(1, documento);
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			items.add("");
			// agrega las ciudades
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * Obtiene los barrios y los agrega al comboBarrio
	 */
	public static ObservableList<String> llenarBarrios() {

		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT barrio_nombre FROM barrio ORDER BY barrio_nombre";

		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			// agrega las ciudades
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return items;
	}

	/**
	 * Este da de baja un cliente. Cambia de habilitado (1) a estado (0) que es
	 * eliminado
	 * 
	 * @param documento documento del cliente a cambiar el estado
	 * @return true si funciono o false si no funciono
	 */
	public boolean bajaCliente(long documento) {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE cliente SET cliente_estado = '0' WHERE cliente_documento = ?;";

		db.setQuery(sql);
		db.setParametro(1, documento);
		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Cambia el estado de la instalacion a (3) que es instalacion cancelada
	 * 
	 * @param documento documento del cliente a cambiar el estado
	 * @return true si funciono o false si no funciono
	 */
	public boolean cancelarInstalacion(long documento) {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE cliente SET cliente_estado = '3' WHERE cliente_documento = ?;";

		db.setQuery(sql);
		db.setParametro(1, documento);
		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Cambia el estado de la instalacion a (1) que es instalacion finalizada
	 * 
	 * @param documento documento del cliente a cambiar el estado
	 * @return true si funciono o false si no funciono
	 */
	public boolean instalacionFinalizada() {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE cliente SET cliente_estado = '1' WHERE cliente_documento = ?;";

		db.setQuery(sql);
		db.setParametro(1, documento);
		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * asigna un numero de abonado al cliente
	 * 
	 * @param idCiudad de la ciudad en la que pertenece el cliente
	 */
	private void asignarNumero(int idCiudad) {
		// numero minimo para dicha ciudad
		numeroAbonado = idCiudad * 1000000 + 10000000;

		String sql = "SELECT MAX(cliente_numero_abonado) FROM cliente WHERE cliente_numero_abonado BETWEEN ? AND ?;";
		DataBase db = new DataBase();
		db.setQuery(sql);
		db.setParametro(1, numeroAbonado); // minimo
		db.setParametro(2, numeroAbonado + 1000000); // maximo

		ResultSet rs = db.executeQuery();
		try {
			if (rs.next())
				numeroAbonado = rs.getInt(1) + 1;

			rs.close();
			db.close();

			if (numeroAbonado == 1)
				numeroAbonado = idCiudad * 1000000 + 10000000;

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
