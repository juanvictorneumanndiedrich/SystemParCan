package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.AsistenciaModelo;
import modelo.ClaseModelo;

/**
 * DAO de {@link ClaseModelo}: acceso a datos de una clase.
 * Hereda de {@link GenericDAO} las operaciones guardar, recuperarPorId y recuperarTodo;
 * eliminar se redefine para evitar que la cascada del grupo/inscripcion deshaga la baja.
 */
public class ClaseDAO extends GenericDAO<ClaseModelo> {

	public ClaseDAO() {
		super(ClaseModelo.class);

	}

	/**
	 * Elimina la clase (y sus asistencias, por cascada). El grupo y las inscripciones tienen la clase/asistencia
	 * en una lista con cascade = ALL; si siguen apuntandola, Hibernate la vuelve a persistir al hacer flush y la baja
	 * no se concreta (sin dar error). Por eso primero se quita la clase de la lista del grupo y cada asistencia de la
	 * lista de su inscripcion, y recien despues se elimina.
	 */
	@Override
	public void eliminar(ClaseModelo entity) throws Exception {
		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction();
			try {
				ClaseModelo clase = session.find(ClaseModelo.class, entity.getClase_id());
				if (clase != null) {
					if (clase.getGrupoCatequesis() != null && clase.getGrupoCatequesis().getClases() != null) {
						clase.getGrupoCatequesis().getClases().remove(clase);
					}
					if (clase.getAsistencias() != null) {
						for (AsistenciaModelo asistencia : clase.getAsistencias()) {
							if (asistencia.getInscripcion() != null && asistencia.getInscripcion().getAsistencias() != null) {
								asistencia.getInscripcion().getAsistencias().remove(asistencia);
							}
						}
					}
					session.remove(clase);
				}
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null)
					transaction.rollback();
				e.printStackTrace();
				throw e;
			}
		}
	}

}
