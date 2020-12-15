package clientes;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import controlador.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Cliente;
import modelo.Direccion;

public class ModificarClientes {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextField txtNombreCliente;

	@FXML
	private TextField txtApellidoCliente;

	@FXML
	private TextField txtTelCliente;

	@FXML
	private TextField txtCelCliente;

	@FXML
	private DatePicker CalendarFechaNac;

	@FXML
	private TextField txtCorreoCliente;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private ComboBox<String> comboBarrio;

	@FXML
	private TextField txtDomicilioCliente;

	@FXML
	private TextField txtManzanaCliente;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private TextField txtMonoblockCliente;

	@FXML
	private ComboBox<String> comboSituacionLaboral;

	@FXML
	private ComboBox<String> comboIva;

	@FXML
	private TextField txtPuesto;
	@FXML
	private TextField txtLugar;

	@FXML
	private TextField txtWeb;

	@FXML
	private TextField txtTwitter;

	@FXML
	private TextField txtFacebook;

	@FXML
	private TextField txtInstagram;

	@FXML
	private TextField txtLinkedin;

	@FXML
	private Button btnGuardar;

	private long documento;

	@FXML
	void guardarCliente(ActionEvent event) {
		btnGuardar.setDisable(true);

		// OBTENER DATOS
		String nombre = txtNombreCliente.getText();
		String apellido = txtApellidoCliente.getText();
		String domicilio = txtDomicilioCliente.getText();
		int manzana = getInt(txtManzanaCliente.getText());
		int monoblock = getInt(txtMonoblockCliente.getText());
		long telefono = Validar.numberPhone(txtTelCliente.getText());
		long celular = Validar.numberPhone(txtCelCliente.getText());
		String correo = Validar.emailAddress(txtCorreoCliente.getText());
		LocalDate nacimiento = CalendarFechaNac.getValue();
		String barrio = getSelectedItem(comboBarrio);
		String ciudad = getSelectedItem(comboCiudad);
		String departamento = txtDepartamento.getText();

		String situacion = getSelectedItem(comboSituacionLaboral);
		String lugar = txtLugar.getText();
		String puesto = txtPuesto.getText();
		String Iva = getSelectedItem(comboIva);
		String twitter = txtTwitter.getText();
		String web = txtWeb.getText();
		String instagram = txtInstagram.getText();
		String facebook = txtFacebook.getText();
		String linkedin = txtLinkedin.getText();

		// Valida celular cliente
		if (celular == -1L) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("error al ingresar el celular");
			alert.showAndWait();

			btnGuardar.setDisable(false);
			return;
		}

		// Validacion de correo electronico

		if (correo == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar el correo");
			alert.showAndWait();
			txtCorreoCliente.setText("");
			btnGuardar.setDisable(false);
			return;
		}

		// valida los datos
		nombre = (nombre.length() > 0) ? nombre : null;

		domicilio = (domicilio.length() > 0) ? domicilio : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombre == null || domicilio == null || celular == -1L || ciudad == null || barrio == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		Direccion direccion = new Direccion(domicilio, ciudad, barrio, departamento, monoblock, manzana);

		Cliente cliente = new Cliente(nombre, apellido, documento, direccion, telefono, celular, correo, nacimiento,
				null, situacion, lugar, puesto, Iva, twitter, web, instagram, facebook, linkedin);

