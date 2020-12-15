package comunicacion;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MenuComunicacion {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane barraMenu;

	@FXML
	private Button btnCargarPlantilla;

	@FXML
	private Button btnMostrarPlantilla;

	@FXML
	private Button btnEnviarCorreos;

	@FXML
	private Button btnCorreosEnviados;

	@FXML
	private Pane contenedorCorreo;

	private Pane activo;

	@FXML
	void cambiarPantalla(ActionEvent event) {
		Button btn;
		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		contenedorCorreo.getChildren().remove(activo);

		if (btn.equals(btnEnviarCorreos)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/EnvioCorreo.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedorCorreo.getChildren().add(activo);
				// carga el controlador
				EnvioCorreo enviarCorreo = (EnvioCorreo) loader.getController();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnCargarPlantilla)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/AltaPlantilla.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedorCorreo.getChildren().add(activo);
				// carga el controlador
				AltaPlantilla correo = (AltaPlantilla) loader.getController();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnMostrarPlantilla)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/MostrarPlantilla.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedorCorreo.getChildren().add(activo);
				// carga el controlador
				MostrarPlantilla mostrarPlantilla = (MostrarPlantilla) loader.getController();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnCorreosEnviados)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/MostrarCorreosEnviados.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedorCorreo.getChildren().add(activo);
				// carga el controlador
				MostrarCorreosEnviados correoEnviado = (MostrarCorreosEnviados) loader.getController();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void initialize() {

	}

	public void redimensionar(double w, double h) {
		barraMenu.setPrefWidth(w);
		contenedorCorreo.setPrefSize(w - barraMenu.getWidth(), h - barraMenu.getHeight());

	}
}
