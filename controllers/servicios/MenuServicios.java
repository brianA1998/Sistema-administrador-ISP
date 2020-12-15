
package servicios;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MenuServicios {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane barraMenu;

	@FXML
	private Button btnAlta;

	@FXML
	private Button btnMostrar;

	@FXML
	private Pane contenedorServicio;

	private Pane activo;

	@FXML
	void cambiarPantalla(ActionEvent event) {
		Button btn;
		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		contenedorServicio.getChildren().remove(activo);

		if (btn.equals(btnAlta)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicio/Alta.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedorServicio.getChildren().add(activo);
				// carga el controlador
				AltaServicio servicio = (AltaServicio) loader.getController();
				servicio.redimensionar(contenedorServicio.getWidth(), contenedorServicio.getHeight());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnMostrar)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicio/Mostrar.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();

				contenedorServicio.getChildren().add(activo);

				// carga el controlador
				TablaServicio mostrarServicio = (TablaServicio) loader.getController();

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
		contenedorServicio.setPrefSize(w - barraMenu.getWidth(), h - barraMenu.getHeight());

	}
}
