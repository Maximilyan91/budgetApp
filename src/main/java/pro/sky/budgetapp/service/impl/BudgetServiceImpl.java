package pro.sky.budgetapp.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.budgetapp.service.BudgetService;

import java.time.LocalDate;
@Service
public class BudgetServiceImpl implements BudgetService {

    public static final int SALARY = 20000;
    public static final int AVG_SALARY = (10000 + 10000 + 10000 + 10000 + 10000 + 15000 + 15000 + 15000 + 15000 + 15000 + 15000 + 20000) / 12;
    public static final double AVG_DAYS = 29.3;

    @Override
    public int getDailyBudget() {
        return SALARY / 30;
    }

    @Override
    public int getBalance() {
        return SALARY - LocalDate.now().getDayOfMonth() * getDailyBudget();
    }

    @Override
    public int getVacationBonus(int daysCount) {
        double avgDaySalary = AVG_SALARY / AVG_DAYS;
        return (int) (daysCount * avgDaySalary);
    }
    @Override
    public int getSalaryWithVacation(int vacationDaysCount, int vacationsWorkingDaysCount, int workingDaysInMonth) {
        getVacationBonus(vacationDaysCount);
        int salary = SALARY / workingDaysInMonth * (workingDaysInMonth - vacationsWorkingDaysCount);
        return salary + getVacationBonus(vacationDaysCount);
    }
}
