
package comunicacion;

import java.sql.ResultSet;

import controlador.Validar;
import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import modelo.Cliente;
import modelo.Direccion;
import modelo.Plantilla;

public class EnvioCorreo {

	@FXML
	private ComboBox<String> comboEstado;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private ComboBox<String> comboDeuda;

	@FXML
	private TextField txtNumeroAbonado;

	@FXML
	private Button btnEnviar;

	@FXML
	private ComboBox<String> comboPlantillas;

	private String mensaje;

	@FXML
	void enviarCorreo(ActionEvent event) throws Exception {

		Plantilla plantilla = new Plantilla();
		plantilla.setTitulo(getSelectedItem(comboPlantillas));
		mensaje = getMensajePlantilla(plantilla.getTitulo());
		plantilla.setMensaje(mensaje);

		Cliente correosClientes[] = consultaFiltrada();

		JavaMail mail = new JavaMail();
		mail.mandarCorreo(correosClientes, plantilla);
	}

	@FXML
	void initialize() {
		// llenamos los combos
		comboCiudad.setItems(Cliente.llenarCiudades());
		comboEstado.setItems(Cliente.llenarEstadoCliente());
		comboDeuda.setItems(Cliente.llenarDeuda());
		// comboDeuda.setDisable(true);
		txtNumeroAbonado.setOnKeyTyped(Validar.numbers());
		comboDeuda.setItems(llenarComboDeuda());
		comboPlantillas.setItems(Plantilla.llenarPlantillas());
	}

	/**
	 * Permite llenar el combo deuda
	 * 
	 * @return item de combo deuda
	 */
	public ObservableList<String> llenarComboDeuda() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("","Al dia", "Moroso");
		return items;
	}

	/**
	 * Permite obtener el mensaje de una plantilla determinada
	 * 
	 * @param titulo titulo a obtener el mensaje
	 * @return el mensaje de la plantilla o null si fallo la consulta
	 */
	private String getMensajePlantilla(String titulo) {

		try {
			String sql = "SELECT plantilla_correo_mensaje FROM plantilla_correo WHERE plantilla_correo_titulo = ?";
			DataBase db = DataBase.getInstancia();
			db.setQuery(sql);
			db.setParametro(1, titulo);

			ResultSet rs = db.executeQuery();
			if (rs.next()) {
				String mensajePlantilla = rs.getString(1);

				rs.close();

				return mensajePlantilla;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Permite retornar el estado seleccionado del combo estado
	 * 
	 * @return estado seleccionado del combo estado
	 */
	private int getEstado() {
		int estado = comboEstado.getSelectionModel().getSelectedIndex();
		if (estado == 0)
			return 1;
		else
			return 0;
	}

	/**
	 * Permite obtener el id de la ciudad
	 * 
	 * @return id de la ciudad o -1 si no se selecciono nada en el combo
	 */
	private int getCiudad() {
		String ciudad = getSelectedItem(comboCiudad);
		int ciudad_id = Direccion.getCiudadId(ciudad);
		return ciudad_id;
	}

	/**
	 * obtiene el elemento seleccionado de un combo
	 * 
	 * @param combo combo a sacar los datos
	 * @return item seleccionado
	 */
	private String getSelectedItem(ComboBox<String> combo) {
		String item = combo.getSelectionModel().getSelectedItem();
		return (item != null && item.length() > 0) ? item : null;
	}

	/**
	 * Realiza consulta dinamica dependiendo de los combo Box o del textfield
	 * 
	 * @return retorna los correos de los clientes o el correo de una persona
	 *         especifica
	 */
	private Cliente[] consultaFiltrada() {
		int numeroAbonado = getInt(txtNumeroAbonado.getText());
		// String[] clientes;
		if (numeroAbonado == -1) {
			try {
				// int j = 2;

				DataBase db = new DataBase();
				String sql = "SELECT cliente_correo,cliente_documento,cliente_nombre FROM cliente WHERE cliente_estado = ?";

				if (getCiudad() != -1)
					sql += " AND ciudad_id=?";

				db.setQuery(sql);
				db.setParametro(1, getEstado());

				if (getCiudad() != -1)
					db.setParametro(2, getCiudad());

				ResultSet rs = db.executeQuery();

				rs.last();
				Cliente[] clientes = new Cliente[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					clientes[i] = new Cliente();
					clientes[i].setCorreo(rs.getString(1));
					clientes[i].setDocumento(rs.getLong(2));
					clientes[i].setNombre(rs.getString(3));
				}
				rs.close();
				return clientes;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		} else {
			try {

				DataBase db = new DataBase();
				String sql = "SELECT cliente_correo,cliente_documento,cliente_nombre FROM cliente WHERE cliente_numero_abonado = ? ";

				db.setQuery(sql);
				db.setParametro(1, numeroAbonado);
				ResultSet rs = db.executeQuery();

				rs.last();
				Cliente[] clientes = new Cliente[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					clientes[i] = new Cliente();
					clientes[i].setCorreo(rs.getString(1));
					clientes[i].setDocumento(rs.getLong(2));
					clientes[i].setNombre(rs.getString(3));
				}

				rs.close();
				return clientes;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}
	}

	/**
	 * Convierte numero de String a Int
	 * 
	 * @param numero a convertir a Int
	 * @return int
	 */
	private int getInt(String numero) {
		if (numero == null)
			return (int) -1L;

		return (int) ((numero.length() > 0) ? Integer.parseInt(numero) : -1L);
	}

}
