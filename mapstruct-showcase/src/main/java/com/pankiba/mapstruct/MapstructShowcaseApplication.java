package com.pankiba.mapstruct;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pankiba.mapstruct.domain.BusinessUnit;
import com.pankiba.mapstruct.domain.Employee;
import com.pankiba.mapstruct.domain.Grade;
import com.pankiba.mapstruct.service.BusinessUnitService;
import com.pankiba.utils.displaytable.DisplayTableUtil;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class MapstructShowcaseApplication {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";
	public static final String DEVELOPMENT_PROFILE = "dev";

	public static void main(String[] args) {

		SpringApplication springApplication = new SpringApplication(MapstructShowcaseApplication.class);

		/*
		 * setting default profile as DEV if no other profile is configured. This needs
		 * to be done before calling run method on SpringApplication
		 */

		Map<String, Object> defaultProperties = new HashMap<>();
		defaultProperties.put(SPRING_PROFILE_DEFAULT, DEVELOPMENT_PROFILE);
		springApplication.setDefaultProperties(defaultProperties);

		log.info("Type of web application : " + springApplication.getWebApplicationType());
		Environment environment = springApplication.run(args).getEnvironment();
		logApplicationStartup(environment);

	}
	
	@Bean
	public CommandLineRunner loadTestData(BusinessUnitService businessUnitService) {

		return (String... args) -> {

			log.info(" Running Data Loader ");

			BusinessUnit businessUnit1 = new BusinessUnit("ADM");

			businessUnit1.addEmployee(new Employee("John", "McLane", "M", "john.rambo@users.noreply.github.com",
					parseDate("1970-07-30", "yyyy-MM-dd"), parseDate("2008-07-26", "yyyy-MM-dd"), Grade.Developer,
					20000L));

			businessUnit1.addEmployee(new Employee("Ethan", "Hunt", "M", "ethan.hunt@users.noreply.github.com",
					parseDate("1982-09-26", "yyyy-MM-dd"), parseDate("2005-07-21", "yyyy-MM-dd"), Grade.Lead, 30000L));

			businessUnitService.createBusinessUnit(businessUnit1);

			BusinessUnit businessUnit2 = new BusinessUnit("TSL");

			businessUnit2.addEmployee(new Employee("Jery", "Maguire", "M", "jery.maguire@@users.noreply.github.com\"",
					parseDate("1959-06-08", "yyyy-MM-dd"), parseDate("1994-04-26", "yyyy-MM-dd"), Grade.Lead, 30000L));

			businessUnitService.createBusinessUnit(businessUnit2);
			
			DisplayTableUtil.printTable(jdbcTemplate, "BUSINESS_UNIT", "EMPLOYEE");

		};

	}

	public static void logApplicationStartup(Environment environment) {

		String protocol = "http";

		if (environment.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}

		String serverPort = environment.getProperty("server.port");
		String contextPath = environment.getProperty("server.servlet.context-path");

		if (StringUtils.isBlank(contextPath)) {
			contextPath = "/";
		}

		String hostAddress = "localhost";

		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException unknownHostException) {
			log.warn("The host name could not be determined, using 'localhost' as fallback");
		}

		String[] profiles = environment.getActiveProfiles();

		if (profiles.length == 0) {
			log.info("No active profile set, falling back to default profiles: {} ",
					Arrays.toString(environment.getDefaultProfiles()));
			profiles = environment.getDefaultProfiles();
		}

		log.info("Spring Framework Version : {}, Spring Boot Version : {}", SpringVersion.getVersion(),
				SpringBootVersion.getVersion());
		log.info("\n------------------------------------------------------------------------------\n\t"
				+ "Application '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}{}\n\t"
				+ "External: \t{}://{}:{}{}\n\t"
				+ "Profile(s): \t{}\n------------------------------------------------------------------------------",
				environment.getProperty("spring.application.name"), protocol, serverPort, contextPath, protocol,
				hostAddress, serverPort, contextPath, profiles);

	}

}
