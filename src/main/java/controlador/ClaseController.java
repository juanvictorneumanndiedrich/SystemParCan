package controlador;

import java.util.List;

import dao.ClaseDAO;
import interfaces.InterfaceABM;
import modelo.ClaseModelo;
import tabla.ModeloTablaClase;
import vista.ClaseVista;

public class ClaseController implements InterfaceABM {

	
	 private ClaseVista vista;
	    private ClaseModelo clase;
	    private ClaseDAO dao;
	    private List<ClaseModelo> clases;
	    private ModeloTablaClase tabla; 
	    
	
	public ClaseController(ClaseVista vista) {
			super();
			this.vista = vista;
		}
	
	
	
	

	@Override
	public void nuevo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardar() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buscar() {
		// TODO Auto-generated method stub
		
	}

}
