package dao;

import modelo.TransferenciaModelo;

/**
 * DAO de {@link TransferenciaModelo}: acceso a datos de una transferencia.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class TransferenciaDAO extends GenericDAO<TransferenciaModelo> {

	public TransferenciaDAO() {
		super(TransferenciaModelo.class);

	}

}