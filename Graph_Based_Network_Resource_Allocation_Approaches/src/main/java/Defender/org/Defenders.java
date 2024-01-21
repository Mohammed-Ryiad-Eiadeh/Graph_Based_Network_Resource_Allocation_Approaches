package Defender.org;

/**
 * This class is used to construct defenders to defent about the assets node and to prevent attackers
 */
public class Defenders {
    private double invest_D;
    public static double spareBudget_D;

    /**
     * This constructor is used to initialize the initial budget for each defender
     * @param invest_d1 Referred to first defender
     */
    public Defenders(double invest_d) {
        if (invest_d < 0) {
            throw new IllegalArgumentException("The initial investments must be non-negative integer");
        }
        this.invest_D = invest_d;
    }

    /**
     * This method is used to retrieve the budget of first defender
     * @return The budget of first defender
     */
    public double getInvest_D() {
        return invest_D;
    }

    /**
     * This method is used to add more budget to the current one of first defender
     * @param value The budget to add
     * @return The budget if there is as existed amount and not exceeds the total capacity of spare budget
     */
    public double addSpareInvestFor_D(double value) {
        if (spareBudget_D - value < 0)
            return spareBudget_D + this.invest_D;
        else
        {
            spareBudget_D -= value;
            return value + this.invest_D;
        }
    }

    /**
     * This method is used to set the budget for first defender
     * @param invest_D1 The budget amount to be set
     */
    public void setInvest_D(double invest_D1) {
        this.invest_D = invest_D1;
    }

    /**
     * This method is used to retrieve the total budget for the defender
     * @return The budget of the defender
     */
    public double totalInvest() {
        return this.getInvest_D();
    }
}