package net.bobnar.marketplace.catalogue.services;

import net.bobnar.marketplace.catalogue.entities.AdEntity;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class AdService {

    @PersistenceContext
    private EntityManager em;

    public List<AdEntity> getAds() {
        List<AdEntity> ads = em
                .createNamedQuery("Ads.findAll", AdEntity.class)
                .getResultList();

        return ads;
    }

    public AdEntity getAd(Integer id) {
        return em.find(AdEntity.class, id);
    }

    @Transactional
    public void saveAd(AdEntity ad) {
        if (ad != null) {
            em.persist(ad);
        }
    }

//    @Transactional(Transactional.TxType.REQUIRED)
    public void deleteAd(Integer id)  {
        AdEntity ad = em.find(AdEntity.class, id);
        if (ad != null) {
            em.remove(ad);
        }
        em.flush();
    }
}
