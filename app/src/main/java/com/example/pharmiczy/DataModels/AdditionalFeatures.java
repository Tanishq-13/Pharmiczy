package com.example.pharmiczy.DataModels;

import java.io.Serializable;
import java.util.List;

public class AdditionalFeatures  implements Serializable {
    public String doctorAdvice;
    public List<String> alternativeMedicines;
    public List<String> userReviews;
    public List<String> faqs;
}
