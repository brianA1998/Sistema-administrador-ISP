package servicios;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import controlador.Mikrotik;
import controlador.Validar;
import db.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Servicio;

public class ModificarServicios {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextField txtNombreServicio;

	@FXML
	private ComboBox<String> comboTipoServicio;

	@FXML
	private ComboBox<String> subidaMegaKilo;

	@FXML
	private ComboBox<String> bajadaMegaKilo;
	@FXML
	private TextField txtSubida;

	@FXML
	private TextField txtBajada;

	@FXML
	private ComboBox<String> comboTipoMoneda;

	@FXML
	private TextField txtMonto;

	@FXML
	private ComboBox<String> comboIva;

	@FXML
	private Button btnGuardar;

	@FXML
	private TextField txtCodigo;

	@FXML
	private ComboBox<String> comboRubro;

	private String nombreServicio;

	@FXML
	void guardarServicio(ActionEvent event) {
		// desabilita el boton, evita el doble click
		btnGuardar.setDisable(true);

		// OBTENER DATOS
		String nombre = txtNombreServicio.getText();

		String subida = txtSubida.getText();
		String bajada = txtBajada.getText();
		float monto = getFloat(txtMonto.getText());
		String tipoServicio = getSelectedItem(comboTipoServicio);
		String tipoMoneda = getSelectedItem(comboTipoMoneda);
		float Iva = getIva();
		String rubro = getSelectedItem(comboRubro);
		String codigo = txtCodigo.getText();

		subida += getSelectedItem(subidaMegaKilo).charAt(0);
		bajada += getSelectedItem(bajadaMegaKilo).charAt(0);

		// Si hubo un dato no ingresado sale sin guardar

		if (nombre == null || monto == -1F || Iva == -1F || tipoMoneda == null || tipoServicio == null
				|| codigo == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		Servicio servicio = new Servicio(nombre, subida, bajada, tipoServicio, tipoMoneda, Iva, monto, codigo, rubro);

		int id_servicio = servicio.getIdServicio();

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de cargar el servicio?");
		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {
				if (servicio.update(id_servicio)) {
					if (rubro.contentEquals("internet")) {
						Mikrotik mikrotik = new Mikrotik();
						mikrotik.insertProfile(servicio);
					}
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Exito");
					alert.setHeaderText("Se ha actualizado correctamente los datos del servicio");
					alert.showAndWait();
					vaciarFormulario();
				} else {
					// si ocurre un error
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Ocurrio un error en la actualizacion de los datos del servicio");
					alert.showAndWait();
				}
				btnGuardar.setDisable(false); // se vuelve a habilitar el boton
			}
		});

	}

	@FXML
	void initialize() {
		seleccionComboRubro();
		// llena los combos
		comboTipoServicio.setItems(Servicio.llenarTipoServicio());
		comboTipoMoneda.setItems(Servicio.llenarCodigoMoneda());
		comboIva.setItems(Servicio.llenarIva());
		comboRubro.setItems(Servicio.llenarRubro());
		subidaMegaKilo.setItems(Servicio.limiteMaximo());
		bajadaMegaKilo.setItems(Servicio.limiteMaximo());

		// Control de entrada de caracteres
		txtNombreServicio.setOnKeyTyped(Validar.alphanumeric());
		txtCodigo.setOnKeyTyped(Validar.alphanumeric());
		txtSubida.setOnKeyTyped(Validar.numbers());
		txtBajada.setOnKeyTyped(Validar.numbers());
		txtMonto.setOnKeyTyped(Validar.numbers());
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
	 * Convierte numero de String a float
	 * 
	 * @param numero a convertir a Float
	 * @return el número o -1 en caso de que no haya numero
	 */
	private float getFloat(String numero) {
		if (numero == null)
			return -1;

		return (numero.length() > 0) ? Float.parseFloat(numero) : -1;
	}

	/**
	 * Vacia todos los campos del formulario de alta servicio
	 */
	private void vaciarFormulario() {
		txtNombreServicio.setText("");
		txtCodigo.setText("");
		txtSubida.setText("");
		txtBajada.setText("");
		txtMonto.setText("");
		comboIva.getSelectionModel().select(null);
		comboRubro.getSelectionModel().select(null);
		comboTipoMoneda.getSelectionModel().select(null);
		comboTipoServicio.getSelectionModel().select(null);
		subidaMegaKilo.getSelectionModel().select(null);
		bajadaMegaKilo.getSelectionModel().select(null);
	}

	/**
	 * Permite retornar el iva seleccionado del combo Iva
	 * 
	 * @return iva seleccionado sin el porcentaje
	 */
	private float getIva() {
		float iva;
		int indice = comboIva.getSelectionModel().getSelectedIndex();

		switch (indice) {
		case 0:
			iva = 10.5f;
			break;
		case 1:
			iva = 21.00f;
			break;
		case 2:
			iva = 27.00f;
			break;
		default:
			iva = 0f;
		}

		return iva;
	}

	/**
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * servicio especifico mediante el nombre de un servicio especifico
	 * 
	 * @param cadena nombre del servicio
	 */
	public void cargarDatosServicio(String cadena) {
		nombreServicio = cadena;
		DataBase database = new DataBase();
		String sql = "SELECT S.servicio_nombre,S.servicio_subida,S.servicio_bajada,S.servicio_monto,S.servicio_codigo,TP.tipo_moneda_codigo,TS.tipo_servicio_nombre,RS.rubro_nombre,S.servicio_iva FROM servicio S "
				+ "INNER JOIN tipo_moneda TP ON S.tipo_moneda_id = TP.tipo_moneda_id "
				+ "INNER JOIN tipo_servicio TS ON S.tipo_servicio_id = TS.tipo_servicio_id "
				+ "INNER JOIN rubro_servicio RS ON rs.rubro_id = S.rubro_id\r\n" + "WHERE S.servicio_nombre = ?";

		database.setQuery(sql); // consulta sql
		database.setParametro(1, nombreServicio);

		ResultSet rs = database.executeQuery();// retorna resulset

		try {
			if (rs.next()) {

				// Llena todos los campos de texto con los campos recuperados de la base de
				// datos
				txtNombreServicio.setText(rs.getString(1));

				if (rs.getString(2) == null) {
					txtSubida.setText("");
				} else {
					String subida = rs.getString(2);
					txtSubida.setText(subida.substring(0, subida.length() - 1));
					if (subida.contains("M"))
						subidaMegaKilo.getSelectionModel().select("Mb");
					else
						subidaMegaKilo.getSelectionModel().select("Kb");

				}

				if (rs.getString(3) == null) {
					txtBajada.setText("");
				} else {
					String bajada = rs.getString(3);
					txtBajada.setText(bajada.substring(0, bajada.length() - 1));
					if (bajada.contains("M"))
						bajadaMegaKilo.getSelectionModel().select("Mb");
					else
						bajadaMegaKilo.getSelectionModel().select("Kb");

				}

				txtMonto.setText(rs.getString(4));
				txtCodigo.setText(rs.getString(5));
				comboTipoMoneda.getSelectionModel().select(rs.getString(6));
				comboTipoServicio.getSelectionModel().select(rs.getString(7));
				comboRubro.getSelectionModel().select(rs.getString(8));
				comboIva.getSelectionModel().select(rs.getString(9));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permite obtener el item seleccionado de combo Rubro y deshabilitar el campo
	 * subida y bajada
	 */
	private void seleccionComboRubro() {
		comboRubro.setOnAction(event -> {

			String rubro = comboRubro.getSelectionModel().getSelectedItem().toString();
			if (rubro.equals("telefonia ip")) {
				txtSubida.setText("");
				txtBajada.setText("");
				txtSubida.setDisable(true);
				txtBajada.setDisable(true);
				subidaMegaKilo.setDisable(true);
				bajadaMegaKilo.setDisable(true);

			} else {

				txtSubida.setDisable(false);
				txtBajada.setDisable(false);
				subidaMegaKilo.setDisable(false);
				bajadaMegaKilo.setDisable(false);
			}

		});
	}

}
