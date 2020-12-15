package ajustes;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import controlador.Validar;
import db.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import modelo.Direccion;

public class Ajustes {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

	@FXML
	private TableView<Direccion> tablaProvincia;

	@FXML
	private TableColumn<Direccion, String> colNombreProvincia;

	@FXML
	private TextField txtProvincia;

	@FXML
	private Button btnGuardarProvincia;

	@FXML
	private TableView<Direccion> tablaCiudad;

	@FXML
	private Button btnEditarProvincia;

	@FXML
	private Button btnAgregarProvincia;

	@FXML
	private TableColumn<Direccion, String> colNombreCiudad;

	@FXML
	private TableColumn<Direccion, Integer> colCodigoPostal;

	@FXML
	private TableColumn<Direccion, String> colProvincia;

	@FXML
	private TextField txtCiudad;

	@FXML
	private Button btnGuardarCiudad;

	@FXML
	private TextField txtCodigoPostal;

	@FXML
	private ComboBox<String> comboProvincia;

	private Direccion provincia;

	private Direccion ciudad;

	@FXML
	void guardarCiudad(ActionEvent event) {

		// desabilita el boton, evita el doble click
		btnGuardarCiudad.setDisable(true);
		txtCiudad.setDisable(true);
		txtCodigoPostal.setDisable(true);
		comboProvincia.setDisable(true);

		// Obtenemos los datos
		String provincia = getSelectedItem(comboProvincia);
		String nombreCiudad = txtCiudad.getText();
		int codigoPostal = getInt(txtCodigoPostal.getText());

		// valida los datos

		nombreCiudad = (nombreCiudad.length() > 0) ? nombreCiudad : null;

		if (provincia == null || nombreCiudad == null || codigoPostal == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardarCiudad.setDisable(false);
			return;
		}

		Direccion ciudad = new Direccion(nombreCiudad, codigoPostal, provincia);

		if (!ciudad.insertarCiudad()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la insercion de datos");
			alert.showAndWait();
		} else {
			vaciarFormulario();
		}

		tablaCiudad.getItems().addAll(ciudad);
		txtCiudad.setText("");
		txtCodigoPostal.setText("");
		btnGuardarCiudad.setDisable(false); // se vuelve a habilitar el boton

	}

	@FXML
	void agregarCiudad(ActionEvent event) {
		txtCiudad.setText("");
		txtCodigoPostal.setText("");
		comboProvincia.getSelectionModel().select(null);
		txtCiudad.setDisable(false);
		txtCodigoPostal.setDisable(false);
		comboProvincia.setDisable(false);
		btnGuardarCiudad.setDisable(false);
		btnGuardarCiudad.setOnAction(e -> guardarCiudad(e));
	}

	@FXML
	void editarCiudad(ActionEvent event) {

		txtCiudad.setDisable(false);
		txtCodigoPostal.setDisable(false);
		comboProvincia.setDisable(false);
		btnGuardarCiudad.setDisable(false);
		btnGuardarCiudad.setOnAction(e -> modificarCiudad());
	}

	/**
	 * Permite modificar los datos de una ciudad
	 */
	void modificarCiudad() {
		String ciudadUpdate = txtCiudad.getText();
		int codigoPostalUpdate = getInt(txtCodigoPostal.getText());
		String provinciaUpdate = getSelectedItem(comboProvincia);

		btnGuardarProvincia.setDisable(true);
		txtProvincia.setDisable(true);

		// Consulta
		DataBase db = DataBase.getInstancia();
		String sql = "UPDATE ciudad SET ciudad_nombre = ?,ciudad_codigo_postal = ?,provincia_id = ?  WHERE ciudad_id = ?";
		db.setQuery(sql);
		db.setParametro(1, ciudadUpdate);
		db.setParametro(2, codigoPostalUpdate);
		db.setParametro(3, Direccion.getProvinciaId(provinciaUpdate));
		db.setParametro(4, Direccion.getCiudadId(ciudad.getCiudad()));

		// Ejecuto el update
		boolean resultado = db.execute();
		db.close();

		if (resultado) {
			// si se guardo
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exito");
			alert.setHeaderText("Se ha actualizado los datos de la ciudad con exito");
			alert.showAndWait();
			vaciarFormulario();
		} else {
			// si ocurre un error
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la actualizacion de datos");
			alert.showAndWait();
		}
	}

	@FXML
	void guardarProvincia(ActionEvent event) {

		// desabilita el boton, evita el doble click

		txtProvincia.setDisable(true);
		btnGuardarProvincia.setDisable(true);

		// Obtenemos los datos
		String nombreProvincia = txtProvincia.getText();

		// valida los datos
		nombreProvincia = (nombreProvincia.length() > 0) ? nombreProvincia : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombreProvincia == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();

			return;
		}

		Direccion provincia = new Direccion(nombreProvincia);
		if (!provincia.insertarProvincia()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la insercion de datos");
			alert.showAndWait();
		} else {
			txtProvincia.setText("");
		}

