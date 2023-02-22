package pro.sky.budgetapp.service;

import pro.sky.budgetapp.model.Transaction;

public interface BudgetService {
    int getDailyBudget(); //бюджет на день

    int getBalance();

    long addTransaction(Transaction transaction);

    Transaction getTransaction(long id);

    int getDailyBalance();

    int getAllSpend();

    int getVacationBonus(int daysCount);

    int getSalaryWithVacation(int vacationDaysCount, int vacationsWorkingDaysCount, int workingDaysInMonth);
}
