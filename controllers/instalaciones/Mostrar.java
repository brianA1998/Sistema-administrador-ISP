package instalaciones;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import DriverJasperReport.JReport;
import db.DataBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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

public class Mostrar {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;
	@FXML
	private TableView<Cliente> tablaInstalacion;

	@FXML
	private TableColumn<Cliente, Long> colNroAbonado;

	@FXML
	private TableColumn<Cliente, Long> colDni;

	@FXML
	private TableColumn<Cliente, String> colNombre;

	@FXML
	private TableColumn<Cliente, String> colApellido;

	@FXML
	private TableColumn<Cliente, String> colDomicilio;

	@FXML
	private TableColumn<Cliente, Long> colCelular;

	@FXML
	private TableColumn<Cliente, Long> colTelefono;

	@FXML
	private TableColumn<Cliente, String> colCorreo;

	@FXML
	private TableColumn<Cliente, LocalDate> colFechaAlta;

	@FXML
	private Button btnCancelar;

	@FXML
	private Button btnFinalizacion;

	@FXML
	private Pane Menu;

	@FXML
	private ComboBox<String> comboEstado;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private Button btnFiltrar;

	@FXML
	private DatePicker fechaDesde;

	@FXML
	private DatePicker fechaHasta;

	@FXML
	private Button btnGenerarContrato;

	@FXML
	void generarContrato(ActionEvent event) {
		Cliente cliente = tablaInstalacion.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun cliente de la tabla de clientes
		if (cliente == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun cliente");
			alert.showAndWait();

			return;
		} else {
			generarContrato(cliente.getDocumento());
		}

	}

	@FXML
	void filtrarCampos(ActionEvent event) {
		llenarTabla();
	}

	@FXML
	void cancelarInstalacion(ActionEvent event) {
		cancelarInstalacion();
	}

	@FXML
	void finalizarInstalacion(ActionEvent event) {
		finalizarInstalacion();
	}

	@FXML
	public void initialize() {

		setIconTablaInstalacion();

		// llenamos los combos

		comboCiudad.setItems(Cliente.llenarCiudades());
		comboEstado.setItems(Cliente.llenarEstado());
		comboEstado.getSelectionModel().select(1);

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

		llenarTabla();
		controlBotones();

		// menu de click secundario
		ContextMenu menu = new ContextMenu();
		MenuItem cancelar = new MenuItem("Cancelar instalación");
		MenuItem modificar = new MenuItem("Finalizar instalación");

		menu.getItems().addAll(cancelar, modificar);

		// eventos de click secundario
		cancelar.setOnAction(e -> cancelarInstalacion(e));
		modificar.setOnAction(e -> finalizarInstalacion(e));

		// agregar el menu
		tablaInstalacion.setContextMenu(menu);
	}

	/**
	 * Establece el icono del boton editar y eliminar de la tabla de instalaciones
	 */
	public void setIconTablaInstalacion() {

		// Establece el icono del boton eliminar
		File archivo = new File("imagenes" + File.separator + "cancelar.png");
		Image imagen = new Image(archivo.toURI().toString(), 50, 50, true, true, true);
		btnCancelar.setGraphic(new ImageView(imagen));
		btnCancelar.setContentDisplay(ContentDisplay.CENTER);

		// Establece el icono del boton finalizacion de instalacion
		File archivo1 = new File("imagenes" + File.separator + "garrapata (1).png");
		Image imagen1 = new Image(archivo1.toURI().toString(), 50, 50, true, true, true);
		btnFinalizacion.setGraphic(new ImageView(imagen1));
		btnFinalizacion.setContentDisplay(ContentDisplay.CENTER);
	}

