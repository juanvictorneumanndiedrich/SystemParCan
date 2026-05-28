package utilidades;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Esta clas se encarga de configurar y mantener una unica conexión
 *  global para toda nuestra aplicación (SessionFactory)
 */
public class HibernateUtil {

	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		try {
			// Crea la SessionFactory a partir del archivo Hibernate.cfg
			// .configure() buscar automaticamente el archivo en src/main/resources
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable e) {
			// Muestra un error si es que no consigue
			System.err.println("Error al inicializr la SessionFactory" + e);
			throw new ExceptionInInitializerError();
		}
	}

	// Metodo para obtener la session en nuestro DAO
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	// Metodo opcional para cerrra la fabrica salir de la aplicación
	public static void shutdown() {
		getSessionFactory().close();
	}

}