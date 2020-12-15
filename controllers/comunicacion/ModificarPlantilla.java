package comunicacion;

import java.net.URL;
import java.util.ResourceBundle;

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

public class ModificarPlantilla {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextArea txtTitulo;

	@FXML
	private TextArea txtMensaje;

	@FXML
	private Button btnGuardar;

	private Plantilla plantilla;

	@FXML
	void cambiarPantalla(ActionEvent event) {

	}

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

		// Le enviamos el mensaje y titulo a plantilla
		plantilla.setMensaje(mensaje);
		plantilla.setTitulo(titulo);

		if (plantilla.update()) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exito");
			alert.setHeaderText("Se ha actualizado correctamente los datos de la plantilla");
			alert.showAndWait();
			vaciarFormulario();
		}

	}

	@FXML
	void initialize() {

	}

	public void cargarDatosPlantilla(Plantilla plantilla) {

		this.plantilla = plantilla;

		// Imprimimos los valores de mensaje y titulo de plantilla
		txtMensaje.setText(plantilla.getMensaje());
		txtTitulo.setText(plantilla.getTitulo());

	}

	private void vaciarFormulario() {
		txtMensaje.setText("");
		txtTitulo.setText("");
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

}
