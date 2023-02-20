package pro.sky.budgetapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.budgetapp.service.BudgetService;

@RestController
@RequestMapping("/budget")
public class BudgetController{

    private BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/daily")
    public int dailyBudget() {
        return budgetService.getDailyBudget();
    }

    @GetMapping("/balance")
    public int balance() {
        return budgetService.getBalance();
    }

    @GetMapping("/vacation")
    public int vacationBonus(@RequestParam int vacationDays) {
        return budgetService.getVacationBonus(vacationDays);
    }

    @GetMapping("/vacation/salary")
    public int SalaryWithVacation(@RequestParam int vacationDays, @RequestParam int workingDays, @RequestParam int vacWorkDays) {
        return budgetService.getSalaryWithVacation(vacationDays, vacWorkDays, workingDays);
    }

}
