package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class EnvController {
    private String port;
    private String memoryLimit;
    private String cfInstanceIndex;
    private String cfInstanceADDR;

    public EnvController(@Value("${PORT:Not Set}") String port,
                         @Value("${MEMORY_LIMIT:Not Set}") String memoryLimit,
                         @Value("${CF_INSTANCE_INDEX:Not Set}") String cfInstanceIndex,
                         @Value("${CF_INSTANCE_ADDR:Not Set}") String cfInstanceADDR){
        this.port=port;
        this.memoryLimit=memoryLimit;
        this.cfInstanceIndex = cfInstanceIndex;
        this.cfInstanceADDR = cfInstanceADDR;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv(){
        Map<String, String> env = new HashMap<>();

        env.put("PORT", port);
        env.put("MEMORY_LIMIT", memoryLimit);
        env.put("CF_INSTANCE_INDEX", cfInstanceIndex);
        env.put("CF_INSTANCE_ADDR", cfInstanceADDR);

    return env;
    }
}
