package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;

import modelo.InscripcionModelo;
import modelo.TransferenciaModelo;

/**
 * DAO de {@link TransferenciaModelo}: acceso a datos de una transferencia.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class TransferenciaDAO extends GenericDAO<TransferenciaModelo> {

	public TransferenciaDAO() {
		super(TransferenciaModelo.class);

	}

	/**
	 * Registra una transferencia en una sola transaccion: actualiza la inscripcion del catequizando (que ya debe
	 * apuntar al grupo destino) y guarda el registro historico de la transferencia. Si algo falla se hace rollback de
	 * las dos cosas, asi nunca queda la inscripcion cambiada sin su transferencia (o al reves).
	 *
	 * @param inscripcion   inscripcion del catequizando, ya con el grupo destino asignado
	 * @param transferencia registro de la transferencia (origen, destino, fecha, observacion)
	 * @throws Exception si la operacion falla; la transaccion queda cancelada
	 */
	public void transferir(InscripcionModelo inscripcion, TransferenciaModelo transferencia) throws Exception {
		try (Session session = getSession()) {
			Transaction transaction = session.beginTransaction();
			try {
				session.merge(inscripcion);
				session.merge(transferencia);
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
