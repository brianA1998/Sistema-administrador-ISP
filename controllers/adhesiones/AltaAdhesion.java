package adhesiones;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.Adhesion;
import modelo.Servicio;

public class AltaAdhesion {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private ComboBox<String> comboServicio;

	@FXML
	private Button btnQuitar;

	@FXML
	private Button btnGuardarAdhesion;

	@FXML
	private Button btnAgregarServicio;

	@FXML
	private Button btnAgregarCuota;

	@FXML
	private TextField txtDetalle;

	@FXML
	private ComboBox<String> comboItem;

	@FXML
	private TextField txtMonto;

	@FXML
	private Label lblNetoUSD;

	@FXML
	private Label lblIvaUSD;

	@FXML
	private Label lblTotalUSD;

	@FXML
	private Label lblNetoARS;

	@FXML
	private Label lblIvaARS;

	@FXML
	private Label lblTotalARS;

	@FXML
	private TableView<Adhesion> tablaAdhesion;

	@FXML
	private ComboBox<String> comboMoneda;

	@FXML
	private ComboBox<String> comboIva;

	@FXML
	private ComboBox<String> comboCuotas;

	@FXML
	private TableColumn<Adhesion, String> colServicio;

	@FXML
	private TableColumn<Adhesion, String> colMoneda;

	@FXML
	private TableColumn<Adhesion, Float> colPrecioUnitario;

	@FXML
	private TableColumn<Adhesion, Float> colIva;

	@FXML
	private TableColumn<Adhesion, Float> colPrecioTotal;

	@FXML
	private TableColumn<Adhesion, Integer> colCuotas;

	@FXML
	private Button btnAgregarVarios;

	@FXML
	private Label labelTitular;

	private List<Adhesion> adhesiones;

