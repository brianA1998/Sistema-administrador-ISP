package clientes;

import java.net.URL;
import java.util.ResourceBundle;

import controlador.Validar;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import modelo.Cliente;
import modelo.Direccion;

public class ModificarEmpresa {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtRazonSocial;

	@FXML
	private TextField txtCuit;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtCelular;

	@FXML
	private TextField txtCorreo;

	@FXML
	private ComboBox<String> comboIva;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private ComboBox<String> comboBarrio;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtManzana;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private TextField txtMonoblock;

	@FXML
	private TextField txtWeb;

	@FXML
	private TextField txtInstagram;

	@FXML
	private TextField txtFacebook;

	@FXML
	private TextField txtLinkedin;

	@FXML
	private TextField txtTwitter;

	@FXML
	private Button btnGuardar;

	private long cuit;

	@FXML
	void guardarEmpresa(ActionEvent event) {
		btnGuardar.setDisable(true);

		// OBTENEMOS LOS DATOS
		String razonSocial = txtRazonSocial.getText();
		long cuit = Validar.numberCuit(txtCuit.getText());
		long telefono = Validar.numberPhone(txtTelefono.getText());
		long celular = Validar.numberPhone(txtCelular.getText());
		String correo = Validar.emailAddress(txtCorreo.getText());
		String iva = getSelectedItem(comboIva);
		String ciudad = getSelectedItem(comboCiudad);
		String barrio = getSelectedItem(comboBarrio);
		String domicilio = txtDomicilio.getText();
		int manzana = getInt(txtManzana.getText());
		String departamento = txtDepartamento.getText();
		int monoblock = getInt(txtMonoblock.getText());
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

			btnGuardar.setDisable(false);
			return;
		}

		// Validacion de cuit de empresa
		if (cuit == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar el cuit");
			alert.showAndWait();

			btnGuardar.setDisable(false);
			return;
		}

		// Validamos los datos
		razonSocial = (razonSocial.length() > 0) ? razonSocial : null;
		domicilio = (domicilio.length() > 0) ? domicilio : null;

		// Si hubo un dato no ingresado sale sin guardar
		if (razonSocial == null || cuit == -1L || domicilio == null || celular == -1L || ciudad == null
				|| barrio == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		Direccion direccion = new Direccion(domicilio, ciudad, barrio, departamento, monoblock, manzana);

		Cliente cliente = new Cliente(razonSocial, cuit, direccion, telefono, celular, correo, null, iva, twitter, web,
				instagram, facebook, linkedin);

		if (cliente.update(cuit)) { // si se guardo
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
		// Control de entrada de caracteres
		txtRazonSocial.setOnKeyTyped(Validar.texts());
		txtCuit.setOnKeyTyped(Validar.numbers());
		txtCelular.setOnKeyTyped(Validar.numbers());
		txtTelefono.setOnKeyTyped(Validar.numbers());
		txtCorreo.setOnKeyTyped(Validar.email());
		txtDomicilio.setOnKeyTyped(Validar.alphanumeric());
		txtMonoblock.setOnKeyTyped(Validar.numbers());
		txtDepartamento.setOnKeyTyped(Validar.alphanumeric());
		txtManzana.setOnKeyTyped(Validar.numbers());

		// llena los combos
		comboCiudad.setItems(Cliente.llenarCiudades());
		comboBarrio.setItems(Cliente.llenarBarrios());
		comboIva.setItems(Cliente.llenarIvaCliente());

		comboCiudad.setOnAction(e -> cargarComboBarrio());
	}

	/**
	 * 
	 */
	public void cargarComboBarrio() {
		int ciudadId = Direccion.getCiudadId(getSelectedItem(comboCiudad));
		comboBarrio.setItems(Direccion.llenarBarrioFiltrado(ciudadId));
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
	 * Limpia todos los campos del formulario
	 */
	private void vaciarFormulario() {
		txtCelular.setText("");
		txtCorreo.setText("");
		txtCuit.setText("");

		txtDomicilio.setText("");
		txtFacebook.setText("");
		txtInstagram.setText("");
		txtLinkedin.setText("");
		txtManzana.setText("");
		txtMonoblock.setText("");
		txtRazonSocial.setText("");
		txtTelefono.setText("");
		txtTwitter.setText("");
		txtWeb.setText("");
		comboBarrio.getSelectionModel().select(0);
		comboCiudad.getSelectionModel().select(0);
		comboIva.getSelectionModel().select(null);

	}

	/**
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * cliente especifico mediante el documento de un cliente especifico
	 * 
	 * @param numero numero de documento del cliente
	 */
	public void cargarDatosEmpresa(long numero) {
		Cliente cliente = new Cliente(numero);
		this.cuit = numero;

		txtRazonSocial.setText(cliente.getNombre());
		txtCuit.setText("" + cliente.getDocumento());
		if (cliente.getTelefono() == 0) {
			txtTelefono.setText("");
		} else {
			txtTelefono.setText("" + cliente.getTelefono());
		}
		txtCelular.setText("" + cliente.getCelular());
		txtCorreo.setText(cliente.getCorreo());
		txtDomicilio.setText(cliente.getDireccion().getDomicilio());
		if (cliente.getDireccion().getManzana() == 0) {
			txtManzana.setText("");
		} else {

			txtManzana.setText("" + cliente.getDireccion().getManzana());
		}

		if (cliente.getDireccion().getMonoblock() == 0) {
			txtMonoblock.setText("");
		} else {

			txtMonoblock.setText("" + cliente.getDireccion().getMonoblock());
		}

		txtDepartamento.setText(cliente.getDireccion().getDepartamento());

		txtWeb.setText(cliente.getWeb());
		txtTwitter.setText(cliente.getTwitter());
		txtInstagram.setText(cliente.getInstagram());
		txtFacebook.setText(cliente.getFacebook());
		txtLinkedin.setText(cliente.getLinkedin());

		// selecciona el elemento del combo
		comboBarrio.getSelectionModel().select(cliente.getDireccion().getBarrio());
		comboCiudad.getSelectionModel().select(cliente.getDireccion().getCiudad());
		comboIva.getSelectionModel().select(cliente.getIva());

	}
}
