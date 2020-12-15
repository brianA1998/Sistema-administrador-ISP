package servicios;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.DataBase;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import modelo.Servicio;

public class TablaServicio {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

	@FXML
	private TableView<Servicio> tablaServicio;

	@FXML
	private TableColumn<Servicio, String> colNombre;

	@FXML
	private TableColumn<Servicio, String> colSubida;

	@FXML
	private TableColumn<Servicio, String> colBajada;

	@FXML
	private TableColumn<Servicio, Float> colMonto;

	@FXML
	private TableColumn<Servicio, Integer> colIva;

	@FXML
	private TableColumn<Servicio, String> colTipoServicio;

	@FXML
	private TableColumn<Servicio, String> colMoneda;

	@FXML
	private TableColumn<Servicio, String> colRubro;

	@FXML
	private TableColumn<Servicio, String> colCodigo;

	@FXML
	private Button btnEditar;

	@FXML
	private Button btnEliminar;

	@FXML
	void editarServicio(ActionEvent event) {

		// Cargamos el controlador
		Servicio servicio = tablaServicio.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun servicio de la tabla de servicios
		if (servicio == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun servicio");
			alert.showAndWait();

			return;
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/servicio/Modificar.fxml"));

			try {
				while (contenedor.getChildren().size() > 0)
					contenedor.getChildren().remove(0);

				Pane root = (Pane) loader.load();
				contenedor.getChildren().add(root);

				String nombreServicio = servicio.getNombre();
				ModificarServicios modificarServicio = (ModificarServicios) loader.getController();
				modificarServicio.cargarDatosServicio(nombreServicio);

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	@FXML
	void eliminarServicio(ActionEvent event) {
		eliminarServicio();
	}

	@FXML
	void initialize() {

		setIconTablaServicio();

		// LLENAMOS LAS FILAS CON DATOS DE BD
		colNombre.setCellValueFactory(new PropertyValueFactory<Servicio, String>("nombre"));
		colCodigo.setCellValueFactory(new PropertyValueFactory<Servicio, String>("codigo"));
		colSubida.setCellValueFactory(new PropertyValueFactory<Servicio, String>("subida"));
		colBajada.setCellValueFactory(new PropertyValueFactory<Servicio, String>("bajada"));
		colMonto.setCellValueFactory(new PropertyValueFactory<Servicio, Float>("monto"));
		colIva.setCellValueFactory(new PropertyValueFactory<Servicio, Integer>("Iva"));
		colTipoServicio.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoServicio"));
		colMoneda.setCellValueFactory(new PropertyValueFactory<Servicio, String>("tipoMoneda"));
		colRubro.setCellValueFactory(new PropertyValueFactory<Servicio, String>("rubro"));

		// CONSULTA SQL
		DataBase db = new DataBase();
		String sql = "SELECT s.servicio_nombre, s.servicio_codigo,s.servicio_subida,s.servicio_bajada,s.servicio_monto,s.servicio_iva, ts.tipo_servicio_nombre , tm.tipo_moneda_codigo, rs.rubro_nombre FROM servicio s INNER JOIN tipo_servicio ts ON s.tipo_servicio_id = ts.tipo_servicio_id INNER JOIN tipo_moneda tm ON s.tipo_moneda_id = tm.tipo_moneda_id INNER JOIN rubro_servicio rs ON s.rubro_id = rs.rubro_id WHERE servicio_estado = 1";

		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			while (rs.next()) {
				Servicio servicio = new Servicio();

				servicio.setNombre(rs.getString(1));
				servicio.setCodigo(rs.getString(2));
				servicio.setSubida(rs.getString(3));
				servicio.setBajada(rs.getString(4));
				servicio.setMonto(rs.getFloat(5));
				servicio.setIva(rs.getFloat(6));
				servicio.setTipoServicio(rs.getString(7));
				servicio.setTipoMoneda(rs.getString(8));
				servicio.setRubro(rs.getString(9));

				tablaServicio.getItems().add(servicio);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		controlBotones();

		// menu de click secundario
		ContextMenu menu = new ContextMenu();
		MenuItem baja = new MenuItem("Eliminar servicio");
		MenuItem modificar = new MenuItem("Modificar datos");

		menu.getItems().addAll(baja, modificar);

		// eventos de click secundario
		baja.setOnAction(e -> eliminarServicio(e));
		modificar.setOnAction(e -> editarServicio(e));

		// agregar el menu
		tablaServicio.setContextMenu(menu);

	}

	/**
	 * Establece los iconos del boton de editar y eliminar de la tabla de servicio
	 */
	public void setIconTablaServicio() {
		// Establece el icono del boton eliminar
		File archivo = new File("imagenes" + File.separator + "cancelar.png");
		Image imagen = new Image(archivo.toURI().toString(), 50, 50, true, true, true);
		btnEliminar.setGraphic(new ImageView(imagen));
		btnEliminar.setContentDisplay(ContentDisplay.CENTER);

		// Establece el icono del boton editar
		File archivo1 = new File("imagenes" + File.separator + "lapices.png");
		Image imagen1 = new Image(archivo1.toURI().toString(), 50, 50, true, true, true);
		btnEditar.setGraphic(new ImageView(imagen1));
		btnEditar.setContentDisplay(ContentDisplay.CENTER);
	}

	/**
	 * Permite controlar que se seleccione primero un cliente y despues habilitar el
	 * boton de eliminar o modificar
	 */
	private void controlBotones() {

		tablaServicio.setOnMouseClicked(event -> {
			if (event.getClickCount() == 1) {
				btnEditar.setDisable(false);
				btnEliminar.setDisable(false);
				return;
			}
		});

	}

	/**
	 * Permite eliminar un servicio
	 */
	private void eliminarServicio() {

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de cargar el servicio?");

		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {
				Servicio servicio = tablaServicio.getSelectionModel().getSelectedItem();

				// Se controla de que se seleccione algun servicio de la tabla de servicios
				if (servicio == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun servicio");
					alert.showAndWait();

					return;
				} else {
					boolean resultado = servicio.delete();

					if (resultado) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Exito");
						alert.setHeaderText("Se cambio el estado del servicio exitosamente");
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

		});

	}

	/**
	 * @return the colRubro
	 */
	public TableColumn<Servicio, String> getColRubro() {
		return colRubro;
	}

	/**
	 * @param colRubro the colRubro to set
	 */
	public void setColRubro(TableColumn<Servicio, String> colRubro) {
		this.colRubro = colRubro;
	}

	/**
	 * @return the colCodigo
	 */
	public TableColumn<Servicio, String> getColCodigo() {
		return colCodigo;
	}

	/**
	 * @param colCodigo the colCodigo to set
	 */
	public void setColCodigo(TableColumn<Servicio, String> colCodigo) {
		this.colCodigo = colCodigo;
	}

	/**
	 * @return the resources
	 */
	public ResourceBundle getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	/**
	 * @return the location
	 */
	public URL getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(URL location) {
		this.location = location;
	}

	/**
	 * @return the contenedor
	 */
	public Pane getContenedor() {
		return contenedor;
	}

	/**
	 * @param contenedor the contenedor to set
	 */
	public void setContenedor(Pane contenedor) {
		this.contenedor = contenedor;
	}

	/**
	 * @return the tablaServicio
	 */
	public TableView<Servicio> getTablaServicio() {
		return tablaServicio;
	}

	/**
	 * @param tablaServicio the tablaServicio to set
	 */
	public void setTablaServicio(TableView<Servicio> tablaServicio) {
		this.tablaServicio = tablaServicio;
	}

	/**
	 * @return the colNombre
	 */
	public TableColumn<Servicio, String> getColNombre() {
		return colNombre;
	}

	/**
	 * @param colNombre the colNombre to set
	 */
	public void setColNombre(TableColumn<Servicio, String> colNombre) {
		this.colNombre = colNombre;
	}

	/**
	 * @return the colSubida
	 */
	public TableColumn<Servicio, String> getColSubida() {
		return colSubida;
	}

	/**
	 * @param colSubida the colSubida to set
	 */
	public void setColSubida(TableColumn<Servicio, String> colSubida) {
		this.colSubida = colSubida;
	}

	/**
	 * @return the colBajada
	 */
	public TableColumn<Servicio, String> getColBajada() {
		return colBajada;
	}

	/**
	 * @param colBajada the colBajada to set
	 */
	public void setColBajada(TableColumn<Servicio, String> colBajada) {
		this.colBajada = colBajada;
	}

	/**
	 * @return the colMonto
	 */
	public TableColumn<Servicio, Float> getColMonto() {
		return colMonto;
	}

	/**
	 * @param colMonto the colMonto to set
	 */
	public void setColMonto(TableColumn<Servicio, Float> colMonto) {
		this.colMonto = colMonto;
	}

	/**
	 * @return the colIva
	 */
	public TableColumn<Servicio, Integer> getColIva() {
		return colIva;
	}

	/**
	 * @param colIva the colIva to set
	 */
	public void setColIva(TableColumn<Servicio, Integer> colIva) {
		this.colIva = colIva;
	}

	/**
	 * @return the colTipoServicio
	 */
	public TableColumn<Servicio, String> getColTipoServicio() {
		return colTipoServicio;
	}

	/**
	 * @param colTipoServicio the colTipoServicio to set
	 */
	public void setColTipoServicio(TableColumn<Servicio, String> colTipoServicio) {
		this.colTipoServicio = colTipoServicio;
	}

	/**
	 * @return the colMoneda
	 */
	public TableColumn<Servicio, String> getColMoneda() {
		return colMoneda;
	}

	/**
	 * @param colMoneda the colMoneda to set
	 */
	public void setColMoneda(TableColumn<Servicio, String> colMoneda) {
		this.colMoneda = colMoneda;
	}

	/**
	 * @return the btnEditar
	 */
	public Button getBtnEditar() {
		return btnEditar;
	}

	/**
	 * @param btnEditar the btnEditar to set
	 */
	public void setBtnEditar(Button btnEditar) {
		this.btnEditar = btnEditar;
	}

	/**
	 * @return the btnEliminar
	 */
	public Button getBtnEliminar() {
		return btnEliminar;
	}

	/**
	 * @param btnEliminar the btnEliminar to set
	 */
	public void setBtnEliminar(Button btnEliminar) {
		this.btnEliminar = btnEliminar;
	}
}
