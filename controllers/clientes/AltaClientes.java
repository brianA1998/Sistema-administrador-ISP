package clientes;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Types;
import java.time.LocalDate;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Adhesion;
import modelo.Cliente;
import modelo.Direccion;

public class AltaClientes {

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
	private TextField txtDocCliente;

	@FXML
	private TextField txtDomicilioCliente;

	@FXML
	private TextField txtManzanaCliente;

	@FXML
	private TextField txtMonoblockCliente;

	@FXML
	private TextField txtCelCliente;

	@FXML
	private TextField txtTelCliente;

	@FXML
	private DatePicker CalendarFechaNac;

	@FXML
	private TextField txtDepartamento;

	@FXML
	private CheckBox checkBoxEmpresa;

	@FXML
	private Button btnGuardar;

	@FXML
	private TextField txtCorreoCliente;

	@FXML
	private ComboBox<String> comboSituacionLaboral;

	@FXML
	private TextField txtLugar;

	@FXML
	private TextField txtPuesto;

	@FXML
	private ComboBox<String> comboIva;

	@FXML
	private TextField txtTwitter;

	@FXML
	private TextField txtWeb;

	@FXML
	private TextField txtInstagram;

	@FXML
	private TextField txtFacebook;

	@FXML
	private TextField txtLinkedin;

	@FXML
	private ComboBox<String> comboBarrio;

	@FXML
	private ComboBox<String> comboCiudad;

	@FXML
	private ComboBox<String> comboEmpresa;

	@FXML
	private Pane contenedor;

	private Pane panelEmpresa;

	@FXML
	public void initialize() {

		// llena los combos
		comboCiudad.setItems(Cliente.llenarCiudades());
		comboBarrio.setItems(Cliente.llenarBarrios());
		comboIva.setItems(Cliente.llenarIvaCliente());
		comboSituacionLaboral.setItems(Cliente.llenarSituacionLaboral());

		// Control de entrada de caracteres
		txtNombreCliente.setOnKeyTyped(Validar.texts());
		txtApellidoCliente.setOnKeyTyped(Validar.texts());
		txtDocCliente.setOnKeyTyped(Validar.numbers());
		txtDomicilioCliente.setOnKeyTyped(Validar.alphanumeric());
		txtMonoblockCliente.setOnKeyTyped(Validar.numbers());
		txtManzanaCliente.setOnKeyTyped(Validar.numbers());
		txtCelCliente.setOnKeyTyped(Validar.numbers());
		txtTelCliente.setOnKeyTyped(Validar.numbers());
		txtDepartamento.setOnKeyTyped(Validar.alphanumeric());
		txtCorreoCliente.setOnKeyTyped(Validar.email());

		txtLugar.setOnKeyTyped(Validar.texts());
		txtPuesto.setOnKeyTyped(Validar.texts());

		comboEmpresa.setItems(Cliente.llenarTipoCliente());
		comboEmpresa.getSelectionModel().selectLast();
		comboCiudad.setOnAction(e -> cargarComboBarrio());
		CalendarFechaNac.setOnAction(e -> validarFechaNacimiento());

	}

	/**
	 * 
	 */
	public void cargarComboBarrio() {
		int ciudadId = Direccion.getCiudadId(getSelectedItem(comboCiudad));
		comboBarrio.setItems(Direccion.llenarBarrioFiltrado(ciudadId));
	}

	@FXML
	public void guardarCliente(ActionEvent event) {
		btnGuardar.setDisable(true);

		// OBTENER DATOS
		String nombre = txtNombreCliente.getText();
		String apellido = txtApellidoCliente.getText();
		Long documento = getLong(txtDocCliente.getText());
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
		String tipoCliente = getSelectedItem(comboEmpresa);

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

		// Validacion de documento de cliente
		String documentoCliente = documento.toString();
		if (documentoCliente.length() < 7 || documentoCliente.length() > 8) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Error al ingresar el documento");
			alert.showAndWait();

			btnGuardar.setDisable(false);
			return;
		}

		// valida los datos
		nombre = (nombre.length() > 0) ? nombre : null;
		apellido = (apellido.length() > 0) ? apellido : null;
		domicilio = (domicilio.length() > 0) ? domicilio : null;