	/**
	 * Permite controlar que se seleccione primero un cliente y despues habilitar el
	 * boton de eliminar o modificar
	 */
	private void controlBotones() {

		tablaInstalacion.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				btnCancelar.setDisable(false);
				btnFinalizacion.setDisable(false);
				return;
			}
		});

	}

	/**
	 * Permite retornar el estado seleccionado del combo estado
	 * 
	 * @return estado seleccionado del combo estado
	 */
	private int getEstado() {
		return comboEstado.getSelectionModel().getSelectedIndex() + 1;
	}

	/**
	 * Permite obtener el id de la ciudad
	 * 
	 * @return id de la ciudad o -1 si no se selecciono nada en el combo
	 */
	private int getCiudad() {
		String ciudad = getSelectedItem(comboCiudad);
		int ciudad_id = Direccion.getCiudadId(ciudad);
		return ciudad_id;
	}

	/**
	 * Permite llenar la tabla con los clientes que tienen estado 1 o 2
	 * 
	 */
	private void llenarTabla() {
		int j = 2;
		int estadoInstalacion = getEstado();
		LocalDate desde = fechaDesde.getValue();
		LocalDate hasta = fechaHasta.getValue();
		DataBase db = new DataBase();
		int id_ciudad = getCiudad();

		if (estadoInstalacion == 2) { // instalacion pendiente

			String sql = "SELECT cliente_documento, cliente_nombre, cliente_apellido, cliente_domicilio,"
					+ " cliente_celular, cliente_telefono, cliente_correo, cliente_numero_abonado, "
					+ " cliente_fecha_alta FROM cliente WHERE cliente_estado = ?";

			if (fechaDesde.getValue() != null && fechaHasta.getValue() != null)
				sql += " AND cliente_fecha_alta  BETWEEN ? AND ?";

			if (id_ciudad != -1)
				sql += " AND ciudad_id=?";

			db.setQuery(sql);
			db.setParametro(1, estadoInstalacion);

			if (fechaDesde.getValue() != null && fechaHasta.getValue() != null) {
				db.setParametro(2, Date.valueOf(desde));
				db.setParametro(3, Date.valueOf(hasta));
				j = 4;
			}

			if (id_ciudad != -1)
				db.setParametro(j, id_ciudad);

		} else if (estadoInstalacion == 1) { // instalacion finalizada

			String sql = "SELECT c.cliente_documento, c.cliente_nombre, c.cliente_apellido, c.cliente_domicilio, c.cliente_celular, c.cliente_telefono, c.cliente_correo, c.cliente_numero_abonado, i.instalacion_alta FROM cliente c INNER JOIN instalacion i ON c.cliente_documento = i.cliente_documento WHERE c.cliente_estado = ?";

			if (fechaDesde.getValue() != null && fechaHasta.getValue() != null)
				sql += " AND instalacion_alta BETWEEN ? AND ?";

			if (id_ciudad != -1)
				sql += " AND ciudad_id=?";

			db.setQuery(sql);
			db.setParametro(1, estadoInstalacion);

			if (fechaDesde.getValue() != null && fechaHasta.getValue() != null) {
				db.setParametro(2, Date.valueOf(desde));
				db.setParametro(3, Date.valueOf(hasta));
				j = 4;
			}

			if (id_ciudad != -1)
				db.setParametro(j, id_ciudad);
		}
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
			}
			tablaInstalacion.getItems().setAll(clientes);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permite generar el contrato con los datos del cliente
	 */
	private void generarContrato(long numero) {
		Cliente cliente = new Cliente(numero);

		String path = "reporte" + File.separator + cliente.getNumeroAbonado() + "-" + cliente.getNombre() + "-"
				+ cliente.getApellido() + ".pdf";

		File archivo = new File(path);

		if (archivo.exists()) {
			try {
				Desktop.getDesktop().open(archivo);
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		String ciudad = cliente.getDireccion().getCiudad();
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nombreCliente", cliente.getNombre());
		parametros.put("apellidoCliente", cliente.getApellido());
		parametros.put("documentoCliente", numero);
		parametros.put("domicilioCliente", cliente.getDireccion().getDomicilio());
		parametros.put("departamentoCliente", cliente.getDireccion().getDepartamento());
		parametros.put("celularCliente", cliente.getCelular());
		parametros.put("barrioCliente", cliente.getDireccion().getBarrio());
		parametros.put("ciudadCliente", cliente.getDireccion().getCiudad());
		parametros.put("provinciaCliente", cliente.getDireccion().getProvincia(ciudad));
		parametros.put("codigoPostal", Direccion.getCodigoPostal(cliente.getDireccion().getCiudad()));
		parametros.put("correoCliente", cliente.getCorreo());
		parametros.put("monoblockCliente", cliente.getDireccion().getMonoblock());
		parametros.put("manzanaCliente", cliente.getDireccion().getManzana());
		parametros.put("fechaNacCliente", (cliente.getNacimiento() == null) ? "" : cliente.getNacimiento().toString());
		parametros.put("numeroCliente", cliente.getNumeroAbonado());

		// Conexion con base de datos
		Connection con = new DataBase().getConnection();

		// Creamos un reporte
		JReport reporte = new JReport();

		reporte.createReport(con, "reporte" + File.separator + "Contrato.jasper", parametros);
		reporte.showViewer();
		reporte.exportPDF(path);

	}

	private void cancelarInstalacion() {

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de cancelar la instalacion?");
		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {

				Cliente cliente = tablaInstalacion.getSelectionModel().getSelectedItem();
				// Se controla de que se seleccione algun cliente de la tabla de clientes
				if (cliente == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun cliente");
					alert.showAndWait();

					return;
				} else {
					long documento = cliente.getDocumento();
					boolean resultado = cliente.cancelarInstalacion(documento);

					if (getEstado() == 2) {
						if (resultado) {
							// Eliminamos el cliente con la instalacion cancelada
							tablaInstalacion.getItems().removeAll(cliente);

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Exito");
							alert.setHeaderText("Instalacion cancelada");
							alert.showAndWait();

							return;
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Error");
							alert.setHeaderText("No se pudo realizar el cambio de estado del servicio");
							alert.showAndWait();

							return;
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText(
								"Solo se puede cancelar la instalacion a un cliente que tiene una instalacion pendiente");
						alert.showAndWait();

						return;
					}
				}

			}

		});

	}

	private void finalizarInstalacion() {
		Cliente cliente = tablaInstalacion.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun cliente de la tabla de clientes
		if (cliente == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun cliente");
			alert.showAndWait();

			return;
		} else {
			boolean resultado = cliente.instalacionFinalizada();

			if (resultado) {
				// Eliminamos el cliente con la instalacion cancelada
				tablaInstalacion.getItems().removeAll(cliente);

				// Insercion en tabla de instalaciones
				DataBase db = new DataBase();
				String sql = "INSERT INTO instalacion (cliente_documento, instalacion_alta) VALUES (?,CURRENT_DATE());";
				db.setQuery(sql);
				db.setParametro(1, cliente.getDocumento());
				db.execute();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Error");
				alert.setHeaderText("Instalacion finalizada,sigue asi!");
				alert.showAndWait();

				return;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("No se pudo realizar el cambio de estado del servicio");
				alert.showAndWait();

				return;
			}
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

}
