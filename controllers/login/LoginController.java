package login;

import java.awt.Label;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import controlador.Sidecom;
import db.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label lblStatus;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnRestContrasena;

	@FXML
	private Button btnRestablecerContraseña;

	@FXML
	private TextField txtUsuario;

	@FXML
	private PasswordField txtContrasena;

	@FXML
	private ImageView imgUser;

	@FXML
	private ImageView imgPass;

	@FXML
	private ImageView imgTituloSidecom;

	int contador = 0;

	@FXML
	void ingresar(ActionEvent event) {
		// obtiene los datos
		String user = txtUsuario.getText();

		byte[] contra;
		try {
			contra = txtContrasena.getText().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			return;
		} // tiene que ser utf8
		String passEncrypted = null;
		try {
			SecretKeySpec key = new SecretKeySpec(contra, "Blowfish");
			Cipher cipher = Cipher.getInstance("Blowfish");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encryptedData = cipher.doFinal(contra);
			passEncrypted = new String(encryptedData);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException | IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		// consulta
		DataBase database = new DataBase();
		String sql = "SELECT * FROM usuario WHERE usuario_estado = 1 AND usuario_usuario = ? AND usuario_password = ? ";
		database.setQuery(sql); // consulta sql
		database.setParametro(1, user);
		database.setParametro(2, passEncrypted);

		ResultSet rs = database.executeQuery();// retorna resulset

		try {
			if (!rs.next()) {

				// cambia de pantalla
				// genera la pantalla principal
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/sidecom/Principal.fxml"));
				AnchorPane root = (AnchorPane) loader.load();
				// creo la pantalla principal

				Stage pantallaPrincipal = new Stage();
				Scene escena = new Scene(root);
				escena.getStylesheets().add(getClass().getResource("/main/application.css").toExternalForm());

				// agrego una escena con el ancho pane de la pantalla princpal
				pantallaPrincipal.setMaximized(true);
				pantallaPrincipal.setScene(escena);
				pantallaPrincipal.show();

				// cierra el login
				Stage login = (Stage) btnLogin.getScene().getWindow();
				login.close();
				// carga el controlador
				Sidecom sidecom = (Sidecom) loader.getController();
				sidecom.redimensionar(pantallaPrincipal.getWidth(), pantallaPrincipal.getHeight());

			} else {
				if (++contador == 3) { // cuando le erre 3 veces al usuario
					sql = "UPDATE usuario SET usuario_estado = '0' WHERE usuario_usuario = ?";
					database.setQuery(sql);
					database.setParametro(1, user);
					if (database.execute()) { // si era un usuario valido se bloquea
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Se ha bloqueado el usuario");
						alert.showAndWait();
					} // sino solo se cierra el programa
					System.exit(0);
				} else { // si le erro menos de 3 veces
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Error de usuario o contraseña");
					alert.showAndWait();
				}
			}

		} catch (SQLException e) { // error de consulta sql
			e.printStackTrace();
		} catch (IOException e) { // error de buscar el archivo de la pantalla
			e.printStackTrace();
		}

	}

	/**
	 * Establece los iconos y titulo de la interfaz de login
	 */
	public void setIconLogin() {
		// Establece el icono de usuario
		File archivo = new File("imagenes" + File.separator + "usuario (2).png");
		Image imagen = new Image(archivo.toURI().toString(), 77, 65, true, true, true);
		imgUser.setImage(imagen);

		// Establece el icono de contraseña
		File archivo1 = new File("imagenes" + File.separator + "llave.png");
		Image imagen1 = new Image(archivo1.toURI().toString(), 77, 65, true, true, true);
		imgPass.setImage(imagen1);

		// Establece el icono de contraseña
		File archivo2 = new File("imagenes" + File.separator + "IMAGEN SIDECOM.png");
		Image imagen2 = new Image(archivo2.toURI().toString(), 363, 109, true, true, true);
		imgTituloSidecom.setImage(imagen2);
	}

	@FXML
	void initialize() {
		setIconLogin();
	}
}
