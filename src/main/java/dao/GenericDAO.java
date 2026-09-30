package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import utilidades.HibernateUtil;

/**
 * DAO generico con las operaciones CRUD basicas sobre cualquier entidad JPA.
 *
 * Todos los DAO concretos del sistema ({@code CatequistaDAO}, {@code EtapaDAO}, etc.)
 * extienden esta clase indicando la entidad con la que trabajan, asi que
 * {@link #guardar}, {@link #eliminar}, {@link #recuperarPorId} y {@link #recuperarTodo}
 * ya vienen resueltos y no hace falta repetirlos.
 *
 * Cada operacion abre su propia {@link Session} de Hibernate y la cierra al terminar.
 *
 * @param <T> entidad JPA que maneja el DAO
 */
public class GenericDAO<T> { // la T significa que va a recibir una entidad, cualquier tipo de clase

	/** Clase de la entidad con la que trabaja este DAO (se usa para las consultas HQL). */
	protected Class<T> clase;

	/**
	 * Crea el DAO para una entidad concreta.
	 *
	 * @param clase clase de la entidad (por ejemplo {@code EtapaModelo.class})
	 */
	public GenericDAO(Class<T> clase) {
		super();
		this.clase = clase;
	}

	/**
	 * Abre una nueva sesion de Hibernate usando la {@code SessionFactory} unica de la aplicacion.
	 * Quien la llame debe cerrarla (los metodos de esta clase lo hacen con try-with-resources).
	 *
	 * @return una sesion nueva y abierta
	 */
	// llama el contexto y abre la session
	protected Session getSession() {

		return HibernateUtil.getSessionFactory().openSession();
	}

	/**
	 * Guarda una entidad en la base de datos: la inserta si es nueva o actualiza
	 * la existente (usa {@code merge}). Si algo falla se hace rollback y se relanza el error.
	 *
	 * @param entity entidad a guardar
	 * @throws Exception si la operacion falla; la transaccion queda cancelada
	 */
	public void guardar(T entity) throws Exception {

		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction(); // creamos una nueva transaccion, agarra la sesion
																	// abierta y begin usame esta que esta detras
			try {
				session.merge(entity); // merge unime mi entidad con base de datos
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null)
					transaction.rollback(); // rollback es para cancelar la operacion, no guardar nada de lo que este
											// corrompido
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Elimina una entidad de la base de datos. Si la entidad no pertenece a la sesion
	 * actual, primero se vincula con {@code merge}. Si algo falla se hace rollback
	 * y se relanza el error.
	 *
	 * @param entity entidad a eliminar
	 * @throws Exception si la operacion falla (por ejemplo, por registros relacionados); la transaccion queda cancelada
	 */
	public void eliminar(T entity) throws Exception {
		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction(); // creamos una nueva transaccion, agarra la sesion
																	// abierta y begin usame esta que esta detras
			try {
				session.remove(session.contains(entity) ? entity : session.merge(entity));
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null)
					transaction.rollback(); // rollback es para cancelar la operacion, no guardar nada de lo que este
											// corrompido
				e.printStackTrace();
				throw e;
			}
		}
	}

	/**
	 * Busca una entidad por su clave primaria.
	 *
	 * @param id identificador de la entidad
	 * @return la entidad encontrada, o {@code null} si no existe
	 */
	public T recuperarPorId(Integer id) {
		try (Session session = getSession()) {
			return session.find(clase, id);

		}
	}

	/**
	 * Recupera todos los registros de la entidad, ordenados por id.
	 *
	 * @return lista con todos los registros (vacia si no hay ninguno)
	 */
	public List<T> recuperarTodo() {
		try (Session session = getSession()) {
			String hql = "FROM " + clase.getName() + " e ORDER BY id";
			Query<T> query = session.createQuery(hql, clase);
			return query.getResultList(); // tranformar en una lista
		}

	}
}