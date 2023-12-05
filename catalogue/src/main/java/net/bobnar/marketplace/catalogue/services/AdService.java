//package net.bobnar.marketplace.catalogue.services;
//
//import com.kumuluz.ee.rest.beans.QueryParameters;
//import com.kumuluz.ee.rest.utils.JPAUtils;
//import net.bobnar.marketplace.catalogue.entities.AdEntity;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//import java.util.List;
//
//@RequestScoped
//public class AdService extends EntityServiceBase {
//
//    public List<AdEntity> getAllAds() {
//        return this.getAds(new QueryParameters());
//    }
//
//    public List<AdEntity> getAds(QueryParameters query) {
//        return JPAUtils.queryEntities(this.em, AdEntity.class, query);
//    }
//
//    public Long getQueryItemsCount(QueryParameters query) {
//        return JPAUtils.queryEntitiesCount(this.em, AdEntity.class, query);
//    }
//
//    public AdEntity getAd(Integer id) {
//        return this.em.find(AdEntity.class, id);
//    }
//
//    public void saveAd(AdEntity ad) {
//        if (ad != null) {
//            try {
//                this.beginTransaction();
//                this.em.persist(ad);
//                this.commitTransaction();
//            } catch (Exception e) {
//                this.rollbackTransaction();
//            }
//        }
//    }
//
//    public void deleteAd(Integer id)  {
//        AdEntity ad = this.em.find(AdEntity.class, id);
//
//        if (ad != null) {
//            try {
//                this.beginTransaction();
//                this.em.remove(ad);
//                this.commitTransaction();
//            } catch (Exception e) {
//                this.rollbackTransaction();
//            }
//        }
//    }
//}
