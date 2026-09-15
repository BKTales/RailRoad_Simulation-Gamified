package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

public class BudgetTes {

    @Test
    public void getFundsTest(){
        Budget budget = new Budget(1000);
        double actual = budget.getFunds();
        double expected = 1000;

        assert actual == expected;
    }

    @Test
    public void hasEnoughFundsTest(){
        double value = 2000;
        Budget budget = new Budget(1000);
        boolean actual = budget.hasEnoughFunds(value);
        boolean expected = false;
        assert actual == expected;
    }

    @Test
    public void subtractFundsTest(){
        double value = 500;
        Budget budget = new Budget(1000);
        budget.subtractFunds(value);
        double expected = 500;
        double actual = budget.getFunds();
        assert actual == expected;
    }
}
