package pro.sky.budgetapp.service;

public interface BudgetService {
    int getDailyBudget(); //бюджет на день

    int getBalance();

    int getVacationBonus(int daysCount);

    int getSalaryWithVacation(int vacationDaysCount, int vacationsWorkingDaysCount, int workingDaysInMonth);
}
