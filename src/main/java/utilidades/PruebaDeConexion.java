package utilidades;

import dao.EtapaDAO;
import modelo.EtapaModelo;

/**
 * Clase de prueba manual: guarda una etapa de ejemplo para comprobar que Hibernate
 * se conecta a la base de datos. No forma parte del sistema; se ejecuta por separado.
 */
public class PruebaDeConexion {

	/**
	 * Inserta una {@code EtapaModelo} de prueba en la base de datos.
	 *
	 * @param args sin uso
	 */
	public static void main(String[] args) {
		System.out.println("Iniciando Prueba de Hibernate 7 ------");

		try {
			EtapaDAO dao = new EtapaDAO();
			
			EtapaModelo etapa = new EtapaModelo();
			etapa.setEtap_descripcion("Etapa de Prueba");
			etapa.setEtap_estado(true);
			
			dao.guardar(etapa);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}

}
