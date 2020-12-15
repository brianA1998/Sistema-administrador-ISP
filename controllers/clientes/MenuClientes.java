package clientes;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MenuClientes {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane menuCliente;

	@FXML
	private Button btnMostrar;

	@FXML
	private Button btnAlta;

	@FXML
	private Pane contenedor;

	private Pane activo;

	@FXML
	void cambiarPantalla(ActionEvent event) {
		Button btn;
		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		contenedor.getChildren().remove(activo);

		if (btn.equals(btnAlta)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/Alta.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);
				// carga el controlador
				AltaClientes clientes = (AltaClientes) loader.getController();
				clientes.redimensionar(contenedor.getWidth(), contenedor.getHeight());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnMostrar)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/Mostrar.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

				// carga el controlador
				TablaCliente mostrarCliente = (TablaCliente) loader.getController();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	void initialize() {

	}

	public void redimensionar(double w, double h) {
		menuCliente.setPrefWidth(w);
	}
}
