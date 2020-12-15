package clientes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Cliente;

public class InformacionRapida {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtFechaNacimiento;

	@FXML
	private TextField txtDocumento;

	@FXML
	private TextField txtNumeroAbonado;

	@FXML
	private TextField txtFechaAlta;

	@FXML
	private TextField txtCelular;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtCorreo;

	@FXML
	private TextField txtCiudad;

	@FXML
	private TextField txtBarrio;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtManzana;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private TextField txtMonoblock;

	@FXML
	private TextField txtLugarTrabajo;

	@FXML
	private TextField txtTipoTrabajo;

	@FXML
	private TextField txtPuestoTrabajo;

	@FXML
	private TextField txtIva;

	@FXML
	private TextField txtSitioWeb;

	@FXML
	private TextField txtTwitter;

	@FXML
	private TextField txtInstagram;

	@FXML
	private TextField txtFacebook;

	@FXML
	private TextField txtLinkedin;

	@FXML
	public void initialize() {
	}

	/**
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * usuario especifico mediante el documento de un usuario especifico
	 * 
	 * @param numero numero de documento del usuario
	 */
	public void cargarDatosCliente(long numero) {
		Cliente cliente = new Cliente(numero);

		// Llena todos los campos de texto con los campos
		txtNombre.setText(cliente.getNombre());
		txtApellido.setText(cliente.getApellido());
		txtDocumento.setText("" + numero);
		txtFechaNacimiento.setText((cliente.getNacimiento() == null) ? "" : cliente.getNacimiento().toString());
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
		txtCelular.setText("" + cliente.getCelular());
		if (cliente.getTelefono() == 0) {
			txtTelefono.setText("");
		} else {
			txtTelefono.setText("" + cliente.getTelefono());
		}
		txtCorreo.setText(cliente.getCorreo());
		txtNumeroAbonado.setText("" + cliente.getNumeroAbonado());
		txtFechaAlta.setText(cliente.getIngreso().toString());
		txtLugarTrabajo.setText(cliente.getLugar());
		txtTipoTrabajo.setText(cliente.getSituacion());
		txtPuestoTrabajo.setText(cliente.getPuesto());
		txtIva.setText(cliente.getIva());
		txtSitioWeb.setText(cliente.getWeb());
		txtTwitter.setText(cliente.getTwitter());
		txtInstagram.setText(cliente.getInstagram());
		txtFacebook.setText(cliente.getFacebook());
		txtLinkedin.setText(cliente.getLinkedin());
		txtCiudad.setText(cliente.getDireccion().getCiudad());
		txtBarrio.setText(cliente.getDireccion().getBarrio());

	}

	/**
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * usuario especifico mediante el documento de un usuario especifico
	 * 
	 * @param numero numero de documento del usuario
	 */
	public void cargarDatosEmpresa(long numero) {

		Cliente cliente = new Cliente(numero);

		// Llena todos los campos de texto con los campos
		txtNombre.setText(cliente.getNombre());
		txtDocumento.setText("" + numero);
		txtCelular.setText("" + cliente.getCelular());
		if (cliente.getTelefono() == 0) {
			txtTelefono.setText("");
		} else {
			txtTelefono.setText("" + cliente.getTelefono());
		}
		txtCorreo.setText(cliente.getCorreo());
		txtIva.setText(cliente.getIva());
		txtCiudad.setText(cliente.getDireccion().getCiudad());
		txtBarrio.setText(cliente.getDireccion().getBarrio());
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
		txtSitioWeb.setText(cliente.getWeb());
		txtTwitter.setText(cliente.getTwitter());
		txtInstagram.setText(cliente.getInstagram());
		txtFacebook.setText(cliente.getFacebook());
		txtLinkedin.setText(cliente.getLinkedin());
		txtNumeroAbonado.setText("" + cliente.getNumeroAbonado());
		txtFechaAlta.setText(cliente.getIngreso().toString());
	}

}
