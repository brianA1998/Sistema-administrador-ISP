package comunicacion;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.DataBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import modelo.Plantilla;

public class MostrarPlantilla {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Pane pantallaModificacion;

	@FXML
	private TableView<Plantilla> tablaPlantilla;

	@FXML
	private TableColumn<Plantilla, String> colTitulo;

	@FXML
	private TableColumn<Plantilla, String> colMensaje;

	@FXML
	private Button btnModificar;

	@FXML
	void initialize() {

		setIconTablaPlantilla();

		colTitulo.setCellValueFactory(new PropertyValueFactory<Plantilla, String>("titulo"));
		colMensaje.setCellValueFactory(new PropertyValueFactory<Plantilla, String>("mensaje"));

		// CONSULTA SQL
		DataBase db = new DataBase();
		String sql = "SELECT plantilla_correo_id,plantilla_correo_mensaje, plantilla_correo_titulo FROM plantilla_correo";

		db.setQuery(sql);
		ResultSet rs = db.executeQuery();

		try {
			while (rs.next()) {
				Plantilla plantilla = new Plantilla();
				plantilla.setIdPlantilla(rs.getInt(1));
				plantilla.setMensaje(rs.getString(2));
				plantilla.setTitulo(rs.getString(3));

				tablaPlantilla.getItems().add(plantilla);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void modificarPlantilla(ActionEvent event) {
		// Cargamos el controlador
		Plantilla plantilla = tablaPlantilla.getSelectionModel().getSelectedItem();

		// Se controla de que se seleccione alguna plantilla de la tabla de plantillas
		if (plantilla == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ninguna plantilla");
			alert.showAndWait();

			return;
		} else {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/comunicacion/ModificacionPlantilla.fxml"));

			try {
				while (pantallaModificacion.getChildren().size() > 0)
					pantallaModificacion.getChildren().remove(0);

				Pane root = (Pane) loader.load();
				pantallaModificacion.getChildren().add(root);

				ModificarPlantilla modificarPlantilla = (ModificarPlantilla) loader.getController();
				modificarPlantilla.cargarDatosPlantilla(plantilla);

			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	/**
	 * Establece el icono del boton modificar de la tabla de plantillas
	 */
	public void setIconTablaPlantilla() {
		// Establece el icono del boton editar
		File archivo1 = new File("imagenes" + File.separator + "lapices.png");
		Image imagen1 = new Image(archivo1.toURI().toString(), 50, 50, true, true, true);
		btnModificar.setGraphic(new ImageView(imagen1));
		btnModificar.setContentDisplay(ContentDisplay.CENTER);
	}

	/**
	 * Permite retonar los itemos del combo de abmCorreo
	 * 
	 * @return item de abmCorreo
	 */
	public ObservableList<String> AbmCorreos() {
		// crea un nuevo observable list
		ObservableList<String> items = FXCollections.observableArrayList();
		items.addAll("Cargar Plantilla", "Mostrar Plantillas");
		return items;
	}

}
