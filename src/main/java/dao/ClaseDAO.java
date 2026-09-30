package dao;

import modelo.ClaseModelo;

/**
 * DAO de {@link ClaseModelo}: acceso a datos de una clase.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class ClaseDAO extends GenericDAO<ClaseModelo> {

	public ClaseDAO() {
		super(ClaseModelo.class);

	}

}