package org.mdv.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.function.Consumer;

public class TransactionUtil {

    public static void doInTransaction(EntityManagerFactory emf, Consumer<EntityManager> action) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            action.accept(em);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (!em.getTransaction().isActive()){
                throw new RuntimeException("Error en transacción JPA", e);
            }
            em.getTransaction().rollback();
        }
    }

    public static void doInSession(EntityManagerFactory emf, Consumer<EntityManager> action) {
        try (EntityManager em = emf.createEntityManager()) {

            action.accept(em);

        } catch (Exception e) {
            throw new RuntimeException("Error al ejecutar consulta JPA", e);
        }
    }
}
