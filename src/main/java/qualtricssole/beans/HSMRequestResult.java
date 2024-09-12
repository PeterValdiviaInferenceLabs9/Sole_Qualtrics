/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qualtricssole.beans;

/**
 *
 * @author Luis D
 */
public class HSMRequestResult {

    private boolean result;
    private int countersucess;
    private int counterfails;

    public HSMRequestResult() {
    }

    public HSMRequestResult(boolean result, int countersucess, int counterfails) {
        this.result = result;
        this.countersucess = countersucess;
        this.counterfails = counterfails;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCountersucess() {
        return countersucess;
    }

    public void setCountersucess(int countersucess) {
        this.countersucess = countersucess;
    }

    public int getCounterfails() {
        return counterfails;
    }

    public void setCounterfails(int counterfails) {
        this.counterfails = counterfails;
    }
}
