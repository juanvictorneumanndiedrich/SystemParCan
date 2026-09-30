package dao;

import modelo.InscripcionModelo;

/**
 * DAO de {@link InscripcionModelo}: acceso a datos de una inscripcion.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class InscripcionDAO extends GenericDAO<InscripcionModelo> {

	public InscripcionDAO() {
		super(InscripcionModelo.class);
	}

}