package usuarios;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MenuUsuarios {

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
	private Pane contenedorUsuario;

	private Pane activo;

	@FXML
	void cambiarPantalla(ActionEvent event) {
		Button btn;

		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		contenedorUsuario.getChildren().remove(activo);
		if (btn.equals(btnAlta)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuario/Alta.fxml"));
			try {
				// Agregar Submenu a contenedor

				activo = (Pane) loader.load();
				// Pane panel = (Pane) loader.load();

				contenedorUsuario.getChildren().add(activo);

				// carga el controlador
				AltaUsuarios usuarios = (AltaUsuarios) loader.getController();
				usuarios.redimensionar(contenedorUsuario.getWidth(), contenedorUsuario.getHeight());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnMostrar)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuario/Mostrar.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();

				contenedorUsuario.getChildren().add(activo);

				// carga el controlador
				TablaUsuario mostrarUsuarios = (TablaUsuario) loader.getController();

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
		contenedorUsuario.setPrefSize(w - barraMenu.getWidth(), h - barraMenu.getHeight());

	}
}
