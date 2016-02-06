/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jboss.weld.util.collections.ArraySet;
import org.primefaces.event.FileUploadEvent;


/**
 *
 * @author vinicius
 */
public class FacesControllerBase extends HibernateControllerBase
{
    protected String urlBase;
    protected boolean formValido;
    protected boolean print;
    
    
    @Override
    public boolean insert(Object objeto) {
        return this.insert(objeto,"Inserido com Sucesso", "Falha ao inserir");
    }
    
     public boolean insert(Object objeto, String mensagemCorreta, String mensagemFalha) {

        if(formValido)
        {
            if (super.insert(objeto)) {
                this.printInfo(mensagemCorreta);
                return true;
            } else {
                this.printError(mensagemFalha);
                return false;
            }
        }
        return false;

    }

    @Override
    public boolean delete(Object objeto) {
        return this.delete(objeto, "Excluido com Sucesso", "Falha ao excluir");
    }
    
    
    public boolean delete(Object objeto, String mensagemSucesso, String mensagemFalha) {

        if (super.delete(objeto)) {
            this.printInfo(mensagemSucesso);
            return true;
        } else {
            this.printError(mensagemFalha);
            return false;
        }

    }

    @Override
    public boolean update(Object objeto) {
        return this.update(objeto, "Alterado com Sucesso", "Falha ao alterar");
    }
    
     public boolean update(Object objeto, String mensagemSucesso, String mensagemFalha) {
        if (super.update(objeto)) {
            this.printInfo(mensagemSucesso);
            return true;
        } else {
            this.printError(mensagemFalha);
            return false;
        }
    }
    
    public void goToPage() 
    {
        String future=(String) this.getObjectFromSession("futurepage");
        if(future!=null && future!="")
        {
           this.setObjectFromSession("futurepage", null); 
           this.goToPage(future);
        }
        else
            this.goToPage("index.xhtml");
        
    }
    public void goToPage(String page) 
    {
        this.goToOtherPage(this.urlBase+page);
    }
    
    public void goToOtherPage(String page)
    {
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        try {
            response.sendRedirect(page);
            FacesContext.getCurrentInstance().responseComplete();
        } catch (Exception ex) {}
    }
    
    protected Object getFromContext(String variavel)
    {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParameter(variavel);
    }
    
    protected Object getFromEvent(ActionEvent event, String variavel)
    {
        
        Object value=this.getFromContext(variavel);
        if(value==null)
        {
            UIParameter val = (UIParameter) event.getComponent().findComponent(variavel);
            if(val!=null)
                value=val.getValue();
            else
                value="0";
        }
        return value;
    }
     
    protected  int getIntFromEvent(ActionEvent event, String variavel)
    {
        int aux=Integer.parseInt(this.getFromEvent(event, variavel).toString());
        return aux;
    }
    
    protected Date getDateFromEvent(ActionEvent event, String nomevariavel)
    {
        //n]ao funciona por outro modo
        UIParameter val = (UIParameter) event.getComponent().findComponent(nomevariavel);
        if(val!=null)
            return (Date) val.getValue();
        else
            return null;
    }
    
    protected  String getStringFromEvent(ActionEvent event, String variavel)
    {
        return this.getFromEvent(event, variavel).toString();
    }
    
