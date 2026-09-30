package dao;

import modelo.EtapaModelo;

/**
 * DAO de {@link EtapaModelo}: acceso a datos de una etapa.
 * Hereda de {@link GenericDAO} las operaciones guardar, eliminar, recuperarPorId y recuperarTodo.
 */
public class EtapaDAO extends GenericDAO<EtapaModelo> {

	public EtapaDAO() {
		super(EtapaModelo.class);

	}

}