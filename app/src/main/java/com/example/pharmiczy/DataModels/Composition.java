package com.example.pharmiczy.DataModels;

import java.io.Serializable;
import java.util.List;

public class Composition implements Serializable {
    public List<String> activeIngredients;
    public List<String> inactiveIngredients;
}

