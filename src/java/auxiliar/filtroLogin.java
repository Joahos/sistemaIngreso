package auxiliar;

import com.syscompraventa.data.entities.Usuarios;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

@Named(value = "filtroLogin")
@ViewScoped
public class filtroLogin implements Serializable {

    private static final Logger LOG = Logger.getLogger(filtroLogin.class.getName());
    private Usuarios auUser;

    public filtroLogin() {
    }

//    public void autenticacionUnica() {
//        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            auUser = (Usuarios) context.getExternalContext().getSessionMap().get("usFiltro");
//            if (auUser == null) {
//                context.getExternalContext().redirect("./../../index.xhtml");
//            }
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Error", ex);
//        }
//    }
    
//    public void autenticacionUnica() {
//        try {
//            FacesContext context = FacesContext.getCurrentInstance();
//            auUser = (Usuarios) context.getExternalContext().getSessionMap().get("usFiltro");
//            if (auUser == null) {
//                context.getExternalContext().redirect("/sysCompraVenta1.0.0/views/plantillas/index.xhtml");
//            }
//        } catch (IOException ex) {
//            LOG.log(Level.SEVERE, "Error", ex);
//        }
//    }
    
        public void autenticacionUnica() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            auUser = (Usuarios) context.getExternalContext().getSessionMap().get("usFiltro");
            if (auUser == null) {
                context.getExternalContext().redirect("./../index.xhtml");
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error", ex);
        }
    }

    public void serrarSecion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

}
