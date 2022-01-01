package com.revature.Project2;

import com.revature.Project2.util.Email;
import com.revature.Project2.util.PropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Project2Application {

	public static void main(String[] args) {
		//PropertyConfig.updateProperties();
		SpringApplication.run(Project2Application.class, args);


		//Email.sendEmail("richard.mace@revature.net", "Test from Project 2", "This is a test from Project 2.  Have a good day and a happy New Years.");
	}

}
