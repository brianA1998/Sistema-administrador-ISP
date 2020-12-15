package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Servicio {
	private String nombre;
	private String codigo;
	private String rubro;
	private String subida;
	private String bajada;
	private String tipoServicio;
	private String tipoMoneda;
	private float iva;
	private float monto;

	/**
	 * Crea un servicio con los datos especificados
	 * 
	 * @param nombre       nombre del servicio
	 * @param codigo       codigo del mikrotik
	 * @param rubro        rubro que pertenece el servicio
	 * @param subida       subida del cliente
	 * @param bajada       bajada del cliente
	 * @param tipoServicio tipo de servicio
	 * @param tipoMoneda   tipo de moneda con la que se va a cobrar el servicio
	 * @param iva          iva del servicio
	 */
	public Servicio(String nombre, String subida, String bajada, String tipoServicio, String tipoMoneda, float iva,
			float monto, String codigo, String rubro) {
		super();
		this.nombre = nombre;
		this.codigo = codigo;
		this.rubro = rubro;
		this.subida = subida;
		this.bajada = bajada;
		this.tipoServicio = tipoServicio;
		this.tipoMoneda = tipoMoneda;
		this.iva = iva;
		this.monto = monto;
	}

	/**
	 * @return the monto
	 */
	public float getMonto() {
		return monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setMonto(float monto) {
		this.monto = monto;
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
	 * @return the subida
	 */
	public String getSubida() {
		return subida;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the rubro
	 */
	public String getRubro() {
		return rubro;
	}

	/**
	 * @param rubro the rubro to set
	 */
	public void setRubro(String rubro) {
		this.rubro = rubro;
	}

	/**
	 * @param subida the subida to set
	 */
	public void setSubida(String subida) {
		this.subida = subida;
	}

	/**
	 * @return the bajada
	 */
	public String getBajada() {
		return bajada;
	}

	/**
	 * @param bajada the bajada to set
	 */
	public void setBajada(String bajada) {
		this.bajada = bajada;
	}

	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}

	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	/**
	 * @return the tipoMoneda
	 */
	public String getTipoMoneda() {
		return tipoMoneda;
	}

	/**
	 * @param tipoMoneda the tipoMoneda to set
	 */
	public void setTipoMoneda(String tipoMoneda) {
		this.tipoMoneda = tipoMoneda;
	}

	/**
	 * @return the iva
	 */
	public float getIva() {
		return iva;
	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(int iva) {
		this.iva = iva;
	}

	/**
	 * Crea un servicio sin datos
	 */
	public Servicio() {
		this.nombre = null;
		this.rubro = null;
		this.codigo = null;

		this.subida = null;
		this.bajada = null;
		this.tipoServicio = null;
		this.tipoMoneda = null;
		this.iva = -1;
	}

	/**
	 * Carga en la base de datos un nuevo servicio
	 * 
	 * @return true si se funciono o false si ocurrio un error en la insercion de
	 *         datos
	 */
	public boolean insertar() {
		int idTipoMoneda = getIdTipoMoneda(getTipoMoneda());
		int idTipoServicio = getIdTipoServicio(getTipoServicio());
		int idRubro = getIdRubro(getRubro());

		// consulta
		DataBase db = new DataBase();
		String sql = "INSERT INTO servicio (servicio_nombre, servicio_subida,"
				+ " servicio_bajada, servicio_monto, servicio_iva, servicio_estado, servicio_codigo,"
				+ "  tipo_moneda_id, tipo_servicio_id , rubro_id) VALUES (?,?,?,?,?,1,?,?,?,?);";

		db.setQuery(sql);
		db.setParametro(1, nombre);

		if (subida == null)
			db.setNull(2, Types.VARCHAR);
		else
			db.setParametro(2, subida);

		if (bajada == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, bajada);

		db.setParametro(4, monto);
		db.setParametro(5, iva);
		db.setParametro(6, codigo);
		db.setParametro(7, idTipoMoneda);
		db.setParametro(8, idTipoServicio);
		db.setParametro(9, idRubro);

		// Ejecuto el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;

	}

	/**
	 * Actualiza los datos de un serivicio en la base de datos
	 * 
	 * @param id_tiposervicio id del servicio a modificar
	 * @return true si funciono el update o false si no funciono
	 */
	public boolean update(int id_servicio) {
		int idTipoMoneda = getIdTipoMoneda(getTipoMoneda());
		int idTipoServicio = getIdTipoServicio(getTipoServicio());
		int idRubro = getIdRubro(getRubro());

		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE servicio SET servicio_nombre = ?, servicio_subida = ?,"
				+ " servicio_bajada = ?, servicio_monto = ?, servicio_iva = ?, servicio_codigo = ?,"
				+ " tipo_moneda_id = ?, tipo_servicio_id = ?, rubro_id = ?  WHERE servicio_id = ?;";

		db.setQuery(sql);
		// Estos parametros que no pueden ser null
		db.setParametro(1, nombre);

		db.setParametro(4, monto);
		db.setParametro(5, iva);
		db.setParametro(6, codigo);
		db.setParametro(7, idTipoMoneda);
		db.setParametro(8, idTipoServicio);
		db.setParametro(9, idRubro);
		db.setParametro(10, id_servicio);

		if (subida == null)
			db.setNull(2, Types.VARCHAR);
		else
			db.setParametro(2, subida);

		if (bajada == null)
			db.setNull(3, Types.VARCHAR);
		else
			db.setParametro(3, bajada);

		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Obtener todos los datos de un servicio por un nombre especifico
	 * 
	 * @param nombreServicio para obtener los datos
	 * @return
	 */
	public static Servicio loadServicio(String nombreServicio) {
		Servicio servicio = new Servicio();
		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();

		String sql = "SELECT s.servicio_nombre, s.servicio_iva,s.servicio_monto,m.tipo_moneda_codigo FROM tipo_moneda m INNER JOIN servicio s ON s.tipo_moneda_id = m.tipo_moneda_id "
				+ " WHERE s.servicio_nombre = ?;";
		db.setQuery(sql);
		db.setParametro(1, nombreServicio);

		ResultSet rs = db.executeQuery();

		try {
			// agrega las ciudades
			while (rs.next()) {
				servicio.nombre = rs.getString(1);
				servicio.iva = rs.getFloat(2);
				servicio.monto = rs.getFloat(3);
				servicio.tipoMoneda = rs.getString(4);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return servicio;

	}

	/**
	 * @param iva the iva to set
	 */
	public void setIva(float iva) {
		this.iva = iva;
	}

	/**
	 * devuelve el id del tipo de moneda
	 * 
	 * @param tipo moneda
	 * @return id del tipo de moneda
	 */
	public static int getIdTipoMoneda(String tipoMoneda) {
		try {
			String sql = "SELECT tipo_moneda_id FROM tipo_moneda WHERE tipo_moneda_codigo=?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, tipoMoneda);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int tipo_moneda_id = rs.getInt(1);
				rs.close();
				return tipo_moneda_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * devuelve el id del servicio
	 * 
	 * @param tipo servicio
	 * @return id del tipo de servicio
	 */
	public static int getIdTipoServicio(String tipoServicio) {
		try {
			String sql = "SELECT tipo_servicio_id FROM tipo_servicio WHERE tipo_servicio_nombre=?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, tipoServicio);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int tipo_servicio_id = rs.getInt(1);
				rs.close();
				return tipo_servicio_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * devuelve el id del rubro
	 * 
	 * @param nombreRubro nombre del rubro
	 * @return id del rubro
	 */
	public static int getIdRubro(String nombreRubro) {
		try {
			String sql = "SELECT rubro_id FROM rubro_servicio WHERE rubro_nombre=?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, nombreRubro);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int rubro_id = rs.getInt(1);
				rs.close();
				return rubro_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Este metodo cambia el estado de habilitado (1) a estado (0) que es bloqueado
	 * 
	 * @param nombreServicio nombre del servicio a cambiar el estado
	 * @return true si funciono o false si no funciono
	 */
	public boolean delete() {
		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE servicio SET servicio_estado = 0 WHERE  servicio_nombre = ?";

		db.setQuery(sql);
		db.setParametro(1, nombre);
		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Recupera de la bd el id del servicio
	 * 
	 * @param nombreServicio nombre del servicio
	 * @return id del servicio
	 */
	public int getIdServicio() {
		try {
			String sql = "SELECT servicio_id FROM servicio WHERE servicio_nombre=?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, nombre);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int servicio_id = rs.getInt(1);
				rs.close();
				return servicio_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	

	/**
	 * Recupera el nombre de servicio de la base de datos
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarServicio() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT servicio_nombre FROM servicio ORDER BY servicio_nombre";
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
	 * Recupera el nombre de servicio de la base de datos
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarTipoServicio() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT tipo_servicio_nombre FROM tipo_servicio ORDER BY tipo_servicio_nombre";
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
	 * Recupera los codigos de las monedas de la base de datos y las agrega al combo
	 * de tipo moneda
	 */
	public static ObservableList<String> llenarCodigoMoneda() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT tipo_moneda_codigo FROM tipo_moneda ORDER BY tipo_moneda_codigo";
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
	 * Agrega los ivas al combo iva
	 */
	public static ObservableList<String> llenarIva() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("10,5 %", "21 %", "27 %");
		return items;
	}

	/**
	 * 
	 * @return
	 */
	public static ObservableList<String> llenarCuotas() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("1", "2", "3", "6", "8", "12");
		return items;
	}

	/**
	 * LLena el combo del limite maximo de mikrotik
	 * 
	 * @return
	 */
	public static ObservableList<String> limiteMaximo() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("Kb", "Mb");
		return items;
	}

	/**
	 * Recupera los rubros de la base de datos y los agrega al combo de Rubro
	 */
	public static ObservableList<String> llenarRubro() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		// recupera todos los rubros desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT rubro_nombre FROM rubro_servicio ORDER BY rubro_nombre";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			// agrega los rubros
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * Recupera el nombre de servicio dependiendo del tipo de cliente de la base de
	 * datos
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarServicioFiltrado(String tipo) {

		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT s.servicio_nombre FROM servicio s INNER JOIN tipo_servicio tp ON tp.tipo_servicio_id = s.tipo_servicio_id WHERE tp.tipo_servicio_nombre = ?";
		db.setQuery(sql);
		db.setParametro(1, tipo);
		ResultSet rs = db.executeQuery();

		try {
			// agrega los servicios
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;

	}

}
