package comunicacion;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import db.DataBase;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import modelo.Cliente;
import modelo.Plantilla;

public class JavaMail {

	public void mandarCorreo(Cliente[] clientes, Plantilla plantilla) {

		// El correo gmail de envío
		String correoEnvia = "sistemas@sidecom.com.ar";
		String claveCorreo = "Sistemas2020";

		// La configuración para enviar correo
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.sidecom.com.ar");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.user", correoEnvia);
		properties.put("mail.password", claveCorreo);

		int idPlantillaCorreo = plantilla.getPlantillaId();

		// Consulta sql
		String sql = "INSERT INTO correo_enviado(cliente_documento, plantilla_correo_id, correo_enviado_fecha, correo_enviado_estado) VALUES (?,?,CURRENT_DATE(),?)";
		DataBase db = new DataBase();
		db.setQuery(sql);

		// Obtener la sesion
		Session session = Session.getInstance(properties, null);

		try {
			// Crear el cuerpo del mensaje
			MimeMessage mimeMessage = new MimeMessage(session);

			// Agregar quien envía el correo
			mimeMessage.setFrom(new InternetAddress(correoEnvia));

			// Agregar el asunto al correo
			mimeMessage.setSubject(plantilla.getTitulo());

			// Creo la parte del mensaje
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setText(plantilla.getMensaje());

			// Crear el multipart para agregar la parte del mensaje anterior
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			// Agregar el multipart al cuerpo del mensaje
			mimeMessage.setContent(multipart);

			// Los destinatarios
			InternetAddress internetAddresses;
			for (int i = 0; i < clientes.length; i++) {

				db.setParametro(1, clientes[i].getDocumento());
				db.setParametro(2, idPlantillaCorreo);

				try {
					internetAddresses = new InternetAddress(clientes[i].getCorreo());

					// Agregar los destinatarios al mensaje
					mimeMessage.setRecipient(Message.RecipientType.TO, internetAddresses);

					// Enviar el mensaje
					Transport transport = session.getTransport("smtp");
					transport.connect(correoEnvia, claveCorreo);
					transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
					transport.close();

					db.setParametro(3, 1);
				} catch (Exception e) {

					db.setParametro(3, 0);
				} finally {
					db.addBatch();
				}
			}

			db.executeBatch();

		} catch (Exception ex) {

		}
		Alert alerta = new Alert(AlertType.INFORMATION);
		alerta.setTitle("Exito");
		alerta.setHeaderText("Correo/s enviado/s con exito");
		alerta.showAndWait();
	}

}