    protected  int getIntFromGet(String variavel)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String id = req.getParameter(variavel);
        if (id != null && !id.isEmpty())
           return Integer.parseInt(id);
        return 0;
    }
    
    protected  String getStringFromGet(String variavel)
    {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        String id = req.getParameter(variavel);
        return id;
        
    }
    
    protected void setErro(String mensagem)
    {
        if(this.isUpdate==false)
        {
            FacesContext context = FacesContext.getCurrentInstance();
            context.validationFailed();
            this.formValido=false;
            this.printError(mensagem);
        }
    }
    
    protected  Object getObjectFromSession(String variavel)
    {
        HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return sessao.getAttribute(variavel);
    }
    
    protected  void setObjectFromSession(String nome, Object variavel)
    {
        HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessao.setAttribute(nome, variavel);
    }
    
    public String formataNome(String nome) {
        //criada para formatar nomes de pessoas e cidades
        char[] array = nome.toLowerCase().trim().toCharArray();
        char[] retorno = new char[array.length];
        boolean upar = false;
        for (int i = 0; i < array.length; i++) {

            if (i == 0) {//primeira letra
                retorno[i] = Character.toUpperCase(array[i]);
            }

            if (Character.isSpace(array[i])) {
                upar = true;
            } else if (upar) {
                retorno[i] = Character.toUpperCase(array[i]);
                upar = false;
            } else {
                retorno[i] = array[i];
            }
        }

        return nome;
    }

    
    
    protected Float formatValueToSave(String valor) {
        valor = valor.replace(",", ".").trim();
        return Float.parseFloat(valor);
    }

    protected String formatValueToGet(Float d) {
        if (d == null) {
            d = new Float(0);
        }
        String valor = String.valueOf(d);
        valor = valor.replace(".", ",").trim();
        return valor;
    }
    
    protected ArrayList toArrayList(Set arrayset)
    {
        ArrayList aux=new ArrayList();
        for(Object obj : arrayset)
        {
            aux.add(obj);
        }
        return aux;
    }
    
    protected Set toArraySet(ArrayList arraylist)
    {
        ArraySet aux=new ArraySet();
        for(Object obj : arraylist)
        {
            aux.add(obj);
        }
        return aux;
    }

    protected void printInfo(String mensagem) {
        if(this.print) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Nota", mensagem));
        }
    }

    protected void printError(String mensagem) {
        if(this.print) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Erro", mensagem));
        }
    }

    protected void printAlert(String mensagem) {
        if(this.print) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Alerta", mensagem));
        }
    }
    
    public void printAlertFromSession()
    {
        Object aux=this.getObjectFromSession("tipomensagem");
        if(aux!=null)
        {
            int tipo=Integer.parseInt(aux.toString());
            String mensagem=this.getObjectFromSession("mensagem").toString();
            if(tipo==1)
            {
                this.printInfo(mensagem);
            }
            else if(tipo==2)
            {
                this.printAlert(mensagem);
            }
            else if(tipo==3)
            {
                this.printError(mensagem);
            }
            this.setObjectFromSession("mensagem", null);
            this.setObjectFromSession("tipomensagem", null);
        }
    }
    
    public void printAlertFromSession(String mensagem, int tipo)
    {
        this.setObjectFromSession("mensagem", mensagem);
        this.setObjectFromSession("tipomensagem", tipo);
    }
     public void handleFileUpload(FileUploadEvent event) {
        if(event!=null && event.getFile()!=null)
        {
            byte[] foto = event.getFile().getContents();
            String nomeArquivo = event.getFile().getFileName();
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
            String arquivo = scontext.getRealPath("/upload/" + nomeArquivo);
            criaArquivo(foto, arquivo);
            this.setUrlImagem(nomeArquivo);
        }
    }
     
    protected void setUrlImagem(String url)
    {
        //herde
    }
     
    protected void criaArquivo(byte[] bytes, String arquivo) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(arquivo);
            fos.write(bytes);
            fos.close();
        } catch (FileNotFoundException ex) {
            printAlert(ex.getMessage());
        } catch (IOException ex) {
             printAlert(ex.getMessage());
        }
    }
    
    @Override
     protected boolean connect() {
        
        if(session==null || !session.isConnected())
        {
            session = HibernateUtil.getSession();
            t = session.beginTransaction();
        }
        return session.isConnected();
    }

    @Override
    protected boolean disconnect() {
        if (session.isConnected()) {
            this.session.close();
        }
        return !this.session.isConnected();
    }

    public boolean isFormValido() {
        return formValido;
    }

    public void setFormValido(boolean formValido) {
        this.formValido = formValido;
    }

    public boolean isPrint() {
        return print;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }
    
    

    public FacesControllerBase() {
       this.connect();
       this.formValido=true;
       this.print=true;
       this.printAlertFromSession();
    }
    
    public void pageToFuture(ActionEvent event)
    {
        String page=this.getStringFromEvent(event, "page");
        String futurepage=FacesContext.getCurrentInstance().getViewRoot().getViewId().replace("/", "");
        String namevar=this.getStringFromEvent(event, "namevar");
        String idPage=this.getStringFromEvent(event, namevar);
        futurepage=futurepage+"?"+namevar+"="+idPage;
        this.setObjectFromSession("futurepage", futurepage);
        this.goToPage(page);
    }
    
    
}
