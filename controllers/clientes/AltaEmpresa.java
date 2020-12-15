package clientes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import DriverJasperReport.JReport;
import adhesiones.AltaAdhesion;
import controlador.Validar;
import db.DataBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Adhesion;
import modelo.Cliente;
import modelo.Direccion;

public class AltaEmpresa {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

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

	@FXML
	private ComboBox<String> comboEmpresa;

	private Pane panelPersona;

	@FXML
	public void guardarCliente(ActionEvent event) {
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
		String tipoCliente = getSelectedItem(comboEmpresa);

		Direccion direccion = new Direccion(domicilio, ciudad, barrio, departamento, monoblock, manzana);

		Cliente cliente = new Cliente(razonSocial, cuit, direccion, telefono, celular, correo, null, iva, twitter, web,
				instagram, facebook, linkedin, tipoCliente);

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

		// VERIFICA SI EL CLIENTE ESTA SEGURO DE GUARDAR LOS DATOS O NO
		ButtonType aceptar = new ButtonType("SI");
		ButtonType cancelar = new ButtonType("NO");
		Alert a = new Alert(AlertType.NONE, "", aceptar, cancelar);
		a.setTitle("Confirmacion");
		a.setHeaderText("¿Esta seguro/a de cargar el cliente?");

		a.showAndWait().ifPresent(response -> {

			if (response.equals(aceptar)) {
				List<Adhesion> adhesiones = new ArrayList<Adhesion>();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/adhesion/AltaAdhesion.fxml"));

				try {

					Scene scene = new Scene((Pane) loader.load());

					// carga el controlador
					AltaAdhesion controller = (AltaAdhesion) loader.getController();
					controller.cargamosTitular(txtRazonSocial.getText());
					controller.cargarComboServicio(cliente.getTipoCliente());
					controller.setAdhesiones(adhesiones);

					Stage stage = new Stage();
					stage.setScene(scene);
					stage.setResizable(false);
					stage.setMaximized(false);

					stage.initModality(Modality.APPLICATION_MODAL);

					stage.showAndWait();
				} catch (IOException e) {

					e.printStackTrace();
				}

				DataBase.getInstancia().setTransaction();
				if (cliente.insert()) {
					boolean resultado = true;
					for (Adhesion adhesion : adhesiones) {
						adhesion.setCliente(cliente);
						if (!(resultado = adhesion.insert()))
							break;
					}
					if (resultado) {

						DataBase.getInstancia().commit();

						// Verifica si se quiere imprimir el contrato
						ButtonType si = new ButtonType("SI");
						ButtonType no = new ButtonType("NO");
						Alert alerta = new Alert(AlertType.NONE, "", si, no);
						alerta.setTitle("Confirmacion");
						alerta.setHeaderText("¿Quiere imprimir el contrato?");

						alerta.showAndWait().ifPresent(respuesta -> {
							if (respuesta.equals(si)) {

								// imprime el contrato
								generarContrato(cliente.getDocumento());
							} else {
								return;
							}
						});

						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Exito");
						alert.setHeaderText(
								"Se ha registrado con exito el cliente numero: " + cliente.getNumeroAbonado());
						alert.showAndWait();
						vaciarFormulario();

					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Ocurrio un error en la insercion de la adhesión");
						alert.showAndWait();
					}
					btnGuardar.setDisable(false); // se vuelve a habilitar el boton
				}
				btnGuardar.setDisable(false);
			}
		});

	}

	@FXML
	public void initialize() {

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
		comboEmpresa.setItems(Cliente.llenarTipoCliente());
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
	 * Limpia todos los campos del formulario
	 */
	private void vaciarFormulario() {
		txtCelular.setText("");
		txtCorreo.setText("");
		txtCuit.setText("");
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
	 * agrega la pantalla de persona
	 * 
	 * @param panelPersona
	 */
	public void setPanelPersona(Pane panelPersona) {
		this.panelPersona = panelPersona;
	}

	/**
	 * Permite mostrar la interfaz de alta persona
	 */
	@FXML
	private void cambiarPantalla(ActionEvent event) {

		String tipo_servicio = comboEmpresa.getSelectionModel().getSelectedItem().toString();

		if (tipo_servicio.equals("Residencial")) {
			panelPersona.setVisible(true);
			contenedor.setVisible(false);
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

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("nombreCliente", cliente.getNombre());
		parametros.put("apellidoCliente", cliente.getApellido());
		parametros.put("documentoCliente", numero);
		parametros.put("domicilioCliente", cliente.getDireccion().getDomicilio());
		parametros.put("departamentoCliente", cliente.getDireccion().getDepartamento());
		parametros.put("celularCliente", cliente.getCelular());
		parametros.put("barrioCliente", cliente.getDireccion().getBarrio());
		parametros.put("ciudadCliente", cliente.getDireccion().getCiudad());
		parametros.put("provinciaCliente", cliente.getDireccion().getProvincia());
		parametros.put("codigoPostal", Direccion.getCodigoPostal(cliente.getDireccion().getCiudad()));
		parametros.put("correoCliente", cliente.getCorreo());
		parametros.put("monoblockCliente",
				(cliente.getDireccion().getMonoblock() == 0) ? 0 : cliente.getDireccion().getMonoblock());
		parametros.put("fechaNacCliente", (cliente.getNacimiento() == null) ? "" : cliente.getNacimiento().toString());
		parametros.put("numeroCliente", cliente.getNumeroAbonado());
		parametros.put("manzanaCliente",
				(cliente.getDireccion().getManzana() == 0) ? 0 : cliente.getDireccion().getManzana());

		// Conexion con base de datos
		Connection con = new DataBase().getConnection();

		// Creamos un reporte
		JReport reporte = new JReport();
		reporte.createReport(con, "reporte" + File.separator + "Contrato.jasper", parametros);
		reporte.showViewer();
		reporte.exportPDF(path);

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
}
