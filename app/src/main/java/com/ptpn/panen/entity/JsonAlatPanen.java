package com.ptpn.panen.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonAlatPanen {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<AlatPanen> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<AlatPanen> getData() {
        return data;
    }

    public void setData(List<AlatPanen> data) {
        this.data = data;
    }
}
