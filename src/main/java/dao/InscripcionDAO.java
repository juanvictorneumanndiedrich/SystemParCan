package dao;

import modelo.InscripcionModelo;

public class InscripcionDAO extends GenericDAO<InscripcionModelo> {

	public InscripcionDAO(Class<InscripcionModelo> clase) {
		super(InscripcionModelo.class);

	}

}