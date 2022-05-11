package org.youandi.youandi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class YouAndIApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouAndIApplication.class, args);
    }

}
