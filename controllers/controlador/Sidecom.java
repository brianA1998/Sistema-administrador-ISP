package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import clientes.MenuClientes;
import clientes.TablaInformacionRapida;
import comunicacion.MenuComunicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import servicios.MenuServicios;
import usuarios.MenuUsuarios;

public class Sidecom {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane menu;

	@FXML
	private TextField txtBuscar;

	@FXML
	private Button btnBuscar;

	@FXML
	private Button btnFacturacion;

	@FXML
	private Button btnInstalacion;

	@FXML
	private Button btnCobranzas;

	@FXML
	private Button btnComunicacion;

	@FXML
	private Button btnClientes;

	@FXML
	private Button btnServicio;

	@FXML
	private Button btnUsuario;

	@FXML
	private Pane contenedor;

	@FXML
	private Pane contenidoBusqueda;

	@FXML
	private Button btnAlta;

	@FXML
	private Button btnConsulta;

	@FXML
	private Button btnAjustes;

	@FXML
	private ImageView imgSidecom;

	private Pane activo;

	@FXML
	public void buscarCliente(ActionEvent event) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/TablaInfoRapida.fxml"));
		try {
			contenidoBusqueda.getChildren().setAll();
			// agrega el contenido a la pantalla
			contenidoBusqueda.getChildren().add((Pane) loader.load());
			contenedor.setVisible(false);
			contenidoBusqueda.setVisible(true);

			// carga el controlador
			TablaInformacionRapida tabla = (TablaInformacionRapida) loader.getController();
			String patron = txtBuscar.getText();
			tabla.llenarTabla(patron);
			tabla.setContenidoAnterior(contenedor);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@FXML
	public void cambiarContenido(ActionEvent event) {

		if (!contenedor.isVisible()) {
			contenedor.setVisible(true);
			contenidoBusqueda.setVisible(false);
		}

		Button btn;

		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		contenedor.getChildren().remove(activo);

		if (btn.equals(btnUsuario)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuario/SubMenu.fxml"));
			try {
				// Agregar Submenu a contenedor
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

				// carga el controlador
				MenuUsuarios usuarios = (MenuUsuarios) loader.getController();
				usuarios.redimensionar(contenedor.getWidth(), contenedor.getHeight());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnClientes)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/Submenu.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

				// carga el controlador

				MenuClientes clientes = (MenuClientes) loader.getController();
				clientes.redimensionar(contenedor.getWidth(), contenedor.getHeight());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (btn.equals(btnServicio)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicio/Submenu.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

				// carga el controlador
				MenuServicios servicio = (MenuServicios) loader.getController();
				servicio.redimensionar(contenedor.getWidth(), contenedor.getHeight());

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		if (btn.equals(btnInstalacion)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/instalacion/Mostrar.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		if (btn.equals(btnComunicacion)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/Submenu.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);
				// carga el controlador
				MenuComunicacion comunicacion = (MenuComunicacion) loader.getController();
				comunicacion.redimensionar(contenedor.getWidth(), contenedor.getHeight());

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		if (btn.equals(btnConsulta)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/consulta/Preventa.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

		if (btn.equals(btnAjustes)) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajustes/Ajustes.fxml"));
			try {
				activo = (Pane) loader.load();
				contenedor.getChildren().add(activo);

			} catch (IOException e) {

				e.printStackTrace();
			}

		}

	}

	@FXML
	public void initialize() {
		txtBuscar.setOnKeyTyped(Validar.alphanumeric());
		contenidoBusqueda.setVisible(false);

		// Establece el icono de usuario
		File archivo = new File("imagenes" + File.separator + "IMAGEN SIDECOM.png");
		Image imagen = new Image(archivo.toURI().toString(), 200, 54, true, true, true);
		imgSidecom.setImage(imagen);
	}

	public void redimensionar(double w, double h) {
		menu.setPrefHeight(h);
		contenedor.setPrefSize(w - menu.getWidth(), h - menu.getHeight());
		contenedor.setLayoutX(menu.getWidth());
	}
}
