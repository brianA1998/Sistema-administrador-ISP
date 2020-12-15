package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Plantilla {
	private String titulo;
	private String mensaje;
	private int idPlantilla;
	private int estadoCorreo;
	private Cliente cliente;
	private LocalDate fechaEnvio;

	/**
	 * Crea una plantilla con los datos especificados
	 * 
	 * @param mensaje mensaje de la plantilla
	 * @param titulo  titulo de la plantilla
	 */
	public Plantilla(String mensaje, String titulo) {
		super();
		this.mensaje = mensaje;
		this.titulo = titulo;
	}

	/**
	 * Crea una plantilla sin datos
	 */
	public Plantilla() {
		this.mensaje = null;
		this.titulo = null;
	}

	/**
	 * Permite insertar los datos de una plantilla a la base de datos
	 * 
	 * @param mensajePlantilla mensaje es el mensaje que se va a colocar en la
	 *                         plantilla
	 * @param tituloPlantilla  titulo es el titulo que se va a colocar en la
	 *                         plantilla
	 * @return true si se inserto correctamente en la base de datos o false si fallo
	 *         la insercion
	 */
	public boolean insertarPlantilla(String mensajePlantilla, String tituloPlantilla) {
		// consulta
		DataBase db = new DataBase();
		String sql = "INSERT INTO plantilla_correo (plantilla_correo_titulo, plantilla_correo_mensaje) VALUES (?,?);";
		db.setQuery(sql);
		db.setParametro(1, tituloPlantilla);
		db.setParametro(2, mensajePlantilla);

		// Ejecuto el insert
		boolean resultado = db.execute();
		db.close();
		return resultado;
	}

	/**
	 * Permite actualizar los datos de una plantilla
	 * 
	 * @return true si funciono o false si no funciono
	 */
	public boolean update() {

		// Consulta db
		DataBase db = new DataBase();
		String sql = "UPDATE plantilla_correo SET plantilla_correo_titulo = ?, plantilla_correo_mensaje = ? WHERE plantilla_correo_id = ?;";

		db.setQuery(sql);
		// Estos parametros que no pueden ser null
		db.setParametro(1, titulo);

		db.setParametro(2, mensaje);
		db.setParametro(3, idPlantilla);

		// ejecuta el update
		boolean resultado = db.execute();
		db.close();
		return resultado;

	}

	/**
	 * Recupera de la bd el id de la plantilla
	 * 
	 * @return id de una plantilla o -1 si falla la consulta
	 */
	public int getPlantillaId() {
		try {

			String sql = "SELECT plantilla_correo_id FROM plantilla_correo WHERE plantilla_correo_titulo = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, titulo);

			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				int idPlantilla = rs.getInt(1);

				rs.close();
				return idPlantilla;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Recupera las plantillas de la base de datos y llenar el combo
	 */
	public static ObservableList<String> llenarPlantillas() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT plantilla_correo_titulo FROM plantilla_correo ORDER BY plantilla_correo_titulo";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			items.add("");
			// agrega las plantillas
			while (rs.next())
				items.add(rs.getString(1));

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}

	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * @return the idPlantilla
	 */
	public int getIdPlantilla() {
		return idPlantilla;
	}

	/**
	 * @param idPlantilla the idPlantilla to set
	 */
	public void setIdPlantilla(int idPlantilla) {
		this.idPlantilla = idPlantilla;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the estadoCorreo
	 */
	public int getEstadoCorreo() {
		return estadoCorreo;
	}

	/**
	 * @param estadoCorreo the estadoCorreo to set
	 */
	public void setEstadoCorreo(int estadoCorreo) {
		this.estadoCorreo = estadoCorreo;
	}

	/**
	 * @return the cliente
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param cliente the cliente to set
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * @return the fechaEnvio
	 */
	public LocalDate getFechaEnvio() {
		return fechaEnvio;
	}

	/**
	 * @param fechaEnvio the fechaEnvio to set
	 */
	public void setFechaEnvio(LocalDate fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

}
