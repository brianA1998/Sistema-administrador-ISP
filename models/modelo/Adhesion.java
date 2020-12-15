package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Adhesion {
	private Cliente cliente;
	private Servicio servicio;
	private Item item;

	/**
	 * crea una dhesion sin datos
	 */
	public Adhesion() {
		this.cliente = null;
		this.servicio = null;
		this.item = null;
	}

	/**
	 * crea una adhesion con los datos especiicados
	 * 
	 * @param cliente
	 * @param servicio
	 * @param item
	 */
	public Adhesion(Cliente cliente, Servicio servicio, Item item) {
		this.cliente = cliente;
		this.servicio = servicio;
		this.item = item;
	}

	/**
	 * Inserta un item vario en la tabla de item varios
	 */
	public void insertarItemVarios() {
		if (!item.insertarItemVarios()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fallo la inserción del item");
			alert.showAndWait();
			return;
		}
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
	 * @return the servicio
	 */
	public Servicio getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the item
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * @return the monto
	 */
	public float getItemMonto() {
		return item.monto;
	}

	/**
	 * @param monto the monto to set
	 */
	public void setItemMonto(float monto) {
		item.monto = monto;
	}

	/**
	 * @return the descripcion
	 */
	public String getItemDescripcion() {
		return item.descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setItemDescripcion(String descripcion) {
		item.descripcion = descripcion;
	}

	/**
	 * @return the moneda
	 */
	public String getItemMoneda() {
		return item.moneda;
	}

	/**
	 * @param moneda the moneda to set
	 */
	public void setItemMoneda(String moneda) {
		item.moneda = moneda;
	}

	/**
	 * @return the cuotasRestantes
	 */
	public int getItemCuotasRestantes() {
		return item.cuotasRestantes;
	}

	/**
	 * @param cuotasRestantes the cuotasRestantes to set
	 */
	public void setItemCuotasRestantes(int cuotasRestantes) {
		item.cuotasRestantes = cuotasRestantes;
	}

	/**
	 * @return the cuotas
	 */
	public int getItemCuotas() {
		return item.cuotas;
	}

	/**
	 * @param cuotas the cuotas to set
	 */
	public void setItemCuotas(int cuotas) {
		item.cuotas = cuotas;
	}

	public float getItemIva() {
		return item.iva;
	}

	public void setItemIva(float iva) {
		item.iva = iva;
	}

	public void setItem(float monto, String descripcion, String moneda, int cuotasRestantes, int cuotas, float iva) {
		this.item = new Item(monto, descripcion, moneda, cuotasRestantes, cuotas, iva);
	}

	public void setItem(float monto, String descripcion, String moneda, float iva) {
		this.item = new Item(monto, descripcion, moneda, iva);
	}

	public boolean insert() {
		if (servicio == null) {
			return item.insertar();
		} else {

			int id_servicio = servicio.getIdServicio();
			DataBase db = DataBase.getInstancia();
			String sql = "INSERT INTO adhesion (cliente_documento, servicio_id, adhesion_estado) VALUES (?, ?, 1);";
			db.setQuery(sql);
			db.setParametro(1, cliente.getDocumento());
			db.setParametro(2, id_servicio);

			// Ejecuto el insert
			boolean resultado = db.execute();
			db.close();
			return resultado;

		}
	}

	private class Item {
		private float monto;
		private String descripcion;
		private String moneda;
		private int cuotasRestantes;
		private int cuotas;
		private float iva;

		public Item(float monto, String descripcion, String moneda, int cuotasRestantes, int cuotas, float iva) {
			super();
			this.monto = monto;
			this.descripcion = descripcion;
			this.moneda = moneda;
			this.cuotasRestantes = cuotasRestantes;
			this.cuotas = cuotas;
			this.iva = iva;
		}

		public Item(float monto, String descripcion, String moneda, float iva) {
			super();
			this.monto = monto;
			this.descripcion = descripcion;
			this.moneda = moneda;

			this.iva = iva;
		}

		/**
		 * inserta un item manual a la base de datos
		 * 
		 * @return retona false si falla en la insercion o true si funciona
		 */
		public boolean insertar() {
			int idTipoMoneda = getIdTipoMoneda(getMoneda());
			int idItemDescripcion = getIdItemDescripcion(getItemDescripcion());

			// insertar en tabla item adhesion
			// consulta
			DataBase db = DataBase.getInstancia();
			String sql = "INSERT INTO item_adhesion (cliente_documento, "
					+ "item_adhesion_estado, item_adhesion_moneda, item_adhesion_cuotas_restantes,"
					+ " item_adhesion_cuotas_totales,item_descripcion_id) VALUES (?,1,?,?,?,?);";
			db.setQuery(sql);
			db.setParametro(1, cliente.getDocumento());

			db.setParametro(2, idTipoMoneda);
			db.setParametro(3, cuotasRestantes);
			db.setParametro(4, cuotas);

			db.setParametro(5, idItemDescripcion);

			// Ejecuto el insert
			boolean resultado = db.execute();
			db.close();
			return resultado;

		}

		/**
		 * Permite insertar en la tabla item Descripcion
		 * 
		 * @return true si funciona o false si no funciono la insercion
		 */
		public boolean insertarItemVarios() {

			int idTipoMoneda = getIdTipoMoneda(getMoneda());
			// insertar en tabla item descripcion
			// consulta
			DataBase db = DataBase.getInstancia();
			String sql = "INSERT INTO item_descripcion (item_descripcion_moneda, item_descripcion_iva, item_descripcion_detalle, item_descripcion_monto)"
					+ " VALUES (?,?,?,?);";
			db.setQuery(sql);
			db.setParametro(1, idTipoMoneda);

			db.setParametro(2, iva);
			db.setParametro(3, descripcion);
			db.setParametro(4, monto);

			// Ejecuto el insert
			boolean resultado = db.execute();
			db.close();
			return resultado;
		}

		/**
		 * @return the moneda
		 */
		public String getMoneda() {
			return moneda;
		}

		/**
		 * devuelve el id del tipo de moneda
		 * 
		 * @param tipo moneda
		 * @return id del tipo de moneda
		 */
		public int getIdTipoMoneda(String tipoMoneda) {
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
		 * Permite obtener el id de Item Descripcion
		 * 
		 * @param descripcion descripcion a buscar el id
		 * @return el id del item descripcion
		 */
		public int getIdItemDescripcion(String descripcion) {
			try {
				String sql = "SELECT item_descripcion_id FROM item_descripcion WHERE item_descripcion_detalle=?";
				DataBase db = DataBase.getInstancia();
				db.setQuery(sql);
				db.setParametro(1, descripcion);
				ResultSet rs = db.executeQuery();
				if (rs.next()) {
					int id_item_descripcion = rs.getInt(1);
					rs.close();
					return id_item_descripcion;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return -1;
		}

	}

	/**
	 * Permite obtener el iva de un item descripcion especifico
	 * 
	 * @param descripcion descripcion a obtener el iva
	 * @return retorna el iva si funciona o -1 si no funciono la consulta
	 */
	public static float getIvaItem(String descripcion) {
		try {
			String sql = "SELECT item_descripcion_iva FROM item_descripcion WHERE item_descripcion_detalle = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, descripcion);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				float ivaItem = rs.getFloat(1);
				rs.close();
				return ivaItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Permite obtener el codigo de la moneda de un item descripcion en especifico
	 * 
	 * @param descripcion descripcion a obtener el codigo de la moneda
	 * @return retorna null si no funciona la consulta o retorna el codigo de la
	 *         moneda si funciono
	 */
	public static String getCodigoMoneda(String descripcion) {
		try {
			String sql = "SELECT tm.tipo_moneda_codigo FROM tipo_moneda tm \r\n"
					+ "INNER JOIN item_descripcion id ON tm.tipo_moneda_id = id.item_descripcion_moneda WHERE id.item_descripcion_detalle = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, descripcion);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				String codigoMoneda = rs.getString(1);
				rs.close();
				return codigoMoneda;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Obtiene el monto de un item adhesion determinado
	 */
	public static float getMontoItem(String descripcion) {
		try {
			String sql = "SELECT item_descripcion_monto FROM item_descripcion WHERE item_descripcion_detalle = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, descripcion);
			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				float montoItem = rs.getFloat(1);
				rs.close();
				return montoItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Recupera la descripcion de un item de la base de datos
	 * 
	 * @return devuelve los items de un combo
	 */
	public static ObservableList<String> llenarItemDescripcion() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT item_descripcion_detalle FROM item_descripcion ORDER BY item_descripcion_detalle";
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

}
