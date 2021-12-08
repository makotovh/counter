package com.makotovh.counter;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.r2dbc.core.DatabaseClient;

@SpringBootApplication
public class CounterApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CounterApiApplication.class, args);
		ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///counterdb?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");

		var client = DatabaseClient.create(connectionFactory);

		client.sql("CREATE TABLE counter" +
						"(id bigint auto_increment primary key," +
						"name VARCHAR(255)," +
						"quantity bigint)")
				.fetch()
				.all()
				.collectList()
				.block();
	}

}
