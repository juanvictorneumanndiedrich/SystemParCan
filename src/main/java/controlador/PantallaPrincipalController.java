package controlador;

import vista.CatequistaVista;
import vista.CatequizandoVista;
import vista.PantallaPrincipalVista;
import controlador.CatequizandoController;
import controlador.CatequistaController;

public class PantallaPrincipalController {

    private PantallaPrincipalVista vista;

    public PantallaPrincipalController(PantallaPrincipalVista vista) {
        this.vista = vista;
        setAcciones();
    }

    private void setAcciones() {
        this.vista.getMntmprsnlzdCatequizando().addActionListener(e -> abrirCatequizando());
        this.vista.getMntmprsnlzdCatequista().addActionListener(e -> abrirCatequista());
    }

    private void abrirCatequizando() {
        CatequizandoVista catequizandoVista = new CatequizandoVista();
        new CatequizandoController(catequizandoVista);
        catequizandoVista.setLocationRelativeTo(this.vista); // centra respecto a la pantalla principal
        catequizandoVista.setVisible(true);
    }
    
    private void abrirCatequista() {
        CatequistaVista catequistaVista = new CatequistaVista();
        new CatequistaController(catequistaVista);
        catequistaVista.setLocationRelativeTo(this.vista);
        catequistaVista.setVisible(true);
    }
    
    
}