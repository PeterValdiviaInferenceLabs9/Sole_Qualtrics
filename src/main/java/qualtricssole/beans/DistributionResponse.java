/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class DistributionResponse {

    private boolean result;
    private String distribution_id;

    public DistributionResponse() {
    }

    public DistributionResponse(boolean result, String distribution_id) {
        this.result = result;
        this.distribution_id = distribution_id;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getDistribution_id() {
        return distribution_id;
    }

    public void setDistribution_id(String distribution_id) {
        this.distribution_id = distribution_id;
    }

}
