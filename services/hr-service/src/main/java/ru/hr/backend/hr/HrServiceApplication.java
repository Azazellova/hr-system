package ru.hr.backend.hr;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication(scanBasePackages = {"ru.hr.backend.hr", "ru.hr.backend.accounting", "ru.hr.backend.clients", "ru.hr.backend.common.exception", "ru.hr.backend.config"})
public class HrServiceApplication {
    public static void main(String[] args) { SpringApplication.run(HrServiceApplication.class, args); }
}
