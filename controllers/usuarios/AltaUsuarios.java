package usuarios;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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

public class AltaUsuarios {
	@FXML
	private Pane pantallaAlta;

	@FXML
	private TextField txtNombre;

	@FXML
	private TextField txtApellido;

	@FXML
	private TextField txtDocumento;

	@FXML
	private TextField txtDomicilio;

	@FXML
	private TextField txtTelefono;

	@FXML
	private TextField txtCorreo;

	@FXML
	private TextField txtSalario;

	@FXML
	private TextField txtUser;

	@FXML
	private DatePicker dateNacimiento;

	@FXML
	private DatePicker dateIngreso;

	@FXML
	private Button btnGuardar;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private PasswordField txtPass;

	private String user;

	private String passEncrypted;

	@FXML
	public void guardarUsuario(ActionEvent event) {

		// desabilita el boton, evita el doble click
		btnGuardar.setDisable(true);

		// Obtenemos los datos
		float salario = getFloat(txtSalario.getText());
		Long documento = getLong(txtDocumento.getText());
		long telefono = Validar.numberPhone(txtTelefono.getText());
		String nombre = txtNombre.getText();
		String apellido = txtApellido.getText();
		String domicilio = txtDomicilio.getText();
		String ciudad = getSelectedItem(comboCiudad);
		String correo = Validar.emailAddress(txtCorreo.getText());
		user = txtUser.getText();
		String pass = txtPass.getText();
		LocalDate nacimiento = dateNacimiento.getValue();
		LocalDate ingreso = dateIngreso.getValue();
		Direccion direccion = new Direccion(domicilio, ciudad, null);

		// valida los datos
		nombre = (nombre.length() > 0) ? nombre : null;
		apellido = (apellido.length() > 0) ? apellido : null;
		user = (user.length() > 0) ? user : null;
		pass = (pass.length() > 0) ? pass : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombre == null || apellido == null || documento == -1L || user == null || pass == null || ciudad == null) {
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

		// Validacion de documento de usuario
		String documentoUsuario = documento.toString();
		if (documentoUsuario.length() < 6 || documentoUsuario.length() > 8) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar el documento del usuario");
			alert.showAndWait();

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
		a.setHeaderText("¿Esta seguro/a de cargar el usuario?");

		a.showAndWait().ifPresent(response -> {
			if (response == aceptar) {
				if (usuario.insert(user, passEncrypted)) { // si se guardo
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Exito");
					alert.setHeaderText("Se ha registrado con exito el usuario");
					alert.showAndWait();
					vaciarFormulario();
				} else { // si ocurre un error
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Ocurrio un error en la insercion de datos");
					alert.showAndWait();
				}
				btnGuardar.setDisable(false); // se vuelve a habilitar el boton
			}

		});

	}

	@FXML
	void initialize() {
		getCiudad();
		// Control de entrada de caracteres
		txtNombre.setOnKeyTyped(Validar.texts());
		txtApellido.setOnKeyTyped(Validar.texts());
		txtDocumento.setOnKeyTyped(Validar.numbers());
		txtDomicilio.setOnKeyTyped(Validar.alphanumeric());
		txtCorreo.setOnKeyTyped(Validar.email());
		txtSalario.setOnKeyTyped(Validar.numbers());
		txtTelefono.setOnKeyTyped(Validar.numbers());
		txtUser.setOnKeyTyped(Validar.alphanumeric());
		txtPass.setOnKeyTyped(Validar.alphanumeric());
	}

	/**
	 * borra los datos del formulario
	 */
	private void vaciarFormulario() {
		txtApellido.setText("");
		txtCorreo.setText("");
		txtDocumento.setText("");
		txtDomicilio.setText("");
		txtNombre.setText("");
		txtPass.setText("");
		txtSalario.setText("");
		txtTelefono.setText("");
		txtUser.setText("");
		comboCiudad.getSelectionModel().select(0);
		dateIngreso.setValue(null);
		dateNacimiento.setValue(null);

	}

	/**
	 * Permite llenar el combo ciudad
	 */
	private void getCiudad() {
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
	}

	/**
	 * Convierte numero de String a Long
	 * 
	 * @param numero a convertir a long
	 * @return el número o -1 en caso de que no haya numero
	 */
	private long getLong(String numero) {
		if (numero == null)
			return -1L;

		return (numero.length() > 0) ? Long.parseLong(numero) : -1L;
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

	public void redimensionar(double width, double height) {
		pantallaAlta.setPrefSize(width, height);
		pantallaAlta.setLayoutX(10);
	}
}
