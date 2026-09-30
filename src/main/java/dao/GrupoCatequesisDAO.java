package dao;

import modelo.GrupoCatequesisModelo;

/**
 * DAO de {@link GrupoCatequesisModelo}: acceso a datos de un grupo de catequesis.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class GrupoCatequesisDAO extends GenericDAO<GrupoCatequesisModelo> {

	public GrupoCatequesisDAO() {
		super(GrupoCatequesisModelo.class);

	}

}