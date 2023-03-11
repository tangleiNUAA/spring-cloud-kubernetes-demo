package org.vidi.spring.kubernetes;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudKubernetesDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudKubernetesDemoApplication.class, args);
    }

}

@RequestMapping("/")
@RestController
class TestController {

    @Autowired
    private DiscoveryClient client;

    @Autowired
    private Config config;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/services")
    @ResponseBody
    public List<String> getName() {
        return client.getServices();
    }

    @GetMapping("/current")
    @ResponseBody
    public List<ServiceInstance> getCurrentService() {
        return client.getInstances(applicationContext.getId());
    }

    @GetMapping("/getConfig")
    public String getConfig() {
        return this.config.getDemo();
    }

}

@Data
@Component
@ConfigurationProperties(prefix = "config")
class Config {
    private String demo;
}

