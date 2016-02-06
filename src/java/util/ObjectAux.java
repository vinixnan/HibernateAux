/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.HibernateControllerBase;

/**
 *
 * @author Vinicius
 */
public class ObjectAux 
{
    
    
    public ObjectAux() {
       
    }

    protected Object getIn(String nomeCampo, Object objeto)
    {
        
        Object obj=null;
        Field f;
        try {
            f = objeto.getClass().getDeclaredField(nomeCampo);
            f.setAccessible(true);
            obj = f.get(objeto);
        } catch (Exception ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
    
    protected void setIn(String nomeCampo, Object valor, Object objeto)
    {
        Field f;
        try {
            f = objeto.getClass().getDeclaredField(nomeCampo);
            f.setAccessible(true);
            f.set(objeto, valor);
        } catch (Exception ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected Object createInstance(Class classe)
    {
        Object objeto=null;
        try 
        {
            objeto= classe.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return objeto;
    }
}
