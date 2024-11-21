package com.example.login.model;

import java.util.List;

public class ProductListResponseWrapper {
    private List<ProductListResponse> items;

    public List<ProductListResponse> getItems() {
        return items;
    }

    public void setItems(List<ProductListResponse> items) {
        this.items = items;
    }
}
