package org.pih;

import org.pih.database.EmployeeDatabase;
import org.pih.dto.Employee;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class EmployeeReminderService {

    public CompletableFuture<Void> sendReminderToEmployee(){
        Executor executor = Executors.newFixedThreadPool(5);

        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("fetchEmployee : "+ Thread.currentThread().getName());
            return EmployeeDatabase.fetchEmployees();
        }, executor)
        .thenApplyAsync((employees) -> {
            System.out.println("filter new joiner employee  "+ Thread.currentThread().getName());
            return employees.stream().
                    filter( employee -> "TRUE".equals(employee.getNewJoiner()))
                    .collect(Collectors.toList());
        }, executor)
                .thenApplyAsync((employees) -> {
                    System.out.println("get emails : "+ Thread.currentThread().getName());
                    return employees.stream().map(Employee::getEmail).collect(Collectors.toList());
        },executor ).thenAcceptAsync((emails) ->{
                    System.out.println("send email : " + Thread.currentThread().getName());
                    emails.forEach(EmployeeReminderService::sendEmail);
                });
        return voidCompletableFuture;
    }


    public static void sendEmail(String email){
        System.out.println("sending training email to : "+ email);
    }

//    public static void main(String[] args) {
//        EmployeeReminderService employeeReminderService = new EmployeeReminderService();
//        employeeReminderService.sendReminderToEmployee();
//    }
}
