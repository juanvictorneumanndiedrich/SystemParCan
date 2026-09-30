package dao;

import modelo.CatequizandoModelo;

/**
 * DAO de {@link CatequizandoModelo}: acceso a datos de un catequizando.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class CatequizandoDAO extends GenericDAO<CatequizandoModelo> {

	public CatequizandoDAO() {
		super(CatequizandoModelo.class);

	}

}