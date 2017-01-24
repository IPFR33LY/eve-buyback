package de.bahr.eve.entities;


import com.tlabs.eve.api.Contract;

import java.util.List;

/**
 * Created by michaelbahr on 24/01/2017.
 */
public class ExtendedContract {

    private Contract contract;
    private String link;
    private double jitaSell;
    private double jitaBuy;
    private double buybackBuy;
    private String client;
    private ApiEntry apiEntry;
    private List<QuantifiedItem> quantifiedItems;
    private String corpId;
    private long contractId;

    public ExtendedContract() {
    }

    public ExtendedContract(ApiEntry apiEntry) {
        this.apiEntry = apiEntry;
    }

    public ExtendedContract(Contract contract, ApiEntry apiEntry) {
        this.contract = contract;
        this.apiEntry = apiEntry;
    }

    public ApiEntry getApiEntry() {
        return apiEntry;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setJitaSell(double jitaSell) {
        this.jitaSell = jitaSell;
    }

    public double getJitaSell() {
        return jitaSell;
    }

    public void setJitaBuy(double jitaBuy) {
        this.jitaBuy = jitaBuy;
    }

    public double getJitaBuy() {
        return jitaBuy;
    }

    public void setBuybackBuy(double buybackBuy) {
        this.buybackBuy = buybackBuy;
    }

    public double getBuybackBuy() {
        return buybackBuy;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public List<QuantifiedItem> getQuantifiedItems() {
        return quantifiedItems;
    }

    public void setQuantifiedItems(List<QuantifiedItem> quantifiedItems) {
        this.quantifiedItems = quantifiedItems;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    public long getContractId() {
        return contractId;
    }
}
