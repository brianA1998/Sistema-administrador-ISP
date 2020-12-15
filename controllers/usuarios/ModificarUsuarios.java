package usuarios;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import controlador.Validar;
import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import modelo.Direccion;
import modelo.Usuario;

public class ModificarUsuarios {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField txtCorreo;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtDocumento;

	@FXML
	private TextField txtSalario;

	@FXML
	private DatePicker dateNacimiento;

	@FXML
	private DatePicker dateIngreso;

	@FXML
	private TextField txtUser;

	@FXML
	private PasswordField txtPass;

	@FXML
	private Button btnGuardar;

	private long documento;

	private String passEncrypted;

	private String user;

	@FXML
	void guardarUsuario(ActionEvent event) {

		// desabilita el boton, evita el doble click
		btnGuardar.setDisable(true);

		// Obtenemos los datos
		float salario = getFloat(txtSalario.getText());
		long telefono = Validar.numberPhone(txtTelefono.getText());
		String nombre = txtNombre.getText();
		String apellido = txtApellido.getText();
		String domicilio = txtDomicilio.getText();
		String ciudad = getSelectedItem(comboCiudad);
		String correo = Validar.emailAddress(txtCorreo.getText());
		user = txtUser.getText();
		String pass = txtPass.getText();
		LocalDate nacimiento = dateNacimiento.getValue();
		LocalDate ingreso = dateNacimiento.getValue();
		Direccion direccion = new Direccion(domicilio, ciudad, null);

		// valida los datos
		nombre = (nombre.length() > 0) ? nombre : null;
		apellido = (apellido.length() > 0) ? apellido : null;

		user = (user.length() > 0) ? user : null;
		pass = (pass.length() > 0) ? pass : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombre == null || apellido == null || user == null || pass == null || ciudad == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		// Valida celular usuario
		if (telefono == -1L) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("error al ingresar el telefono");
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
			txtCorreo.setText("");
			btnGuardar.setDisable(false);
			return;
		}

