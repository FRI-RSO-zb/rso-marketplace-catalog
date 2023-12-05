package net.bobnar.marketplace.catalogue.services;

import net.bobnar.marketplace.catalogue.entities.SellerEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class SellerService extends EntityServiceBase {

    public List<SellerEntity> getSellers() {
        List<SellerEntity> sellers = this.em
                .createNamedQuery("Sellers.findAll", SellerEntity.class)
                .getResultList();

        return sellers;
    }

    public SellerEntity getSeller(Integer id) {
        return this.em.find(SellerEntity.class, id);
    }

    public void saveSeller(SellerEntity seller) {
        if (seller != null) {
            try {
                this.beginTransaction();
                this.em.persist(seller);
                this.commitTransaction();
            } catch (Exception e) {
                this.rollbackTransaction();
            }
        }
    }

    public void deleteSeller(Integer id) {
        SellerEntity seller = this.em.find(SellerEntity.class, id);

        if (seller != null) {
            try {
                this.beginTransaction();
                this.em.remove(seller);
                this.commitTransaction();
            } catch (Exception e) {
                this.rollbackTransaction();
            }
        }
    }
}
