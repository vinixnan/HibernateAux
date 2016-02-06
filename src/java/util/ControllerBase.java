/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Collection;
import javax.faces.event.ActionEvent;
import model.Administrador;
import model.Parceiro;
import model.Usuario;

/**
 *
 * @author Vinicius
 */
public abstract class ControllerBase extends FacesControllerBase {

    public ControllerBase() {
        this.urlBase = "/faces/";
    }

    public void goToLogin() {
        if (!this.isLogadoAdmin()) {
            this.goToPage("gerencia/login.xhtml");
        }
    }

    public final boolean isLogadoAdmin() {
        Administrador admin = (Administrador) this.getObjectFromSession("administradorLogado");
        if (admin != null) {
            return (admin.getUsuario() != null);
        }
        return false;
    }
    
    public Parceiro parceiroLogado()
    {
         return (Parceiro) this.getObjectFromSession("parceiroLogado");
    }
    
     public final boolean isLogadoParceiro() {
        Parceiro admin = (Parceiro) this.getObjectFromSession("parceiroLogado");
        if (admin != null) {
            return (admin.getUsuario() != null);
        }
        return false;
    }
     
     
     public final boolean isLogadoUsuario() {
        Usuario admin = (Usuario) this.getObjectFromSession("usuarioLogado");
        if (admin != null) {
            return true;
        }
        return false;
    }
     
    public final boolean isLogado()
    {
        return(this.isLogadoParceiro() || this.isLogadoUsuario());
    }
    
    protected Usuario loginUsuario()
    {
        Usuario user=(Usuario) this.getObjectFromSession("usuarioLogado");
        if(user==null)
        {
            if(!this.isLogadoParceiro())
                this.goToPage("login.xhtml");
        }
        return user;
    }
    
    protected void remountUsuario()
    {
        Usuario user=(Usuario) this.getObjectFromSession("usuarioLogado");
        if(user!=null)
        {
            int id=user.getIdusuario();
            Usuario usr=new Usuario();
            usr=(Usuario) getObjectById(id, usr); 
            this.setObjectFromSession("usuarioLogado",usr);
        }
        else
        {
             Parceiro parca=(Parceiro) this.getObjectFromSession("parceiroLogado");
             int id=parca.getUsuario().getIdusuario();
             Parceiro usr=new Parceiro();
             usr=(Parceiro) getObjectById(id, usr); 
            this.setObjectFromSession("parceiroLogado",usr);
        }
    }

    public int getIdFromEvery(String nomeVariavel)
    {
        int id = this.getIntFromGet(nomeVariavel);
        if(id==0)
        {
            Object value=this.getFromContext(nomeVariavel);
            if(value!=null)
            {
                id=Integer.parseInt(value.toString());
            }
        }
        return id;
    }
    
    public void getById(String nomeVariavel) {
        int id = this.getIdFromEvery(nomeVariavel);
        if (id != 0) {
            this.isUpdate = true;
            this.getElementById(id);
        } else {
            
            this.isUpdate = false;
            this.setId(0);
        }
    }
    
    public void getById() {
        this.getById("id");
    }

    public void excluir(ActionEvent event) {
        this.delete(this.getIntFromEvent(event, "editId"));
    }

    public abstract void setId(int id);

    public abstract int getId();

    public abstract Collection getValuesComboBox();
    
    public void salvar()
    {
        this.save();
    }
    
    public String getVideoFromGet()
    {
        String str=this.getStringFromGet("urlVideo");
        str="http://www.youtube.com/embed/"+str;
        return str;
    }
}
