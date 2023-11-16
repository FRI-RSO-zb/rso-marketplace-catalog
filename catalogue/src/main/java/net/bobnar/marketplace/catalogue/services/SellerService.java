package net.bobnar.marketplace.catalogue.services;

import net.bobnar.marketplace.catalogue.entities.SellerEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class SellerService {

    @PersistenceContext
    private EntityManager em;


    public List<SellerEntity> getSellers() {
        List<SellerEntity> sellers = em
                .createNamedQuery("Sellers.findAll", SellerEntity.class)
                .getResultList();

        return sellers;
    }

    public SellerEntity getSeller(Integer id) {
        return em.find(SellerEntity.class, id);
    }

    @Transactional
    public void saveSeller(SellerEntity seller) {
        if (seller != null) {
            em.persist(seller);
        }
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteSeller(Integer id) {
        SellerEntity seller = em.find(SellerEntity.class, id);
        if (seller != null) {
            em.remove(seller);
        }
    }
}
