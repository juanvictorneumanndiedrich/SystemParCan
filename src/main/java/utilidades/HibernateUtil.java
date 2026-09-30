package utilidades;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Esta clase se encarga de configurar y mantener una unica conexión
 *  global para toda nuestra aplicación (SessionFactory)
 */
public class HibernateUtil {

	/** Unica SessionFactory de la aplicacion, creada al cargar la clase. */
	private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

	/**
	 * Construye la SessionFactory leyendo {@code hibernate.cfg.xml}.
	 *
	 * @return la SessionFactory lista para usar
	 * @throws ExceptionInInitializerError si la configuracion o la conexion fallan
	 */
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

	/**
	 * Da acceso a la SessionFactory unica; los DAO la usan para abrir sesiones.
	 *
	 * @return la SessionFactory de la aplicacion
	 */
	// Metodo para obtener la session en nuestro DAO
	public static SessionFactory getSessionFactory() {
		return SESSION_FACTORY;
	}

	/** Cierra la SessionFactory y libera las conexiones; se llama al salir de la aplicacion. */
	// Metodo opcional para cerrra la fabrica salir de la aplicación
	public static void shutdown() {
		getSessionFactory().close();
	}

}