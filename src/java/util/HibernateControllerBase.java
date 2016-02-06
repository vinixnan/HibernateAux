/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author vinicius
 */
public class HibernateControllerBase implements Serializable {

    protected Session session;
    protected Class persistentClass;
    protected Transaction t;
    protected Object objeto;
    protected boolean isUpdate = false;

    public HibernateControllerBase() {
        session = null;
    }

    public HibernateControllerBase(Class persistentClass) {
        session = HibernateUtil.getSession();
        this.persistentClass = persistentClass;
    }

    protected boolean connect() {
        session = HibernateUtil.getSession();
        t = session.beginTransaction();
        return session.isConnected();
    }

    protected boolean disconnect() {
        if (session.isConnected()) {
            this.session.close();
        }
        return !this.session.isConnected();
    }

    public boolean insert(Object objeto) {

        try {
            this.connect();
            session.save(objeto);
            t.commit();
            return true;
        } catch (HibernateException e) {
            System.out.println("Insert_HibernateDAO: " + e);
            t.rollback();
            return false;
        }
        finally
        {
            this.disconnect();
        }

    }
    
    public boolean insert()
    {
        return this.insert(this.objeto);
    }
    
    public boolean delete(Object objeto) {
        System.out.println("Delete " + objeto.getClass().getSimpleName());
        try {
                Class classe=objeto.getClass();
                this.connect();
                session.delete(objeto);
                t.commit();
                objeto=this.createInstance(classe);
                return true;
        } catch (HibernateException e) {
            System.out.println("Delete_HibernateDAO: " + e);
            t.rollback();
            return false;
        }
        finally
        {
            this.disconnect();
        }

    }

    public boolean delete() {
        return this.delete(this.objeto);
    }
    
    public boolean delete(int id)
    {
        this.getElementById(id);
        return this.delete();
    }
    
    public boolean update(Object objeto) {
        System.out.println("Update");
        try {
            this.connect();
            session.update(objeto);
            t.commit();
            return true;

        } catch (HibernateException e) {
            System.out.println("Update_HibernateDAO: " + e);
            t.rollback();
            return false;
        }
        finally
        {
            this.disconnect();
        }

    }
    
    public boolean update()
    {
        return this.update(this.objeto);
    }

    public List findAll() {
        List<Object> resultList = new ArrayList<Object>();
        try {
            this.connect();
            String nameTabela = this.objeto.getClass().getSimpleName();
            Query query = session.getNamedQuery(nameTabela + ".findAll");
            resultList = query.list();
        } catch (HibernateException e) {
            System.out.println("Hibernate Exception: " + e);
        }
        finally
        {
            this.disconnect();
        }
        return resultList;

    }

    public boolean save() {

        if (this.isUpdate) {
            return this.update(this.objeto);
        } else {
            return this.insert(this.objeto);
        }
    }

    public void save(Object item) {

        if (this.isUpdate) {
            this.update(item);
        } else {
            this.insert(item);
        }
    }

    public void setObjeto(Object objeto) {
        this.objeto = objeto;
    }

    public Object getObjeto() {
        return objeto;
    }

    

    public boolean isIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
        
    }

   
    

    public void getElementById(int id) {
        this.objeto=this.getObjectById(id, this.objeto);
    }
    
    private Object createInstance(Class classe)
    {
        Object obj=null;
        try 
        {
            obj= classe.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(HibernateControllerBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
    
    protected Object getObjectById(int id, Object objeto) 
    {
        Class classe=objeto.getClass();
        Object obj=null;
        try {
            System.out.println("Get: em " + objeto.getClass().getSimpleName() + ".findById ID" + id);
            this.connect();
            Query query = this.session.getNamedQuery(objeto.getClass().getSimpleName() + ".findById").setInteger("id", id);
            obj=query.uniqueResult();
        } catch (HibernateException e) {
            System.out.println("Hibernate Exception: " + e);
        }
        finally
        {
            this.disconnect();
        }
        if(obj==null)
        {
            obj=this.createInstance(classe);
        }
        return obj;
    }
    
    
}
