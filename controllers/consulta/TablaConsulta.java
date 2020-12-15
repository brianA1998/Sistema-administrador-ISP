package consulta;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import db.DataBase;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import modelo.Preventa;

public class TablaConsulta {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane tablaRegistroConsulta;

	@FXML
	private TableView<Preventa> tablaConsultas;

	@FXML
	private TableColumn<Preventa, Integer> colIdConsulta;

	@FXML
	private TableColumn<Preventa, String> colNombreConsulta;

	@FXML
	private TableColumn<Preventa, LocalDate> colFechaConsulta;

	private Pane contenedor;

	@FXML
	void initialize() {
		mostrarTablaObservacion();
	}

	public void llenarTabla(String dato) {
		colIdConsulta.setCellValueFactory(new PropertyValueFactory<Preventa, Integer>("idPreventa"));
		colNombreConsulta.setCellValueFactory(new PropertyValueFactory<Preventa, String>("nombre"));
		colFechaConsulta.setCellValueFactory(new PropertyValueFactory<Preventa, LocalDate>("fechaConsulta"));

		String sql = "SELECT P.preventa_id, P.preventa_nombre, P.preventa_fecha, C.ciudad_nombre, B.barrio_nombre "
				+ "FROM preventa P INNER JOIN ciudad C ON P.ciudad_id = C.ciudad_id "
				+ "INNER JOIN barrio B ON P.barrio_id = B.barrio_id WHERE P.preventa_nombre LIKE '%' ? '%'";
		DataBase db = new DataBase();
		db.setQuery(sql);
		db.setParametro(1, dato);
		ResultSet rs = db.executeQuery();
		try {
			rs.last();
			Preventa[] preventas = new Preventa[rs.getRow()];
			rs.beforeFirst();
			for (int i = 0; rs.next(); i++) {
				preventas[i] = new Preventa();
				preventas[i].setIdPreventa(rs.getInt(1));
				preventas[i].setNombre(rs.getString(2));
				preventas[i].setFechaConsulta(rs.getDate(3).toLocalDate());
				preventas[i].setCiudad(rs.getString(4));
				preventas[i].setBarrio(rs.getString(5));
			}
			tablaConsultas.getItems().setAll(preventas);
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}

	private void mostrarTablaObservacion() {

		tablaConsultas.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2) {

				// se obtiene id de la preventa
				Preventa preventa = tablaConsultas.getSelectionModel().getSelectedItem();

				// Se controla de que se seleccione alguna preventa de la tabla de preventas
				if (preventa == null) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("No se selecciono ninguna consulta");
					alert.showAndWait();

					return;
				} else {
					FXMLLoader loader = new FXMLLoader(
							getClass().getResource("/consulta/TablaConsultaObservacion.fxml"));

					try {
						tablaRegistroConsulta.getChildren().add((Pane) loader.load());

						contenedor.getChildren().setAll(tablaRegistroConsulta);

						// carga el controlador
						ConsultaTablaObservacion tablaObservacion = (ConsultaTablaObservacion) loader.getController();

						int idPreventa = preventa.getIdPreventa();

						preventa.insert();
						tablaObservacion.llenarTablaObservacion(idPreventa);

					} catch (IOException e) {
						e.printStackTrace();

					}
				}

			}
		});

	}

	public void setPanelPreventa(Pane contenedor) {
		this.contenedor = contenedor;

	}

}
