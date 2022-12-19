package org.pih;

import org.pih.database.EmployeeDatabase;
import org.pih.dto.Employee;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SupplyAsyncExample {

    public List<Employee> getEmployees() throws ExecutionException, InterruptedException {
        CompletableFuture<List<Employee>> completableFuture = CompletableFuture.supplyAsync(() ->
                {
                    System.out.println("Executed by : "+ Thread.currentThread().getName());
                    return EmployeeDatabase.fetchEmployees();
                }
        );
        return completableFuture.get();
    }

//    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        SupplyAsyncExample demo = new SupplyAsyncExample();
//        List<Employee> employees = demo.getEmployees();
//        employees.stream().forEach(System.out::println);
//    }
}