		if (cliente.update(documento)) { // si se guardo
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Exito");
			alert.setHeaderText("Se ha actualizado los datos del cliente con exito");
			alert.showAndWait();
			vaciarFormulario();
		} else {
			// si ocurre un error
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la actualizacion de datos");
			alert.showAndWait();
		}
		btnGuardar.setDisable(false); // se vuelve a habilitar el boton

	}

	@FXML
	void initialize() {

		// llena los combos
		comboCiudad.setItems(Cliente.llenarCiudades());
		comboBarrio.setItems(Cliente.llenarBarrios());
		comboIva.setItems(Cliente.llenarIvaCliente());
		comboSituacionLaboral.setItems(Cliente.llenarSituacionLaboral());

		// Control de entrada de caracteres
		txtNombreCliente.setOnKeyTyped(Validar.texts());
		txtApellidoCliente.setOnKeyTyped(Validar.texts());
		txtDomicilioCliente.setOnKeyTyped(Validar.alphanumeric());
		txtMonoblockCliente.setOnKeyTyped(Validar.numbers());
		txtManzanaCliente.setOnKeyTyped(Validar.numbers());
		txtCelCliente.setOnKeyTyped(Validar.numbers());
		txtTelCliente.setOnKeyTyped(Validar.numbers());
		txtDepartamento.setOnKeyTyped(Validar.alphanumeric());
		txtCorreoCliente.setOnKeyTyped(Validar.email());
		txtFacebook.setOnKeyTyped(Validar.email());
		txtLugar.setOnKeyTyped(Validar.texts());
		txtPuesto.setOnKeyTyped(Validar.texts());

	}

	/**
	 * Convierte numero de String a Int
	 * 
	 * @param numero a convertir a Int
	 * @return int
	 */
	private int getInt(String numero) {
		if (numero == null)
			return (int) -1L;

		return (int) ((numero.length() > 0) ? Integer.parseInt(numero) : -1L);
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
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * cliente especifico mediante el documento de un cliente especifico
	 * 
	 * @param numero numero de documento del cliente
	 */
	public void cargarDatosCliente(long numero) {
		Cliente cliente = new Cliente(numero);
		this.documento = numero;

		// Llena todos los campos de texto con los campos
		txtNombreCliente.setText(cliente.getNombre());
		txtApellidoCliente.setText(cliente.getApellido());
		CalendarFechaNac.setValue(cliente.getNacimiento());

		txtDomicilioCliente.setText(cliente.getDireccion().getDomicilio());
		if (cliente.getDireccion().getManzana() == 0) {
			txtManzanaCliente.setText("");
		} else {

			txtManzanaCliente.setText("" + cliente.getDireccion().getManzana());
		}

		if (cliente.getDireccion().getMonoblock() == 0) {
			txtMonoblockCliente.setText("");
		} else {

			txtMonoblockCliente.setText("" + cliente.getDireccion().getMonoblock());
		}

		txtDepartamento.setText(cliente.getDireccion().getDepartamento());
		txtCelCliente.setText("" + cliente.getCelular());
		if (cliente.getTelefono() == 0) {
			txtTelCliente.setText("");
		} else {
			txtTelCliente.setText("" + cliente.getTelefono());
		}

		txtCorreoCliente.setText(cliente.getCorreo());
		txtLugar.setText(cliente.getLugar());
		txtPuesto.setText(cliente.getPuesto());

		txtWeb.setText(cliente.getWeb());
		txtTwitter.setText(cliente.getTwitter());
		txtInstagram.setText(cliente.getInstagram());
		txtFacebook.setText(cliente.getFacebook());
		txtLinkedin.setText(cliente.getLinkedin());
		// selecciona el elemento del combo
		comboBarrio.getSelectionModel().select(cliente.getDireccion().getBarrio());
		comboCiudad.getSelectionModel().select(cliente.getDireccion().getCiudad());
		comboIva.getSelectionModel().select(cliente.getIva());
		comboSituacionLaboral.getSelectionModel().select(cliente.getSituacion());

	}

	/**
	 * borra los datos del formulario
	 */
	private void vaciarFormulario() {
		txtNombreCliente.setText("");
		txtApellidoCliente.setText("");
		txtCorreoCliente.setText("");
		txtDomicilioCliente.setText("");
		txtMonoblockCliente.setText("");
		txtDepartamento.setText("");
		txtManzanaCliente.setText("");
		txtCelCliente.setText("");
		txtTelCliente.setText("");
		comboCiudad.getSelectionModel().select(null);
		comboBarrio.getSelectionModel().select(null);
		comboIva.getSelectionModel().select(null);
		comboSituacionLaboral.getSelectionModel().select(null);
		txtFacebook.setText("");
		txtInstagram.setText("");
		txtLinkedin.setText("");
		txtLugar.setText("");
		txtPuesto.setText("");
		txtTwitter.setText("");
		txtWeb.setText("");
		CalendarFechaNac.setValue(null);
	}

}
