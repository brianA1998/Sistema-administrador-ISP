package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Direccion {
	private String domicilio, ciudad, provincia, barrio, departamento;
	private int manzana, monoblock, codigoPostal;

	public Direccion(String domicilio, String ciudad, String provincia) {
		super();
		this.domicilio = domicilio;
		this.ciudad = ciudad;
		this.provincia = provincia;
	}

	public Direccion(String domicilio, String ciudad, String barrio, String departamento, int monoblock, int manzana) {
		super();
		this.domicilio = domicilio;
		this.ciudad = ciudad;
		this.barrio = barrio;
		this.departamento = departamento;
		this.monoblock = monoblock;
		this.manzana = manzana;

	}

	public Direccion(String ciudad, String barrio) {
		super();

		this.ciudad = ciudad;
		this.barrio = barrio;
	}

	public Direccion(String ciudad, int codigoPostal, String provincia) {
		super();

		this.ciudad = ciudad;
		this.codigoPostal = codigoPostal;
		this.provincia = provincia;
	}

	public Direccion(String provincia) {
		super();

		this.provincia = provincia;

	}

	public Direccion() {
		this.domicilio = null;
		this.ciudad = null;
		this.barrio = null;
		this.departamento = null;
		this.monoblock = -1;
		this.manzana = -1;
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
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the monoblock
	 */
	public int getMonoblock() {
		return monoblock;
	}

	/**
	 * @param monoblock the monoblock to set
	 */
	public void setMonoblock(int monoblock) {
		this.monoblock = monoblock;
	}

	/**
	 * @return the manzana
	 */
	public int getManzana() {
		return manzana;
	}

	/**
	 * @param manzana the manzana to set
	 */
	public void setManzana(int manzana) {
		this.manzana = manzana;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public int getCodigoPostal() {
		return codigoPostal;
	}

	public void setCodigoPostal(int codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Direccion other = (Direccion) obj;
		if (ciudad == null) {
			if (other.ciudad != null)
				return false;
		} else if (!ciudad.equals(other.ciudad))
			return false;
		if (domicilio == null) {
			if (other.domicilio != null)
				return false;
		} else if (!domicilio.equals(other.domicilio))
			return false;
		if (provincia == null) {
			if (other.provincia != null)
				return false;
		} else if (!provincia.equals(other.provincia))
			return false;
		return true;
	}

	/**
	 * Permite insertar en la tabla de provincia el nombre de una provincia
	 * 
	 * @return retorna true si funciono la insercion o false si no funciono
	 */
	public boolean insertarProvincia() {
		// consulta
		DataBase db = new DataBase();
		String sql = "INSERT INTO provincia (provincia_nombre) VALUES (?);";
		db.setQuery(sql);
		db.setParametro(1, provincia);

		// ejecuta el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Permite insertar una ciudad en la tabla de ciudad
	 * 
	 * @return true si funciono la insercion o false si no funciono
	 */
	public boolean insertarCiudad() {

		int idProvincia = getProvinciaId(provincia);
		// consulta
		DataBase db = new DataBase();
		String sql = "INSERT INTO ciudad (ciudad_nombre, ciudad_codigo_postal, provincia_id) VALUES (?,?,?);";
		db.setQuery(sql);
		db.setParametro(1, ciudad);
		db.setParametro(2, codigoPostal);
		db.setParametro(3, idProvincia);

		// ejecuta el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * devuelve el id de la provincia
	 * 
	 * @param provincia provincia buscada
	 * @return id de la provincia
	 */
	public static int getProvinciaId(String provincia) {
		String sql = "SELECT provincia_id FROM provincia WHERE provincia_nombre=?";
		try {
			DataBase db = new DataBase();
			db.setQuery(sql);
			db.setParametro(1, provincia);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int codigo = rs.getInt(1);
				rs.close();
				return codigo;
			}
		} catch (SQLException e) {
			System.out.println(e);
		}
		return -1;
	}

	/**
	 * devuelve el id de la ciudad especificada
	 * 
	 * @param ciudad    ciudad buscada
	 * @param provincia provincia
	 * @return id de la ciudad
	 */
	public static int getCiudadId(String ciudad, String provincia) {
		int id = getProvinciaId(provincia);
		try {
			String sql = "SELECT ciudad_id FROM ciudad " + "WHERE ciudad_nombre=? AND provincia_id=?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, ciudad);
			db.setParametro(2, id);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int ciudad_id = rs.getInt(1);
				rs.close();
				return ciudad_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permite obtener el codigo postal de una determinada ciudad
	 * 
	 * @param ciudad ciudad a obtener el codigo postal
	 * @return retorna el codigo postal de la ciudad
	 */
	public static int getCodigoPostal(String ciudad) {
		int id = getCiudadId(ciudad);
		try {
			String sql = "SELECT ciudad_codigo_postal FROM ciudad WHERE ciudad_id = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, id);

			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int codigoPostal = rs.getInt(1);
				rs.close();
				return codigoPostal;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permite obtener la provincia apartir de una ciudad
	 * 
	 * @param ciudad ciudad a obtener la provincia
	 * @return retorna la provincia
	 */
	public static String getProvincia(String ciudad) {
		int id = getCiudadId(ciudad);
		try {
			String sql = "SELECT P.provincia_nombre FROM provincia P INNER JOIN ciudad C ON P.provincia_id = C.provincia_id WHERE C.ciudad_id = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, id);

			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				String provincia = rs.getString(1);
				rs.close();
				return provincia;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Devuelve el id del barrio especificado que se encuentra en determinada ciudad
	 * 
	 * @param barrio barrio buscado
	 * @param ciudad ciudad
	 * @return id de barrio
	 */
	public static int getBarrioId(String barrio, String ciudad) {
		int idCiudad = getCiudadId(ciudad);
		try {
			String sql = "SELECT barrio_id FROM barrio WHERE barrio_nombre = ? AND ciudad_id = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, barrio);
			db.setParametro(2, idCiudad);
			ResultSet rs = db.executeQuery();
			System.out.println("");
			if (rs.next()) {
				int barrio_id = rs.getInt(1);
				rs.close();
				db.close();
				return barrio_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * devuelve el id de la ciudad especificada
	 * 
	 * @param ciudad ciudad buscada
	 * 
	 * @return id de la ciudad
	 */
	public static int getCiudadId(String ciudad) {
		try {
			String sql = "SELECT ciudad_id FROM ciudad WHERE ciudad_nombre=?";
			DataBase db = new DataBase();
			db.setQuery(sql);
			db.setParametro(1, ciudad);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int ciudad_id = rs.getInt(1);
				rs.close();
				return ciudad_id;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Obtiene las provincias y los agrega al comboProvincia
	 */
	public static ObservableList<String> llenarProvincias() {

		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT provincia_nombre FROM provincia ORDER BY provincia_nombre";

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
	 * crea un modelo con todas las ciudades de la provincia elejida
	 * 
	 * @return devuelve un modelo de combo de ciudades
	 */
	public static DefaultComboBoxModel<String> llenarComboCiudad(String provincia) {
		DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<String>();
		DataBase db = DataBase.getInstancia();

		String sql = "SELECT c.ciudad_nombre " + "FROM ciudad c " + "INNER JOIN provincia p "
				+ "ON p.provincia_id=c.provincia_id " + "WHERE p.provincia_nombre=? " + "GROUP BY c.ciudad_nombre "
				+ "ORDER BY c.ciudad_nombre";

		db.setQuery(sql);
		db.setParametro(1, provincia);
		ResultSet rs = db.executeQuery();
		modelo.addElement("");

		try {
			while (rs.next()) {
				modelo.addElement(rs.getString(1));
			}
		} catch (SQLException e) {
			System.out.println(e);
		}

		return modelo;
	}

	/**
	 * Recupera los barrios dependiendo de la ciudad
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarBarrioFiltrado(int idCiudad) {

		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT b.barrio_nombre FROM barrio b INNER JOIN ciudad c ON b.ciudad_id = c.ciudad_id \r\n"
				+ "INNER JOIN provincia p ON c.provincia_id = p.provincia_id \r\n" + "WHERE c.ciudad_id = ?";
		db.setQuery(sql);
		db.setParametro(1, idCiudad);
		ResultSet rs = db.executeQuery();

		try {

			// agrega los barrios
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;

	}

}
