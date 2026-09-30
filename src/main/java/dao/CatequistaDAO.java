package dao;

import modelo.CatequistaModelo;

/**
 * DAO de {@link CatequistaModelo}: acceso a datos de un catequista.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class CatequistaDAO extends GenericDAO<CatequistaModelo> {

	public CatequistaDAO() {
		super(CatequistaModelo.class);

	}

}