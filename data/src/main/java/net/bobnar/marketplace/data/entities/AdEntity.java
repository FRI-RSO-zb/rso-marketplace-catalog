package net.bobnar.marketplace.data.entities;

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
public class AdEntity extends EntityBase implements Serializable {
    private String title;
    private String source;
    private String sourceId;
    private Integer sellerId;
    private String originalUri;
    private String photoUri;
    private String brand;
    private String model;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

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
}
