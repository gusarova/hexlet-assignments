package exercise.daytime;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


public class Night implements Daytime {
    private String name = "night";

    public String getName() {
        return name;
    }
    // BEGIN
    @PostConstruct
    public void init(){
        System.out.println("Night bean created");
    }

    @PreDestroy
    public void cleanup(){
        System.out.println("Night bean before destroy");
    }
    // END
}
