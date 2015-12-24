package com.example.a15lorenaae.final_a15lorenaae;

/**
 * Created by a15lorenaae on 12/14/15.
 */
public class Salario {
    String month;
    double total_salary;

    public Salario() {
    }

    public Salario(String month, double total_salary) {
        this.month = month;
        this.total_salary = total_salary;

    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getTotal_salary() {
        return total_salary;
    }

    public void setTotal_salary(double total_salary) {
        this.total_salary = total_salary;
    }

    @Override
    public String toString() {
        return "Salario{" +
                "month='" + month + '\'' +
                ", total_salary=" + total_salary +
                '}';
    }


}
