package com.example.pharmiczy.DataModels;

import java.io.Serializable;
import java.util.List;

public class Regulatory  implements Serializable {
    public String drugType;
    public List<String> sideEffects;
    public List<String> warnings;
    public List<String> contraindications;
    public List<String> interactions;
}
