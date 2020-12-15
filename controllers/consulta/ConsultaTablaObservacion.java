package consulta;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import controlador.Validar;
import db.DataBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.Preventa;

public class ConsultaTablaObservacion {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Preventa> tablaObservaciones;

	@FXML
	private TableColumn<Preventa, LocalDateTime> colFechaHora;

	@FXML
	private TableColumn<Preventa, String> colObservacion;

	@FXML
	private TableColumn<Preventa, String> colUsuario;

	@FXML
	private TextArea txtObservacion;

	@FXML
	private Button btnCargar;

	private int idPreventa;

	@FXML
	public void cargarObservacion(ActionEvent event) {
		Preventa preventa = new Preventa(txtObservacion.getText());
		preventa.setIdPreventa(idPreventa);
		preventa.setFechaObservacion(LocalDateTime.now());

		if (preventa.insertarObservacion()) {

			txtObservacion.setText("");
			tablaObservaciones.getItems().add(preventa);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Ocurrio un error en la insercion de datos");
			alert.showAndWait();
		}
		btnCargar.setDisable(false);
	}

	@FXML
	public void initialize() {
		// Control de entrada de caracteres
		txtObservacion.setOnKeyTyped(Validar.alphanumeric());

		colFechaHora.setCellFactory(cf -> new TableCell<Preventa, LocalDateTime>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);

				DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

				if (item == null || empty) {
					setText(null);
				} else {
					setText(formato.format(item));
				}
			}
		});

		colFechaHora.setCellValueFactory(new PropertyValueFactory<Preventa, LocalDateTime>("fechaObservacion"));
		colObservacion.setCellValueFactory(new PropertyValueFactory<Preventa, String>("observacion"));
		colUsuario.setCellValueFactory(new PropertyValueFactory<Preventa, String>(""));
		getEnter();
	}

	/**
	 * Permite llenar la tabla de observaciones Cuando la persona no tiene
	 * obsevaciones aun. Y es la primera que se va a cargar
	 */
	public void llenarTablaObservacion(int preventaid) {
		this.idPreventa = preventaid;

		String sql = "SELECT DISTINCT C.consulta_fecha, C.consulta_observacion FROM consulta C INNER JOIN preventa_consulta PC ON C.consulta_id = PC.consulta_id INNER JOIN preventa P ON P.preventa_id = PC.preventa_id WHERE P.preventa_id = ?";
		DataBase db = new DataBase();
		db.setQuery(sql);
		db.setParametro(1, preventaid);
		ResultSet rs = db.executeQuery();

		try {
			rs.last();
			Preventa[] preventas = new Preventa[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				preventas[i] = new Preventa();
				preventas[i].setFechaObservacion(rs.getTimestamp(1).toLocalDateTime());
				preventas[i].setObservacion(rs.getString(2));

			}

			tablaObservaciones.getItems().setAll(preventas);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Permite cargar los datos a la tabla e inserta los datos a la base de datos en
	 * la tabla de consulta
	 */
	private void getEnter() {
		txtObservacion.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					Preventa preventa = new Preventa(txtObservacion.getText());
					preventa.setIdPreventa(idPreventa);
					preventa.setFechaObservacion(LocalDateTime.now());

					if (preventa.insertarObservacion()) {

						txtObservacion.setText("");
						tablaObservaciones.getItems().add(preventa);
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Ocurrio un error en la insercion de datos");
						alert.showAndWait();
					}
					btnCargar.setDisable(false);
				}
			}
		});
	}
}
