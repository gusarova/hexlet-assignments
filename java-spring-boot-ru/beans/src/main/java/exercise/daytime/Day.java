package exercise.daytime;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


public class Day implements Daytime {
    private String name = "day";

    public String getName() {
        return name;
    }

    // BEGIN
    @PostConstruct
    public void init(){
       System.out.println("Day Bean is initialized!");
    }

    @PreDestroy
    public void cleanup(){
        System.out.println("Day Bean before destroyed!");
    }
    // END
}
