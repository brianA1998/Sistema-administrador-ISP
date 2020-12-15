package controlador;

import me.legrange.mikrotik.ApiConnection;
import me.legrange.mikrotik.MikrotikApiException;
import modelo.Servicio;

public class Mikrotik {
	// IP del router
	public static final String LOCAL_ADDRESS = "192.168.1.200";
	public static final String USER = "admin";
	public static final String PASS = "sdc159753";

	/**
	 * inserta un nuevo profile de un servicio la unidades pueden ser Kilobit (K) o
	 * Megabit (M) ejemplo: 5M son 5 Megabit, o 5000K son 5000 Kilobit
	 * 
	 * @param servicio datos del servicio
	 */
	public void insertProfile(Servicio servicio) {
		if (servicio == null)
			return;
		try {
			String comando = "/ppp/profile/add name=" + servicio.getCodigo();
			comando += " local-address=" + LOCAL_ADDRESS;
			comando += " remote-address=pool_VPN"; // corregir pool
			comando += " rate-limit=" + servicio.getSubida() + "/" + servicio.getBajada();
			ApiConnection con = ApiConnection.connect(LOCAL_ADDRESS);
			con.setTimeout(1500);
			con.login(USER, PASS);
			con.execute(comando);
			con.close();
		} catch (MikrotikApiException e) {
			e.printStackTrace();
		}
	}

}