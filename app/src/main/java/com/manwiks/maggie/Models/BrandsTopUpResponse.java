package com.manwiks.maggie.Models;

import java.util.ArrayList;
import java.util.List;

public class BrandsTopUpResponse {
    private TopUpsModel topUpsModel;
    private List<BrandsModel> brandsModelList;

    public BrandsTopUpResponse(TopUpsModel topUpsModel, List<BrandsModel> brandsModelList) {
        this.topUpsModel = topUpsModel;
        this.brandsModelList = brandsModelList != null ? brandsModelList : new ArrayList<>();
    }

    public TopUpsModel getTopUpsModel() {
        return topUpsModel;
    }

    public List<BrandsModel> getBrandsModelList() {
        return brandsModelList;
    }
}
