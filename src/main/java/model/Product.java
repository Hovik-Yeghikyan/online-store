package model;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import types.ProductType;

import java.io.Serializable;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Product implements Serializable {

    private String id;
    private String name;
    private String description;
    private double price;
    private int stockQty;
    private ProductType type;


}

