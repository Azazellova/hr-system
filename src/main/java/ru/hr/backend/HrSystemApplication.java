package ru.hr.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class HrSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HrSystemApplication.class, args);
	}
	@Bean
	public CommandLineRunner checkDatabase(DataSource dataSource) {
		return args -> {
			try (Connection connection = dataSource.getConnection()) {
				DatabaseMetaData metaData = connection.getMetaData();

				System.out.println("========== DATABASE INFO ==========");
				System.out.println("URL: " + metaData.getURL());
				System.out.println("Database: " + metaData.getDatabaseProductName());
				System.out.println("Version: " + metaData.getDatabaseProductVersion());

				try (Statement statement = connection.createStatement();
					 ResultSet resultSet = statement.executeQuery("SELECT inet_server_addr(), inet_server_port(), version()")) {

					if (resultSet.next()) {
						System.out.println("Server address: " + resultSet.getString(1));
						System.out.println("Server port: " + resultSet.getInt(2));
						System.out.println("Server version: " + resultSet.getString(3));
					}
				}

				System.out.println("===================================");
			}
		};
	}
}
