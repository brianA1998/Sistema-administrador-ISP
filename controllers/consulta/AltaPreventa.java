package consulta;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controlador.Validar;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import modelo.Cliente;
import modelo.Direccion;
import modelo.Preventa;

public class AltaPreventa {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnGuardar;

	@FXML
	private TextField txtNombreConsulta;

	@FXML
	private ComboBox<String> comboCiudadConsulta;

	@FXML
	private TextField txtTelefonoConsulta;

	@FXML
	private ComboBox<String> comboBarrioConsulta;

	@FXML
	private Pane contenedor;

	@FXML
	private Pane contenedorBusqueda;

	@FXML
	void guardarPreventa(ActionEvent event) {
		btnGuardar.setDisable(true);

		// Obtenemos los datos ingresados por usuario
		String nombre = txtNombreConsulta.getText();
		long telefono = Validar.numberPhone(txtTelefonoConsulta.getText());
		String barrio = getSelectedItem(comboBarrioConsulta);
		String ciudad = getSelectedItem(comboCiudadConsulta);

		controlCamposVacios(nombre, telefono, barrio, ciudad);

		Preventa preventa = new Preventa(nombre, telefono, barrio, ciudad);

		if (preventa.insert()) {

			vaciarFormulario();
		} else {
			// si ocurre un error
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la insercion de datos");
			alert.showAndWait();
		}
		btnGuardar.setDisable(false); // se vuelve a habilitar el boton

	}

	@FXML
	void initialize() {
		getEnter();

		// Evento de textfield
		txtNombreConsulta.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (oldVal) {
				mostrarTablaConsulta();
			}
		});

		// llena los combos
		comboCiudadConsulta.setItems(Cliente.llenarCiudades());
		comboBarrioConsulta.setItems(Cliente.llenarBarrios());
		// Control de entrada de caracteres
		txtNombreConsulta.setOnKeyTyped(Validar.texts());
		txtTelefonoConsulta.setOnKeyTyped(Validar.numbers());
		comboCiudadConsulta.setOnAction(e -> cargarComboBarrio());
	}

	/**
	 * Permite llenar el combo de barrio a partir de la ciudad en la que pertenece
	 * el barrio
	 */
	public void cargarComboBarrio() {
		int ciudadId = Direccion.getCiudadId(getSelectedItem(comboCiudadConsulta));
		comboBarrioConsulta.setItems(Direccion.llenarBarrioFiltrado(ciudadId));
	}

	/**
	 * Permite mostrar la tabla de consultas realizadas cuando se realiza un enter
	 */
	private void getEnter() {
		txtNombreConsulta.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					mostrarTablaConsulta();
				}
			}
		});
	}

	/**
	 * Permite mostrar la tabla de consulta realizadas
	 */
	private void mostrarTablaConsulta() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulta/TablaClienteConsultas.fxml"));

		try {
			contenedorBusqueda.getChildren().setAll();
			// agrega el contenido a la pantalla
			contenedorBusqueda.getChildren().add((Pane) loader.load());
			contenedorBusqueda.setVisible(true);

			// cargamos controlador
			TablaConsulta tabla = (TablaConsulta) loader.getController();
			tabla.setPanelPreventa(contenedor);
			String patron = txtNombreConsulta.getText();
			tabla.llenarTabla(patron);

		} catch (IOException e1) {

			e1.printStackTrace();
		}
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
	 * Permite verificar si los campos estan vacios o no
	 */
	private void controlCamposVacios(String nombre, long telefono, String barrio, String ciudad) {

		// Valida celular cliente
		if (telefono == -1L) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("error al ingresar el telefono");
			alert.showAndWait();

			btnGuardar.setDisable(false);
			return;
		}

		// valida los datos
		nombre = (nombre.length() > 0) ? nombre : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombre == null || telefono == -1L || ciudad == null || barrio == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

	}

	/**
	 * Permite limpiar los campos del formulario de preventa
	 */
	private void vaciarFormulario() {
		txtNombreConsulta.setText("");
		txtTelefonoConsulta.setText("");
		comboCiudadConsulta.getSelectionModel().select(0);
		comboBarrioConsulta.getSelectionModel().select(0);
	}

	/**
	 * @return the txtNombreConsulta
	 */
	public String getTxtNombreConsulta() {
		return txtNombreConsulta.getText();
	}

	/**
	 * @param txtNombreConsulta the txtNombreConsulta to set
	 */
	public void setTxtNombreConsulta(TextField txtNombreConsulta) {
		this.txtNombreConsulta = txtNombreConsulta;
	}

}
