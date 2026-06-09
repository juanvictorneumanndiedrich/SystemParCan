package utilidades;

import dao.EtapaDAO;
import modelo.EtapaModelo;

public class PruebaDeConexion {

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
