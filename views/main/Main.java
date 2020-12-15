package main;

import java.io.File;

import db.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("/login/LoginSIDECOM.fxml"));

			BackgroundImage fondo = new BackgroundImage(
					new Image(new File("imagenes" + File.separator + "grisDegrade.jpg").toURI().toString()),
					BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
					new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true));
			root.setBackground(new Background(fondo));

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			// configuracion de base de datos
			DataBase db = DataBase.getInstancia();
			// db.setDataBase("172.16.24.6", "sidecom", 3306, "sitca", "159753");
			db.setDataBase("localhost", "sidecom", 3306, "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
