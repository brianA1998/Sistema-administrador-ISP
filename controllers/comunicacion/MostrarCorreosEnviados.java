package comunicacion;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.DataBase;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import modelo.Cliente;
import modelo.Plantilla;

public class MostrarCorreosEnviados {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane contenedor;

	@FXML
	private TableView<Plantilla> tablaCorreoEnviado;

	@FXML
	private TableColumn<Plantilla, Long> colDocumento;

	@FXML
	private TableColumn<Plantilla, String> colTitulo;

	@FXML
	private TableColumn<Plantilla, String> colFechaEnvio;

	@FXML
	private TableColumn<Plantilla, String> colNombre;

	@FXML
	void initialize() {

		// Cambia el color la fila segun el valor del estado del correo
		tablaCorreoEnviado.setRowFactory(tr -> new TableRow<Plantilla>() {
			@Override
			public void updateItem(Plantilla item, boolean empty) {

				super.updateItem(item, empty);
				if (item != null) {
					if (item.getEstadoCorreo() == 0) {
						setStyle("-fx-background-color: #EF6666;");
					}
				}
			}
		});

		colTitulo.setCellValueFactory(new PropertyValueFactory<Plantilla, String>("titulo"));

		colFechaEnvio.setCellValueFactory(new PropertyValueFactory<Plantilla, String>("fechaEnvio"));

		// Valores de nombre del cliente
		colNombre.setCellValueFactory(new PropertyValueFactory<Plantilla, String>("") {
			public ObservableValue<String> call(CellDataFeatures<Plantilla, String> param) {
				StringProperty property = new SimpleStringProperty();
				Plantilla plantilla = param.getValue();
				property.set(plantilla.getCliente().getNombre());
				return property;
			}
		});

		// Valores de documento del cliente
		colDocumento.setCellValueFactory(new PropertyValueFactory<Plantilla, Long>("") {
			public ObservableValue<Long> call(CellDataFeatures<Plantilla, Long> param) {
				SimpleLongProperty property = new SimpleLongProperty();
				Plantilla plantilla = param.getValue();

				property.set(plantilla.getCliente().getDocumento());
				return property.asObject();
			}
		});

		// CONSULTA SQL
		DataBase db = new DataBase();
		String sql = "SELECT C.cliente_nombre,C.cliente_documento, P.plantilla_correo_titulo, CE.correo_enviado_fecha, CE.correo_enviado_estado FROM correo_enviado CE \r\n"
				+ "INNER JOIN cliente C ON CE.cliente_documento = C.cliente_documento \r\n"
				+ "INNER JOIN plantilla_correo P ON P.plantilla_correo_id = CE.plantilla_correo_id";

		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			rs.last();
			Plantilla[] plantillas = new Plantilla[rs.getRow()];
			Cliente[] clientes = new Cliente[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				clientes[i] = new Cliente();
				plantillas[i] = new Plantilla();
				clientes[i].setNombre(rs.getString(1));
				clientes[i].setDocumento(rs.getLong(2));
				plantillas[i].setTitulo(rs.getString(3));
				plantillas[i].setFechaEnvio(rs.getDate(4).toLocalDate());
				plantillas[i].setEstadoCorreo(rs.getInt(5));
				plantillas[i].setCliente(clientes[i]);

			}
			tablaCorreoEnviado.getItems().addAll(plantillas);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
