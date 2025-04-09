package com.example.pharmiczy.DataModels;

import java.io.Serializable;

public class Packaging  implements Serializable {
    public String packSize;
    public String expiryDate; // You can convert to Date type if you prefer
    public String storageInstructions;
}