		// si hubo un dato no ingresado sale sin guardar
		if (nombre == null || documento == -1L || domicilio == null || celular == -1L || ciudad == null
				|| barrio == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Falta ingresar datos");
			alert.showAndWait();
			btnGuardar.setDisable(false);
			return;
		}

		Direccion direccion = new Direccion(domicilio, ciudad, barrio, departamento, monoblock, manzana);

		Cliente cliente = new Cliente(nombre, apellido, documento, direccion, telefono, celular, correo, nacimiento,
				null, situacion, lugar, puesto, Iva, twitter, web, instagram, facebook, linkedin, tipoCliente);

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
					controller.cargamosTitular(txtNombreCliente.getText());

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

				} else {
					// si ocurre un error
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Ocurrio un error en la insercion de datos");
					alert.showAndWait();
				}
				btnGuardar.setDisable(false); // se vuelve a habilitar el boton

			}

			btnGuardar.setDisable(false);
		});

	}

	/**
	 * Permite validar si es mayor de edad una persona
	 */
	private void validarFechaNacimiento() {
		// Controla que la fecha sea null
		if (CalendarFechaNac.getValue() == null)
			return;

		// Obtengo dia,mes y año
		int dia = CalendarFechaNac.getValue().getDayOfMonth();
		int mes = CalendarFechaNac.getValue().getMonthValue();
		int año = CalendarFechaNac.getValue().getYear();

		int fecha = Validar.calcularEdad(dia, mes, año);

		if (fecha <= 18) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Es menor de edad");
			alert.showAndWait();

			btnGuardar.setDisable(false);
			return;
		}
	}

	/**
	 * @return the comboEmpresa
	 */
	public ComboBox<String> getComboEmpresa() {
		return comboEmpresa;
	}

	/**
	 * @param comboEmpresa the comboEmpresa to set
	 */
	public void setComboEmpresa(ComboBox<String> comboEmpresa) {
		this.comboEmpresa = comboEmpresa;
	}

	/**
	 * redimensiona el formulario
	 */
	public void redimensionar(double width, double height) {
		pantallaAlta.setPrefSize(width, height);
		pantallaAlta.setLayoutX(10);
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
		parametros.put("manzanaCliente",
				(cliente.getDireccion().getManzana() == 0) ? Types.NULL : cliente.getDireccion().getManzana());

		// Conexion con base de datos
		Connection con = new DataBase().getConnection();

		// Creamos un reporte
		JReport reporte = new JReport();
		reporte.createReport(con, "reporte" + File.separator + "Contrato.jasper", parametros);
		reporte.showViewer();
		reporte.exportPDF(path);

	}

	/**
	 * Permite mostrar la interfaz de alta empresa
	 */
	@FXML
	private void cambiarPantalla(ActionEvent event) {
		String tipo_servicio = comboEmpresa.getSelectionModel().getSelectedItem().toString();

		if (tipo_servicio.equals("Empresa") || tipo_servicio.equals("PyME")) {
			if (panelEmpresa == null) { // entra solo la primera vez
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/AltaEmpresa.fxml"));
				try {
					// carga vista y controlador
					panelEmpresa = (Pane) loader.load();
					contenedor.getChildren().add(panelEmpresa);

					AltaEmpresa empresa = (AltaEmpresa) loader.getController();
					empresa.setPanelPersona(pantallaAlta);

					// redimencion de panel empresa
					panelEmpresa.setPrefSize(pantallaAlta.getWidth(), pantallaAlta.getHeight());
					panelEmpresa.setLayoutX(0d);
					panelEmpresa.setLayoutY(0d);

					pantallaAlta.setVisible(false);
					panelEmpresa.setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else { // entra el resto de las veces
				pantallaAlta.setVisible(false);
				panelEmpresa.setVisible(true);
			}

		}

	}

	/**
	 * Convierte numero de String a Long
	 * 
	 * @param numero a convertir a long
	 * @return long
	 */
	private long getLong(String numero) {
		if (numero == null)
			return -1L;

		return (numero.length() > 0) ? Long.parseLong(numero) : -1L;
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
	 * Borra todos los datos del formulario
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
