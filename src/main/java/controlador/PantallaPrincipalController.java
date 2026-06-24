package controlador;

import vista.CatequizandoVista;
import vista.PantallaPrincipalVista;
import controlador.CatequizandoController;

public class PantallaPrincipalController {

    private PantallaPrincipalVista vista;

    public PantallaPrincipalController(PantallaPrincipalVista vista) {
        this.vista = vista;
        setAcciones();
    }

    private void setAcciones() {
        this.vista.getMntmprsnlzdCatequizando().addActionListener(e -> abrirCatequizando());
    }

    private void abrirCatequizando() {
        CatequizandoVista catequizandoVista = new CatequizandoVista();
        new CatequizandoController(catequizandoVista);
        catequizandoVista.setLocationRelativeTo(this.vista); // centra respecto a la pantalla principal
        catequizandoVista.setVisible(true);
    }
}