		tablaProvincia.getItems().addAll(provincia);
		txtProvincia.setText("");
		btnGuardarProvincia.setDisable(false); // se vuelve a habilitar el boton

	}

	@FXML
	void agregarProvincia(ActionEvent event) {
		txtProvincia.setText("");
		txtProvincia.setDisable(false);
		btnGuardarProvincia.setDisable(false);
		btnGuardarProvincia.setOnAction(e -> guardarProvincia(e));
	}

	@FXML
	void editarProvincia(ActionEvent event) {
		txtProvincia.setDisable(false);
		btnGuardarProvincia.setDisable(false);
		btnGuardarProvincia.setOnAction(e -> modificarProvincia());
	}

	/**
	 * Permite actualizar los datos de una provincia en la base de datos
	 */
	void modificarProvincia() {

		String provinciaUpdate = txtProvincia.getText();
		btnGuardarProvincia.setDisable(true);
		txtProvincia.setDisable(true);

		// Consulta
		DataBase db = new DataBase();
		String sql = "UPDATE provincia SET provincia_nombre = ? WHERE provincia_id = ?";
		db.setQuery(sql);
		db.setParametro(1, provinciaUpdate);
		db.setParametro(2, Direccion.getProvinciaId(provincia.getProvincia()));

		// Ejecuto el update
		boolean resultado = db.execute();
		db.close();

		if (resultado) {
			// si se guardo
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exito");
			alert.setHeaderText("Se ha actualizado los datos de la provincia con exito");
			alert.showAndWait();
			txtProvincia.setText("");
		} else {
			// si ocurre un error
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la actualizacion de datos");
			alert.showAndWait();
		}

	}

	@FXML
	void initialize() {
		// Validacion de caracteres correctos
		txtProvincia.setOnKeyTyped(Validar.texts());
		txtCiudad.setOnKeyTyped(Validar.texts());
		txtCodigoPostal.setOnKeyTyped(Validar.numbers());
		comboProvincia.setItems(Direccion.llenarProvincias());

		colNombreCiudad.setCellValueFactory(new PropertyValueFactory<Direccion, String>("ciudad"));
		colCodigoPostal.setCellValueFactory(new PropertyValueFactory<Direccion, Integer>("codigoPostal"));
		colProvincia.setCellValueFactory(new PropertyValueFactory<Direccion, String>("provincia"));

		colNombreProvincia.setCellValueFactory(new PropertyValueFactory<Direccion, String>("provincia"));

		llenarTablaProvincia();
		llenarTablaCiudad();

		tablaProvincia.setOnMouseClicked(event -> {
			provincia = tablaProvincia.getSelectionModel().getSelectedItem();

			txtProvincia.setText(provincia.getProvincia());

		});

		tablaCiudad.setOnMouseClicked(event -> {
			ciudad = tablaCiudad.getSelectionModel().getSelectedItem();

			txtCiudad.setText(ciudad.getCiudad());
			comboProvincia.getSelectionModel().select(ciudad.getProvincia());
			txtCodigoPostal.setText("" + ciudad.getCodigoPostal());
		});

	}

	/**
	 * Permite llenar la tabla de provincias con el nombre de cada una
	 */
	public void llenarTablaProvincia() {
		String sql = "SELECT provincia_nombre FROM provincia ORDER BY provincia_nombre";
		DataBase db = new DataBase();
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			rs.last();
			Direccion[] direccion = new Direccion[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				direccion[i] = new Direccion();
				direccion[i].setProvincia(rs.getString(1));

			}

			tablaProvincia.getItems().setAll(direccion);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void llenarTablaCiudad() {
		String sql = "SELECT C.ciudad_nombre,C.ciudad_codigo_postal,P.provincia_nombre FROM ciudad C INNER JOIN provincia P ON P.provincia_id = C.provincia_id ORDER BY C.ciudad_nombre";
		DataBase db = new DataBase();
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			rs.last();
			Direccion[] direccion = new Direccion[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				direccion[i] = new Direccion();
				direccion[i].setCiudad(rs.getString(1));
				direccion[i].setCodigoPostal(rs.getInt(2));
				direccion[i].setProvincia(rs.getString(3));
			}

			tablaCiudad.getItems().setAll(direccion);
		} catch (SQLException e) {

			e.printStackTrace();
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
	 * Convierte numero de String a int
	 * 
	 * @param numero a convertir a int
	 * @return el número o -1 en caso de que no haya numero
	 */
	private int getInt(String numero) {
		if (numero == null)
			return -1;

		return (numero.length() > 0) ? Integer.parseInt(numero) : -1;
	}

	/**
	 * Permite vaciar el formulario
	 */
	private void vaciarFormulario() {
		txtCiudad.setText("");
		txtCodigoPostal.setText("");
		comboProvincia.getSelectionModel().select(null);
	}
}
