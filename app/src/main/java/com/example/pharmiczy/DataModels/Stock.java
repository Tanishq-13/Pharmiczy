package com.example.pharmiczy.DataModels;

import java.io.Serializable;

public class Stock  implements Serializable {
    public boolean available;
    public int quantity;
    public int minOrderQuantity;
    public int maxOrderQuantity;
}

