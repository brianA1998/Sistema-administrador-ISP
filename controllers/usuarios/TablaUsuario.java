package usuarios;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import db.DataBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import modelo.Direccion;
import modelo.Usuario;

public class TablaUsuario {
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Usuario> tablaUser;

	@FXML
	private TableColumn<Usuario, String> colNombre;

	@FXML
	private TableColumn<Usuario, String> colApellido;

	@FXML
	private TableColumn<Usuario, Long> colDni;

	@FXML
	private TableColumn<Usuario, String> colCorreo;

	@FXML
	private TableColumn<Usuario, Long> colTelefono;

	@FXML
	private TableColumn<Usuario, String> colDomicilio;

	@FXML
	private TableColumn<Usuario, LocalDate> colNacimiento;

	@FXML
	private TableColumn<Usuario, LocalDate> colIngreso;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnEliminar;

	@FXML
	private Pane contenedor;

	@FXML
	public void editarUsuario(ActionEvent event) {

		// Cargamos el controlador
		Usuario usuario = tablaUser.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun usuario de la tabla de usuarios
		if (usuario == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun usuario");
			alert.showAndWait();

			return;
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/usuario/Modificar.fxml"));
			try {
				while (contenedor.getChildren().size() > 0)
					contenedor.getChildren().remove(0);

				Pane root = (Pane) loader.load();
				contenedor.getChildren().add(root);

				long documento = usuario.getDocumento();
				ModificarUsuarios modificarUsuario = (ModificarUsuarios) loader.getController();
				modificarUsuario.cargarDatosUsuario(documento);

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	@FXML
	public void eliminarUsuario(ActionEvent event) {

		eliminarUsuario();

	}

	@FXML
	void initialize() {

		setIconTablaUsuario();

		colNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
		colApellido.setCellValueFactory(new PropertyValueFactory<Usuario, String>("apellido"));
		colDni.setCellValueFactory(new PropertyValueFactory<Usuario, Long>("documento"));
		colCorreo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("correo"));
		colTelefono.setCellValueFactory(new PropertyValueFactory<Usuario, Long>("telefono"));
		colNacimiento.setCellValueFactory(new PropertyValueFactory<Usuario, LocalDate>("nacimiento"));
		colIngreso.setCellValueFactory(new PropertyValueFactory<Usuario, LocalDate>("ingreso"));
		colDomicilio.setCellValueFactory(new PropertyValueFactory<Usuario, String>("domicilio") {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Usuario, String> param) {
				StringProperty property = new SimpleStringProperty();
				Usuario usuario = param.getValue();
				property.set(usuario.getDireccion().getDomicilio());
				return property;
			}
		});

		DataBase db = new DataBase();
		String sql = "SELECT usuario_nombre, usuario_apellido, usuario_documento, usuario_correo, usuario_telefono, "
				+ "usuario_nacimiento, usuario_fecha_ingreso, usuario_domicilio FROM usuario WHERE usuario_estado = 1;";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			while (rs.next()) {
				Usuario usuario = new Usuario();
				Direccion direccion = new Direccion();

				usuario.setNombre(rs.getString(1));
				usuario.setApellido(rs.getString(2));
				usuario.setDocumento(rs.getLong(3));
				usuario.setCorreo(rs.getString(4));
				usuario.setTelefono(rs.getLong(5));
				usuario.setNacimiento((rs.getDate(6) == null) ? null : rs.getDate(6).toLocalDate());
				usuario.setDireccion(direccion);
				usuario.setIngreso((rs.getDate(7) == null) ? null : rs.getDate(7).toLocalDate());
				direccion.setDomicilio(rs.getString(8));

				tablaUser.getItems().add(usuario);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		controlBotonEditar();

		// menu de click secundario
		ContextMenu menu = new ContextMenu();
		MenuItem baja = new MenuItem("Dar de baja");
		MenuItem modificar = new MenuItem("Modificar datos");

		menu.getItems().addAll(baja, modificar);

		// eventos de click secundario
		baja.setOnAction(e -> eliminarUsuario(e));
		modificar.setOnAction(e -> editarUsuario(e));

		// agregar el menu
		tablaUser.setContextMenu(menu);

	}

	/**
	 * Establece los iconos de editar y eliminar de la tabla de usuarios
	 */
	public void setIconTablaUsuario() {
		// Establece el icono del boton eliminar
		File archivo = new File("imagenes" + File.separator + "cancelar.png");
		Image imagen = new Image(archivo.toURI().toString(), 50, 50, true, true, true);
		btnEliminar.setGraphic(new ImageView(imagen));
		btnEliminar.setContentDisplay(ContentDisplay.TOP);

		// Establece el icono del boton editar
		File archivo1 = new File("imagenes" + File.separator + "lapices.png");
		Image imagen1 = new Image(archivo1.toURI().toString(), 50, 50, true, true, true);
		btnEditar.setGraphic(new ImageView(imagen1));
		btnEditar.setContentDisplay(ContentDisplay.TOP);
	}

	/**
	 * Permite controlar que se seleccione primero un cliente y despues habilitar el
	 * boton de eliminar o modificar
	 */
	private void controlBotonEditar() {

		tablaUser.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				btnEditar.setDisable(false);
				btnEliminar.setDisable(false);
				return;
			}
		});

	}

	/**
	 * Permite eliminar un usuario
	 */
	private void eliminarUsuario() {

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de eliminar al usuario?");

		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {
				// Cargamos el controlador
				Usuario usuario = tablaUser.getSelectionModel().getSelectedItem();
				// Se controla de que se seleccione algun usuario de la tabla de usuarios
				if (usuario == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun usuario");
					alert.showAndWait();

					return;
				} else {
					long documento = usuario.getDocumento();
					boolean resultado = usuario.cambiarEstado(documento);

					if (resultado) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Error");
						alert.setHeaderText("Se elimino exitosamente ");
						alert.showAndWait();

						return;
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("No se pudo realizar el cambio de estado del usuario");
						alert.showAndWait();

						return;
					}
				}

			}

		});

	}
}
