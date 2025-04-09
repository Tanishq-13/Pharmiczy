package com.example.pharmiczy.DataModels;

import java.io.Serializable;
import java.util.List;

public class Medicine implements Serializable {
    private String _id;
    private String productName;
    private String brandName;
    private String genericName;
    private String manufacturer;
    private String description;
    private String category;
    private boolean prescriptionRequired;
    private List<String> images;
    private Composition composition;
    private Dosage dosage;
    private Pricing pricing;
    private Stock stock;
    private Packaging packaging;
    private Regulatory regulatory;
    private AdditionalFeatures additionalFeatures;

    // Getters
    public String getId() { return _id; }
    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public String getGenericName() { return genericName; }
    public String getManufacturer() { return manufacturer; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public boolean isPrescriptionRequired() { return prescriptionRequired; }
    public List<String> getImages() { return images; }
    public Composition getComposition() { return composition; }
    public Dosage getDosage() { return dosage; }
    public Pricing getPricing() { return pricing; }
    public Stock getStock() { return stock; }
    public Packaging getPackaging() { return packaging; }
    public Regulatory getRegulatory() { return regulatory; }
    public AdditionalFeatures getAdditionalFeatures() { return additionalFeatures; }

    // Setters
    public void setId(String _id) { this._id = _id; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setPrescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; }
    public void setImages(List<String> images) { this.images = images; }
    public void setComposition(Composition composition) { this.composition = composition; }
    public void setDosage(Dosage dosage) { this.dosage = dosage; }
    public void setPricing(Pricing pricing) { this.pricing = pricing; }
    public void setStock(Stock stock) { this.stock = stock; }
    public void setPackaging(Packaging packaging) { this.packaging = packaging; }
    public void setRegulatory(Regulatory regulatory) { this.regulatory = regulatory; }
    public void setAdditionalFeatures(AdditionalFeatures additionalFeatures) { this.additionalFeatures = additionalFeatures; }
}
