package net.bobnar.marketplace.data.entities;

import net.bobnar.marketplace.common.dtos.catalog.v1.ads.Ad;
import net.bobnar.marketplace.data.converters.AdConverter;
import net.bobnar.marketplace.data.enums.BodyType;
import net.bobnar.marketplace.data.enums.EngineType;
import net.bobnar.marketplace.data.enums.StateType;
import net.bobnar.marketplace.data.enums.TransmissionType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ads")
@NamedQuery(
        name="Ads.findAll",
        query="SELECT e FROM AdEntity e"
)
@NamedQuery(
        name="Ads.findBySellerId",
        query="SELECT e FROM AdEntity e WHERE e.sellerId=:sellerId"
)
@NamedQuery(
        name="Ads.findByBrandId",
        query="SELECT e FROM AdEntity e WHERE e.brandId=:brandId"
)
@NamedQuery(
        name="Ads.findByModelId",
        query="SELECT e FROM AdEntity e WHERE e.modelId=:modelId"
)
public class AdEntity extends EntityBase<Ad> implements Serializable {
    private String title;
    private String source;
    private String sourceId;

    @ManyToOne
    @JoinColumn(name="sellerid", insertable = false, updatable = false)
    private SellerEntity seller;
    private Integer sellerId;

    private String originalUri;
    private String photoUri;

    @ManyToOne
    @JoinColumn(name="brandid", insertable = false, updatable = false)
    private CarBrandEntity brand;
    private Integer brandId;

    @ManyToOne
    @JoinColumn(name="modelid", insertable = false, updatable = false)
    private CarModelEntity model;
    private Integer modelId;

    private int firstRegistrationYear;
    private int firstRegistrationMonth;
    private int drivenDistanceKm;
    @Enumerated(EnumType.ORDINAL)
    private EngineType engineType;
    @Enumerated(EnumType.ORDINAL)
    private TransmissionType transmissionType;
    @Enumerated(EnumType.ORDINAL)
    private StateType stateType;
    @Enumerated(EnumType.ORDINAL)
    private BodyType bodyType;
    private String additionalNotes;
    private String otherData;
    private double price;


    public SellerEntity getSeller() {
        return seller;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public CarBrandEntity getBrand() {
        return brand;
    }

//    public void setBrand(CarBrandEntity brand) {
//        this.brand = brand;
//    }

    public CarModelEntity getModel() {
        return model;
    }

//    public void setModel(CarModelEntity model) {
//        this.model = model;
//    }

    public int getFirstRegistrationYear() {
        return firstRegistrationYear;
    }

    public void setFirstRegistrationYear(int firstRegistrationYear) {
        this.firstRegistrationYear = firstRegistrationYear;
    }

    public int getFirstRegistrationMonth() {
        return firstRegistrationMonth;
    }

    public void setFirstRegistrationMonth(int firstRegistrationMonth) {
        this.firstRegistrationMonth = firstRegistrationMonth;
    }

    public int getDrivenDistanceKm() {
        return drivenDistanceKm;
    }

    public void setDrivenDistanceKm(int drivenDistanceKm) {
        this.drivenDistanceKm = drivenDistanceKm;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(EngineType engineType) {
        this.engineType = engineType;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(TransmissionType transmissionType) {
        this.transmissionType = transmissionType;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalUri() {
        return originalUri;
    }

    public void setOriginalUri(String orginalUri) {
        this.originalUri = orginalUri;
    }

    public Integer getSellerId() {
        return sellerId;
    }

    public void setSellerId(Integer sellerId) {
        this.sellerId = sellerId;
    }

    @Override
    public Ad toDto() {
        return AdConverter.getInstance().toDto(this);
    }
}
