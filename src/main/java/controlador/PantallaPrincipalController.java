package controlador;

import vista.CatequistaVista;
import vista.CatequizandoVista;
import vista.EtapaVista;
import vista.GrupoCatequesisVista;
import vista.SacramentoVista;
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
        this.vista.getMntmprsnlzdEtapa().addActionListener(e -> abrirEtapa());
        this.vista.getMntmprsnlzdGrupocatequesis().addActionListener(e -> abrirGrupoCatequesis());
        this.vista.getMntmprsnlzdSacramentos().addActionListener(e -> abrirSacramentos());
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

    private void abrirEtapa() {
    	EtapaVista etapaVista = new EtapaVista();
    	new EtapaController(etapaVista);
    	etapaVista.setLocationRelativeTo(this.vista);
    	etapaVista.setVisible(true);
    }

    private void abrirGrupoCatequesis() {
    	GrupoCatequesisVista grupoCatequesisVista = new GrupoCatequesisVista();
    	new GrupoCatequesisController(grupoCatequesisVista);
    	grupoCatequesisVista.setLocationRelativeTo(this.vista);
    	grupoCatequesisVista.setVisible(true);
    }

    private void abrirSacramentos() {
    	SacramentoVista sacramentoVista = new SacramentoVista();
    	new SacramentoController(sacramentoVista);
    	sacramentoVista.setLocationRelativeTo(this.vista);
    	sacramentoVista.setVisible(true);
    }

}