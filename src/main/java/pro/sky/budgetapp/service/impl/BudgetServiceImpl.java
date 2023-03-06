package pro.sky.budgetapp.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.budgetapp.model.Transaction;
import pro.sky.budgetapp.service.BudgetService;
import pro.sky.budgetapp.service.FilesService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final FilesService filesService;

    public static final int SALARY = 20000;
    public static final int SAVING = 3000;
    public static final int DAILY_BUDGET = (SALARY - SAVING) / LocalDate.now().lengthOfMonth(); //бюджет на день
    public static final int AVG_SALARY = (10000 + 10000 + 10000 + 10000 + 10000 + 15000 + 15000 + 15000 + 15000 + 15000 + 15000 + 20000) / 12;
    public static final double AVG_DAYS = 29.3;
    private static TreeMap<Month, LinkedHashMap<Long, Transaction>> transactions = new TreeMap<>();
    public static int balance = 0;
    private static long lastId = 0;

    public BudgetServiceImpl(FilesService filesService) {
        this.filesService = filesService;
    }

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
        LinkedHashMap<Long, Transaction> monthTransactions = transactions.getOrDefault(LocalDate.now().getMonth(), new LinkedHashMap<>());
        monthTransactions.put(lastId, transaction);
        transactions.put(LocalDate.now().getMonth(), monthTransactions);
        saveToFile();
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
    public Transaction editTransaction(long id, Transaction transaction) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id)) {
                transactionByMonth.put(id, transaction);
                saveToFile();
            }
        }
        return transaction;
    }

    @Override
    public boolean deleteTransaction(long id) {
        for (Map<Long, Transaction> transactionByMonth : transactions.values()) {
            if (transactionByMonth.containsKey(id)) {
                transactionByMonth.remove(id);
                return true;
            }
        }
        return false;
    }

    @Override
    public void deleteAllTransactions() {
        transactions = new TreeMap<>();
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
            sum =sum + transaction.getSum();
        }
        return sum;
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

    private void saveToFile() {
        try {

            String json = new ObjectMapper().writeValueAsString(transactions);
            filesService.saveToFile(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        String json = filesService.readFromFile();
        try {
           transactions = new ObjectMapper().readValue(json, new TypeReference<TreeMap<Month, LinkedHashMap<Long, Transaction>>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
@PostConstruct
    private void init() {
        readFromFile();
    }
}
