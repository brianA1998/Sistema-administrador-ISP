package comunicacion;

import java.net.URL;
import java.util.ResourceBundle;

import controlador.Validar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import modelo.Plantilla;

public class AltaPlantilla {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextArea txtTitulo;

	@FXML
	private TextArea txtMensaje;

	@FXML
	private Button btnGuardar;

	@FXML
	void guardarPlantilla(ActionEvent event) {

		// Obtenemos los datos
		String mensaje = txtMensaje.getText();
		String titulo = txtTitulo.getText();

		// valida los datos
		titulo = (titulo.length() > 0) ? titulo : null;
		mensaje = (mensaje.length() > 0) ? mensaje : null;

		// si hubo un dato no ingresado sale sin guardar
		if (mensaje == null || titulo == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		Plantilla plantilla = new Plantilla(mensaje, titulo);

		if (plantilla.insertarPlantilla(mensaje, titulo)) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exito");
			alert.setHeaderText("Se registro correctamente la plantilla");

			alert.showAndWait();
			vaciarFormulario();
		}

	}

	@FXML
	void initialize() {

		// Validamos los campos
		txtMensaje.setOnKeyTyped(Validar.alphanumeric());
		txtTitulo.setOnKeyTyped(Validar.alphanumeric());
	}

	/**
	 * Permite retonar los itemos del combo de abmCorreo
	 * 
	 * @return item de abmCorreo
	 */
	public ObservableList<String> AbmCorreos() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("Cargar Plantilla", "Mostrar Plantillas");
		return items;
	}

	/**
	 * Permite borrar todos los campos de la interfaz de altaCorreo
	 */
	private void vaciarFormulario() {
		txtMensaje.setText("");
		txtTitulo.setText("");

	}

}
