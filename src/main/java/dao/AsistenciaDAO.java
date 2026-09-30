package dao;

import modelo.AsistenciaModelo;

/**
 * DAO de {@link AsistenciaModelo}: acceso a datos de una asistencia.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class AsistenciaDAO extends GenericDAO<AsistenciaModelo> {

	public AsistenciaDAO() {
		super(AsistenciaModelo.class);

	}

}