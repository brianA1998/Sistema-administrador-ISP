package adhesiones;

import java.net.URL;
import java.util.ResourceBundle;

import controlador.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import modelo.Adhesion;

public class AltaVarios {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenidoItemVario;

	@FXML
	private TextField txtDescripcionVarios;

	@FXML
	private TextField txtMontoVarios;

	@FXML
	private ComboBox<String> comboIvaVarios;

	@FXML
	private ComboBox<String> comboMonedaVarios;

	@FXML
	private Button btnGuardar;

	@FXML
	void guardarItemVarios(ActionEvent event) {
		// crear objeto del modelo
		Adhesion adhesion = new Adhesion();

		btnGuardar.setDisable(true);

		// OBTENER DATOS
		String detalleVarios = txtDescripcionVarios.getText();
		float montoVarios = getFloat(txtMontoVarios.getText());
		float ivaVarios = getFloat(getSelectedItem(comboIvaVarios).replace("%", "").trim());
		String monedaVarios = getSelectedItem(comboMonedaVarios);

		// valida los datos
		detalleVarios = (detalleVarios.length() > 0) ? detalleVarios : null;

		// Si hubo un dato no ingresado sale sin guardar

		if (detalleVarios == null || montoVarios == -1F || ivaVarios == -1F || monedaVarios == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		adhesion.setItem(montoVarios, detalleVarios, monedaVarios, ivaVarios);

		// Insertamos un item varios en la base de datos
		adhesion.insertarItemVarios();

		// cierro ventana modal
		Stage stage = (Stage) btnGuardar.getParent().getScene().getWindow();
		stage.close();
	}

	@FXML
	void initialize() {
		// Llenamos los combos
		comboIvaVarios.setItems(Adhesion.llenarIva());
		comboMonedaVarios.setItems(Adhesion.llenarCodigoMoneda());

		// Control de entrada de caracteres
		txtDescripcionVarios.setOnKeyTyped(Validar.alphanumeric());
		txtMontoVarios.setOnKeyTyped(Validar.numbers());
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
}
