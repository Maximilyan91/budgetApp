package pro.sky.budgetapp.service.impl;

import org.springframework.stereotype.Service;
import pro.sky.budgetapp.model.Transaction;
import pro.sky.budgetapp.service.BudgetService;

import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BudgetServiceImpl implements BudgetService {

    public static final int SALARY = 20000;
    public static final int SAVING = 3000;
    public static final int DAILY_BUDGET = (SALARY - SAVING) / LocalDate.now().lengthOfMonth(); //бюджет на день
    public static int balance = 0;
    public static final int AVG_SALARY = (10000 + 10000 + 10000 + 10000 + 10000 + 15000 + 15000 + 15000 + 15000 + 15000 + 15000 + 20000) / 12;
    public static final double AVG_DAYS = 29.3;
    private static long lastId = 0;

    private static Map<Month, Map<Long, Transaction>> transactions = new TreeMap<>();

    @Override
    public int getDailyBudget() {
        return DAILY_BUDGET;
    }

    @Override
    public int getBalance() {   //Оствток средств всего (сколько денег кошельке)
        return SALARY - SAVING - getAllSpend();
    }

    @Override
    public long addTransaction(Transaction transaction) { //Добавление транзакции(когда мы покупаем что-то)
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        monthTransactions.put(lastId, transaction);
        return lastId++;
    }
    @Override
    public Transaction getTransaction(long id) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            Transaction transaction = transactionByMonth.get(id);
            if (transaction != null) {
                return transaction;
            }
        }
        return null;
    }
    @Override
    public int getDailyBalance() { //Остаток средств на текущий день (сколько еще можно сегодня потратить)
        return DAILY_BUDGET * LocalDate.now().getDayOfMonth() - getAllSpend();
    }
    @Override
    public int getAllSpend() { //Получить все траты (подсчет сколько мы уже потратили)
        Map<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());


        int sum = 0;
        for (Transaction transaction : monthTransactions.values()) {
            sum += transaction.getSum();
        } return sum;
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