		// encripta la contraseña
		byte[] contra;
		passEncrypted = null;
		try {
			contra = txtPass.getText().getBytes("UTF-8");
			SecretKeySpec key = new SecretKeySpec(contra, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedData = cipher.doFinal(contra);
			passEncrypted = new String(encryptedData);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			btnGuardar.setDisable(false);
			return;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			btnGuardar.setDisable(false);
			return;
		} catch (InvalidKeyException | IllegalBlockSizeException e) {
			e.printStackTrace();
			btnGuardar.setDisable(false);
			return;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			btnGuardar.setDisable(false);
			return;
		}

		Usuario usuario = new Usuario(documento, nombre, apellido, nacimiento, direccion, telefono, correo, ingreso,
				salario);

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de modificar los datos?");

		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {
				if (usuario.update(user, passEncrypted)) { // si se guardo
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Exito");
					alert.setHeaderText("Se ha actualizado los datos del usuario con exito");
					alert.showAndWait();
					vaciarFormulario();
				} else { // si ocurre un error
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Ocurrio un error en la actualizacion de datos");
					alert.showAndWait();
				}
				btnGuardar.setDisable(false); // se vuelve a habilitar el boton
			}

		});

	}

	@FXML
	void initialize() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();

		// recupera todas las ciudades desde la base de datos
		DataBase db = new DataBase();
		String sql = "SELECT ciudad_nombre FROM ciudad ORDER BY ciudad_nombre";
		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			// agrega las ciudades
			items.add("");

			while (rs.next())
				items.add(rs.getString(1));

			comboCiudad.setItems(items); // agrega los items al combo
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Control de entrada de caracteres
		txtNombre.setOnKeyTyped(Validar.texts());
		txtApellido.setOnKeyTyped(Validar.texts());
		txtDomicilio.setOnKeyTyped(Validar.alphanumeric());
		txtCorreo.setOnKeyTyped(Validar.email());
		txtSalario.setOnKeyTyped(Validar.numbers());
		txtTelefono.setOnKeyTyped(Validar.numbers());
		txtUser.setOnKeyTyped(Validar.alphanumeric());
		txtPass.setOnKeyTyped(Validar.alphanumeric());

	}

	/**
	 * Llena todos los campos del formulario de modificacion con los datos de un
	 * usuario especifico mediante el documento de un usuario especifico
	 * 
	 * @param numero numero de documento del usuario
	 */
	public void cargarDatosUsuario(long numero) {
		Usuario usuario = new Usuario(numero);
		this.documento = numero;

		// Llena todos los campos de texto con los campos recuperados de la base de
		// datos
		txtNombre.setText(usuario.getNombre());
		txtApellido.setText(usuario.getApellido());
		dateNacimiento.setValue(usuario.getNacimiento());
		txtDomicilio.setText(usuario.getDireccion().getDomicilio());

		if (usuario.getTelefono() == 0) {
			txtTelefono.setText("");
		} else {
			txtTelefono.setText("" + usuario.getTelefono());
		}

		txtCorreo.setText(usuario.getCorreo());
		if (usuario.getSalario() == 0) {
			txtSalario.setText("");
		} else {
			txtSalario.setText(new DecimalFormat("###, ###.###").format(usuario.getSalario()));
		}

		txtUser.setText(usuario.getUsuario());

		dateIngreso.setValue(usuario.getIngreso());
		comboCiudad.getSelectionModel().select(usuario.getDireccion().getCiudad());
		dateIngreso.setDisable(true);
		txtPass.setText("");

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
	 * @return the pantallaAlta
	 */
	public Pane getPantallaAlta() {
		return pantallaAlta;
	}

	/**
	 * @param pantallaAlta the pantallaAlta to set
	 */
	public void setPantallaAlta(Pane pantallaAlta) {
		this.pantallaAlta = pantallaAlta;
	}

	/**
	 * @return the txtNombre
	 */
	public TextField getTxtNombre() {
		return txtNombre;
	}

	/**
	 * @param txtNombre the txtNombre to set
	 */
	public void setTxtNombre(TextField txtNombre) {
		this.txtNombre = txtNombre;
	}

	/**
	 * @return the txtApellido
	 */
	public TextField getTxtApellido() {
		return txtApellido;
	}

	/**
	 * @param txtApellido the txtApellido to set
	 */
	public void setTxtApellido(TextField txtApellido) {
		this.txtApellido = txtApellido;
	}

	/**
	 * @return the txtCorreo
	 */
	public TextField getTxtCorreo() {
		return txtCorreo;
	}

	/**
	 * @param txtCorreo the txtCorreo to set
	 */
	public void setTxtCorreo(TextField txtCorreo) {
		this.txtCorreo = txtCorreo;
	}

	/**
	 * @return the comboCiudad
	 */
	public ComboBox<String> getComboCiudad() {
		return comboCiudad;
	}

	/**
	 * @param comboCiudad the comboCiudad to set
	 */
	public void setComboCiudad(ComboBox<String> comboCiudad) {
		this.comboCiudad = comboCiudad;
	}

	/**
	 * @return the txtDomicilio
	 */
	public TextField getTxtDomicilio() {
		return txtDomicilio;
	}

	/**
	 * @param txtDomicilio the txtDomicilio to set
	 */
	public void setTxtDomicilio(TextField txtDomicilio) {
		this.txtDomicilio = txtDomicilio;
	}

	/**
	 * @return the txtTelefono
	 */
	public TextField getTxtTelefono() {
		return txtTelefono;
	}

	/**
	 * @param txtTelefono the txtTelefono to set
	 */
	public void setTxtTelefono(TextField txtTelefono) {
		this.txtTelefono = txtTelefono;
	}

	/**
	 * @return the txtSalario
	 */
	public TextField getTxtSalario() {
		return txtSalario;
	}

	/**
	 * @param txtSalario the txtSalario to set
	 */
	public void setTxtSalario(TextField txtSalario) {
		this.txtSalario = txtSalario;
	}

	/**
	 * @return the dateNacimiento
	 */
	public DatePicker getDateNacimiento() {
		return dateNacimiento;
	}

	/**
	 * @param dateNacimiento the dateNacimiento to set
	 */
	public void setDateNacimiento(DatePicker dateNacimiento) {
		this.dateNacimiento = dateNacimiento;
	}

	/**
	 * @return the txtUser
	 */
	public TextField getTxtUser() {
		return txtUser;
	}

	/**
	 * @param txtUser the txtUser to set
	 */
	public void setTxtUser(TextField txtUser) {
		this.txtUser = txtUser;
	}

	/**
	 * @return the txtPass
	 */
	public PasswordField getTxtPass() {
		return txtPass;
	}

	/**
	 * @param txtPass the txtPass to set
	 */
	public void setTxtPass(PasswordField txtPass) {
		this.txtPass = txtPass;
	}

	/**
	 * @return the btnGuardar
	 */
	public Button getBtnGuardar() {
		return btnGuardar;
	}

	/**
	 * @param btnGuardar the btnGuardar to set
	 */
	public void setBtnGuardar(Button btnGuardar) {
		this.btnGuardar = btnGuardar;
	}

	/**
	 * borra los datos del formulario
	 */
	private void vaciarFormulario() {
		txtApellido.setText("");
		txtCorreo.setText("");

		txtDomicilio.setText("");
		txtNombre.setText("");
		txtPass.setText("");
		txtSalario.setText("");
		txtTelefono.setText("");
		txtUser.setText("");
		comboCiudad.getSelectionModel().select(0);

		dateNacimiento.setValue(null);
	}

	/**
	 * Convierte numero de String a float
	 * 
	 * @param numero a convertir a Float
	 * @return el número o -1 en caso de que no haya numero
	 */
	private float getFloat(String numero) {
		if (numero == null)
			return -1;

		return (numero.length() > 0) ? Float.parseFloat(numero) : -1;
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