	@FXML
	void agregarVarios(ActionEvent event) {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/adhesion/itemVarios.fxml"));

		try {
			Scene scene = new Scene((Pane) loader.load());

			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setResizable(false);
			stage.setMaximized(false);

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.showAndWait();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	void quitarTabla(ActionEvent event) {
		Adhesion adhesion = tablaAdhesion.getSelectionModel().getSelectedItem();
		// Se controla de que se seleccione algun servicio o item de la tabla de
		// adhesiones
		if (adhesion == null) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("No se selecciono ningun item o servicio");
			alert.showAndWait();

			return;
		} else {
			tablaAdhesion.getItems().remove(adhesion);
			if (adhesion.getServicio() == null) {
				// si es servicio todo resta
				float monto = adhesion.getItemMonto();
				String tipoMoneda = adhesion.getItemMoneda();
				float iva = adhesion.getItemIva();

				// adhesion.setItem(monto, detalle, tipoMoneda, cuotas - 1, cuotas, iva);
				if (tipoMoneda.equals("ARS")) {
					// sumamos los valores
					float netoarg = Float.parseFloat(lblNetoARS.getText());
					netoarg -= monto;
					lblNetoARS.setText("" + netoarg);

					float ivaarg = Float.parseFloat(lblIvaARS.getText());
					ivaarg -= monto * iva / 100;
					lblIvaARS.setText("" + ivaarg);

					lblTotalARS.setText("" + (netoarg + ivaarg));
				} else {
					// sumamos los valores
					float netousd = Float.parseFloat(lblNetoUSD.getText());
					netousd -= monto;
					lblNetoUSD.setText("" + netousd);

					float ivausd = Float.parseFloat(lblIvaUSD.getText());
					ivausd -= monto * iva / 100;
					lblIvaUSD.setText("" + ivausd);

					lblTotalUSD.setText("" + (netousd + ivausd));
				}

			} else {
				// si es items todo resta

				// getServicio para sacar datos

				Servicio servicio = adhesion.getServicio();

				if (servicio.getTipoMoneda().equals("ARS")) {
					// sumamos los valores
					float netoarg = Float.parseFloat(lblNetoARS.getText());
					netoarg -= servicio.getMonto();
					lblNetoARS.setText("" + netoarg);

					float ivaarg = Float.parseFloat(lblIvaARS.getText());
					ivaarg -= servicio.getMonto() * servicio.getIva() / 100;
					lblIvaARS.setText("" + ivaarg);

					lblTotalARS.setText("" + (netoarg + ivaarg));
				} else {
					// sumamos los valores
					float netousd = Float.parseFloat(lblNetoUSD.getText());
					netousd -= servicio.getMonto();
					lblNetoUSD.setText("" + netousd);

					float ivausd = Float.parseFloat(lblIvaUSD.getText());
					ivausd -= servicio.getMonto() * servicio.getIva() / 100;
					lblIvaUSD.setText("" + ivausd);

					lblTotalUSD.setText("" + (netousd + ivausd));
				}
			}
		}

	}

	/**
	 * Establece el icono del boton guardar
	 */
	public void setIconAdhesion() {
		// Establece el icono del boton guardar Adhesion
		File archivo = new File("imagenes" + File.separator + "disco-flexible.png");
		Image imagen = new Image(archivo.toURI().toString(), 46, 46, true, true, true);
		btnGuardarAdhesion.setGraphic(new ImageView(imagen));
		btnGuardarAdhesion.setContentDisplay(ContentDisplay.CENTER);
	}

	@FXML
	public void guardarAdhesion(ActionEvent event) {
		Button btn;

		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		if (btn.equals(btnGuardarAdhesion)) {
			// cierro ventana modal
			Stage stage = (Stage) btn.getParent().getScene().getWindow();
			stage.close();
		}

	}

	@FXML
	public void agregarItems(ActionEvent event) {
		// crear objeto del modelo
		Adhesion adhesion = new Adhesion();
		Button btn;

		if (event.getSource() instanceof Button)
			btn = (Button) event.getSource();
		else
			return;

		// sacar los datos de pantalla y agrega al modelo
		if (btn.equals(btnAgregarServicio)) {
			String nombreServicio = getSelectedItem(comboServicio);
			Servicio servicio = Servicio.loadServicio(nombreServicio);
			adhesion.setServicio(servicio);

			if (servicio.getTipoMoneda().equals("ARS")) {
				// sumamos los valores
				float netoarg = Float.parseFloat(lblNetoARS.getText());
				netoarg += servicio.getMonto();
				lblNetoARS.setText("" + netoarg);

				float ivaarg = Float.parseFloat(lblIvaARS.getText());
				ivaarg += servicio.getMonto() * servicio.getIva() / 100;
				lblIvaARS.setText("" + ivaarg);

				lblTotalARS.setText("" + (netoarg + ivaarg));
			} else {
				// sumamos los valores
				float netousd = Float.parseFloat(lblNetoUSD.getText());
				netousd += servicio.getMonto();
				lblNetoUSD.setText("" + netousd);

				float ivausd = Float.parseFloat(lblIvaUSD.getText());
				ivausd += servicio.getMonto() * servicio.getIva() / 100;
				lblIvaUSD.setText("" + ivausd);

				lblTotalUSD.setText("" + (netousd + ivausd));
			}

		} else {

			int cuotas = getCuotas();
			String detalle = getSelectedItem(comboItem);
			float iva = Adhesion.getIvaItem(detalle);
			String tipoMoneda = Adhesion.getCodigoMoneda(detalle);
			float monto = Adhesion.getMontoItem(detalle);

			adhesion.setItem(monto, detalle, tipoMoneda, cuotas - 1, cuotas, iva);
			if (tipoMoneda.equals("ARS")) {
				// sumamos los valores
				float netoarg = Float.parseFloat(lblNetoARS.getText());
				netoarg += monto;
				lblNetoARS.setText("" + netoarg);

				float ivaarg = Float.parseFloat(lblIvaARS.getText());
				ivaarg += monto * iva / 100;
				lblIvaARS.setText("" + ivaarg);

				lblTotalARS.setText("" + (netoarg + ivaarg));
			} else {
				// sumamos los valores
				float netousd = Float.parseFloat(lblNetoUSD.getText());
				netousd += monto;
				lblNetoUSD.setText("" + netousd);

				float ivausd = Float.parseFloat(lblIvaUSD.getText());
				ivausd += monto * iva / 100;
				lblIvaUSD.setText("" + ivausd);

				lblTotalUSD.setText("" + (netousd + ivausd));
			}

		}

		// agregar modelo a la tabla
		adhesiones.add(adhesion);

		tablaAdhesion.getItems().add(adhesion);
	}

	/**
	 * @return the comboServicio
	 */
	public ComboBox<String> getComboServicio() {
		return comboServicio;
	}

	/**
	 * @param comboServicio the comboServicio to set
	 */
	public void setComboServicio(ComboBox<String> comboServicio) {
		this.comboServicio = comboServicio;
	}

	/**
	 * @return the txtMonto
	 */
	public TextField getTxtMonto() {
		return txtMonto;
	}

	/**
	 * @param txtMonto the txtMonto to set
	 */
	public void setTxtMonto(TextField txtMonto) {
		this.txtMonto = txtMonto;
	}

	/**
	 * @return the comboMoneda
	 */
	public ComboBox<String> getComboMoneda() {
		return comboMoneda;
	}

	/**
	 * @param comboMoneda the comboMoneda to set
	 */
	public void setComboMoneda(ComboBox<String> comboMoneda) {
		this.comboMoneda = comboMoneda;
	}

	/**
	 * @return the comboIva
	 */
	public ComboBox<String> getComboIva() {
		return comboIva;
	}

	/**
	 * @param comboIva the comboIva to set
	 */
	public void setComboIva(ComboBox<String> comboIva) {
		this.comboIva = comboIva;
	}

	/**
	 * @return the comboCuotas
	 */
	public ComboBox<String> getComboCuotas() {
		return comboCuotas;
	}

	/**
	 * @param comboCuotas the comboCuotas to set
	 */
	public void setComboCuotas(ComboBox<String> comboCuotas) {
		this.comboCuotas = comboCuotas;
	}

	@FXML
	public void initialize() {
		setIconAdhesion();
		comboCuotas.setItems(Servicio.llenarCuotas());
		comboItem.setItems(Adhesion.llenarItemDescripcion());
		llenarTabla();
	}

	/**
	 * 
	 * @param nombre
	 */
	public void cargamosTitular(String nombre) {
		labelTitular.setText(nombre);
	}

	/**
	 * 
	 */
	public void cargarComboServicio(String tipo) {
		comboServicio.setItems(Servicio.llenarServicioFiltrado(tipo));
	}

	/**
	 * Permite llenar la tabla de servicio con el nombre del servicio y las cuotas
	 */
	public void llenarTabla() {

		// Cargo los items
		colServicio.setCellValueFactory(new PropertyValueFactory<Adhesion, String>("") {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Adhesion, String> param) {
				StringProperty property = new SimpleStringProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					property.set(adhesion.getItemDescripcion());
				} else {
					Servicio servicio = adhesion.getServicio();
					property.set(servicio.getNombre());
				}
				return property;
			}
		});
		colMoneda.setCellValueFactory(new PropertyValueFactory<Adhesion, String>("") {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Adhesion, String> param) {
				StringProperty property = new SimpleStringProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					property.set(adhesion.getItemMoneda());
				} else {
					Servicio servicio = adhesion.getServicio();
					property.set(servicio.getTipoMoneda());
				}
				return property;
			}
		});
		colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Adhesion, Float>("") {
			@Override
			public ObservableValue<Float> call(CellDataFeatures<Adhesion, Float> param) {
				SimpleFloatProperty property = new SimpleFloatProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					property.set(adhesion.getItemMonto());
				} else {
					Servicio servicio = adhesion.getServicio();
					property.set(servicio.getMonto());
				}
				return property.asObject();
			}
		});
		colIva.setCellValueFactory(new PropertyValueFactory<Adhesion, Float>("") {
			@Override
			public ObservableValue<Float> call(CellDataFeatures<Adhesion, Float> param) {
				SimpleFloatProperty property = new SimpleFloatProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					property.set(adhesion.getItemIva());
				} else {
					Servicio servicio = adhesion.getServicio();
					property.set(servicio.getIva());
				}
				return property.asObject();
			}
		});
		colPrecioTotal.setCellValueFactory(new PropertyValueFactory<Adhesion, Float>("") {
			@Override
			public ObservableValue<Float> call(CellDataFeatures<Adhesion, Float> param) {
				SimpleFloatProperty property = new SimpleFloatProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					float iva = 1 + adhesion.getItemIva() / 100;
					property.set(adhesion.getItemMonto() * iva);
				} else {
					Servicio servicio = adhesion.getServicio();
					float iva = 1 + servicio.getIva() / 100;
					property.set(servicio.getMonto() * iva);
				}
				return property.asObject();
			}
		});

		colCuotas.setCellValueFactory(new PropertyValueFactory<Adhesion, Integer>("") {
			@Override
			public ObservableValue<Integer> call(CellDataFeatures<Adhesion, Integer> param) {
				SimpleIntegerProperty property = new SimpleIntegerProperty();
				Adhesion adhesion = param.getValue();

				if (adhesion.getServicio() == null) {
					property.set(adhesion.getItemCuotas());
					return property.asObject();
				} else {
					return null;
				}

			}
		});

		// falta llenar tabla con los datos de la base de datos
	}

	public void setAdhesiones(List<Adhesion> adhesiones) {
		this.adhesiones = adhesiones;
	}

	/**
	 * Permite retornar la cantidad de cuotas seleccionadas del combo Cuotas
	 * 
	 * @return retorna la cantida de cuotas
	 */
	private int getCuotas() {
		int cuotas;
		int indice = comboCuotas.getSelectionModel().getSelectedIndex();

		switch (indice) {
		case 0:
			cuotas = 1;
			break;
		case 1:
			cuotas = 2;
			break;
		case 2:
			cuotas = 3;
			break;
		case 3:
			cuotas = 6;
			break;
		case 4:
			cuotas = 8;
			break;
		case 5:
			cuotas = 12;
			break;
		default:
			cuotas = 0;
		}
		return cuotas;
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
