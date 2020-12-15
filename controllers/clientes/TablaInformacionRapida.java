package clientes;

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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import modelo.Cliente;

public class TablaInformacionRapida {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedorTabla;

	@FXML
	private TableView<Cliente> tablaInfoRapida;

	@FXML
	private TableColumn<Cliente, Long> colAbonado;

	@FXML
	private TableColumn<Cliente, String> colNombre;

	@FXML
	private TableColumn<Cliente, Long> colDni;

	@FXML
	private Button btnCerrarVentana;

	private Pane contenidoAnterior;

	@FXML
	public void cerrarTabla(ActionEvent event) {
		contenedorTabla.getChildren().setAll();
		contenidoAnterior.setVisible(true);
	}

	@FXML
	public void initialize() {
		mostrarInfoRapida();
	}

	/**
	 * Llenamos la tabla de info rapida
	 */
	public void llenarTabla(String dato) {

		colAbonado.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("numeroAbonado"));
		colNombre.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombre"));
		colDni.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("documento"));

		// consulta sql
		DataBase db = new DataBase();

		String sql = "SELECT cliente_numero_abonado, cliente_nombre, cliente_documento FROM cliente WHERE cliente_documento LIKE '%' ? '%' OR cliente_numero_abonado LIKE '%' ? '%' OR cliente_nombre LIKE '%' ? '%'";

		db.setQuery(sql);
		db.setParametro(1, dato);
		db.setParametro(2, dato);
		db.setParametro(3, dato);

		ResultSet rs = db.executeQuery();

		try {
			rs.last();
			Cliente[] clientes = new Cliente[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				clientes[i] = new Cliente();
				clientes[i].setNumeroAbonado(rs.getInt(1));
				clientes[i].setNombre(rs.getString(2));
				clientes[i].setDocumento(rs.getLong(3));

			}
			tablaInfoRapida.getItems().addAll(clientes);

		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	/**
	 * establece el contenido anteior
	 * 
	 * @param contenidoAnterior
	 */
	public void setContenidoAnterior(Pane contenidoAnterior) {
		this.contenidoAnterior = contenidoAnterior;
	}

	/**
	 * Agrega el evento doble click de abrir interface de información rápida de
	 * cliente
	 */
	private void mostrarInfoRapida() {

		tablaInfoRapida.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) { // evento de doble click

				// se obtiene el correo del cliente seleccionado
				Cliente cliente = tablaInfoRapida.getSelectionModel().getSelectedItem();
				// Se controla de que se seleccione algun cliente de la tabla de clientes
				if (cliente == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ningun cliente");
					alert.showAndWait();

					return;
				} else {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/cliente/InfoRapida.fxml"));

					// elimina todo el contenido de contenedor
					contenedorTabla.getChildren().setAll();

					try {
						// agrega el contenido a la pantalla
						contenedorTabla.getChildren().add((Pane) loader.load());

						// carga el controlador
						InformacionRapida infoRapida = (InformacionRapida) loader.getController();

						long documentoCliente = cliente.getDocumento();

						infoRapida.cargarDatosCliente(documentoCliente);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});
	}

}
