
package clientes;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import modelo.Cliente;
import modelo.Direccion;

public class TablaCliente {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Cliente> tablaCliente;

	@FXML
	private TableColumn<Cliente, String> colNombre;

	@FXML
	private TableColumn<Cliente, String> colApellido;

	@FXML
	private TableColumn<Cliente, Long> colDni;

	@FXML
	private TableColumn<Cliente, String> colDomicilio;

	@FXML
	private TableColumn<Cliente, Long> colCelular;

	@FXML
	private TableColumn<Cliente, Long> colTelefono;

	@FXML
	private TableColumn<Cliente, String> colCorreo;

	@FXML
	private TableColumn<Cliente, Long> colNroAbonado;

	@FXML
	private TableColumn<Cliente, LocalDate> colFechaAlta;

	@FXML
	private Button btnEliminar;

	@FXML
	private Button btnEditar;

	@FXML
	private Pane contenedor;

	@FXML
	public void editarCliente(ActionEvent event) {

		// Cargamos el controlador
		Cliente cliente = tablaCliente.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun cliente de la tabla de clientes
		if (cliente == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun cliente");
			alert.showAndWait();

			return;
		} else {
			if (!cliente.getTipoCliente().equals("Residencial")) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/ModificarEmpresa.fxml"));
				try {
					while (contenedor.getChildren().size() > 0)
						contenedor.getChildren().remove(0);

					Pane root = (Pane) loader.load();
					contenedor.getChildren().add(root);
					long documento = cliente.getDocumento();
					ModificarEmpresa modificarEmpresa = (ModificarEmpresa) loader.getController();
					modificarEmpresa.cargarDatosEmpresa(documento);
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/Modificar.fxml"));
				try {
					while (contenedor.getChildren().size() > 0)
						contenedor.getChildren().remove(0);

					Pane root = (Pane) loader.load();
					contenedor.getChildren().add(root);
					long documento = cliente.getDocumento();
					ModificarClientes modificarCliente = (ModificarClientes) loader.getController();
					modificarCliente.cargarDatosCliente(documento);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	@FXML
	public void eliminarCliente(ActionEvent event) {
		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de eliminar el cliente?");
		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {

				Cliente cliente = tablaCliente.getSelectionModel().getSelectedItem();
				// Se controla de que se seleccione algun cliente de la tabla de cliente
				if (cliente == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun cliente");
					alert.showAndWait();

					return;
				} else {
					long documento = cliente.getDocumento();
					boolean resultado = cliente.bajaCliente(documento);

					if (resultado) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Exito");
						alert.setHeaderText("Se cambio el estado del cliente exitosamente");
						alert.showAndWait();

						return;
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("No se pudo realizar el cambio de estado del cliente");
						alert.showAndWait();

						return;
					}
				}

			}
		});
	}

	@FXML
	public void initialize() {

		setIconTablaCliente();

		colNroAbonado.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("numeroAbonado"));
		colDni.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("documento"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
		colApellido.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellido"));
		colCelular.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("celular"));
		colTelefono.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("telefono"));
		colCorreo.setCellValueFactory(new PropertyValueFactory<Cliente, String>("correo"));
		colFechaAlta.setCellValueFactory(new PropertyValueFactory<Cliente, LocalDate>("ingreso"));
		// valores de domicilio
		colDomicilio.setCellValueFactory(new PropertyValueFactory<Cliente, String>("") {
			public ObservableValue<String> call(CellDataFeatures<Cliente, String> param) {
				StringProperty property = new SimpleStringProperty();
				Cliente cliente = param.getValue();
				property.set(cliente.getDireccion().getDomicilio());
				return property;
			}
		});

		String sql = "SELECT C.cliente_documento, C.cliente_nombre, C.cliente_apellido, C.cliente_domicilio,"
				+ "				 C.cliente_celular, C.cliente_telefono, C.cliente_correo, C.cliente_numero_abonado, "
				+ "				 C.cliente_fecha_alta,CT.cliente_tipo_nombre FROM cliente C INNER JOIN cliente_tipo CT ON C.cliente_tipo_id = CT.cliente_tipo_id";
		DataBase db = new DataBase();
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();
		try {
			rs.last();
			Cliente[] clientes = new Cliente[rs.getRow()];
			Direccion[] direcciones = new Direccion[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				clientes[i] = new Cliente();
				direcciones[i] = new Direccion();
				clientes[i].setDocumento(rs.getLong(1));
				clientes[i].setNombre(rs.getString(2));
				clientes[i].setApellido(rs.getString(3));
				direcciones[i].setDomicilio(rs.getString(4));
				clientes[i].setCelular(rs.getLong(5));
				clientes[i].setTelefono(rs.getLong(6));
				clientes[i].setCorreo(rs.getString(7));
				clientes[i].setNumeroAbonado(rs.getInt(8));
				clientes[i].setIngreso(rs.getDate(9).toLocalDate());
				clientes[i].setDireccion(direcciones[i]);
				clientes[i].setTipoCliente(rs.getString(10));
			}
			tablaCliente.getItems().addAll(clientes);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		mostrarInfoRapida();
		// controlBotonEditar();

		// menu de click secundario
		ContextMenu menu = new ContextMenu();
		MenuItem baja = new MenuItem("Dar de baja");
		MenuItem modificar = new MenuItem("Modificar datos");
		// MenuItem info = new MenuItem("Información rápida");
		menu.getItems().addAll(baja, modificar);

		// eventos de click secundario
		baja.setOnAction(e -> eliminarCliente(e));
		modificar.setOnAction(e -> editarCliente(e));
		// info.setOnAction(e -> eliminarCliente(e));

		// agregar el menu
		tablaCliente.setContextMenu(menu);

	}

	/**
	 * Establece los iconosde eliminar y editar de la tabla de clientes
	 */
	public void setIconTablaCliente() {

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

	/*
	 * Permite controlar que se seleccione primero un cliente y despues habilitar el
	 * boton de eliminar o modificar
	 * 
	 */
	/*
	 * private void controlBotonEditar() { tablaCliente.setOnMouseClicked(event -> {
	 * if (event.getClickCount() == 1) {
	 * 
	 * btnEditar.setDisable(false); btnEliminar.setDisable(false); return; } });
	 * 
	 * }
	 */

	/**
	 * Agrega el evento doble click de abrir interface de información rápida de
	 * cliente
	 */
	private void mostrarInfoRapida() {

		tablaCliente.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) { // evento de doble click

				Cliente cliente = tablaCliente.getSelectionModel().getSelectedItem();
				if (cliente == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun cliente");
					alert.showAndWait();

					return;
				} else {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/InfoRapida.fxml"));

					// elimina todo el contenido de contenedor
					contenedor.getChildren().setAll();

					try {
						// agrega el contenido a la pantalla
						contenedor.getChildren().add((Pane) loader.load());

						// carga el controlador
						InformacionRapida infoRapida = (InformacionRapida) loader.getController();
						// se obtiene el correo del cliente seleccionado

						long documentoCliente = cliente.getDocumento();
						if (documentoCliente > 20000000000L) {
							infoRapida.cargarDatosEmpresa(documentoCliente);
						} else {
							infoRapida.cargarDatosCliente(documentoCliente);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

}
