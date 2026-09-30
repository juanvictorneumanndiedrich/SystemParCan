package dao;

import modelo.SacramentoModelo;

/**
 * DAO de {@link SacramentoModelo}: acceso a datos de un sacramento.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class SacramentoDAO extends GenericDAO<SacramentoModelo> {

	public SacramentoDAO() {
		super(SacramentoModelo.class);

	}

}