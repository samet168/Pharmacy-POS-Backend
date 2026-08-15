package com.pharmacy.pos.setup;

import com.pharmacy.pos.setup.service.SetupService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FixPermissionsRunner implements CommandLineRunner {

    private final SetupService setupService;

    public FixPermissionsRunner(SetupService setupService) {
        this.setupService = setupService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if we should fix permissions on startup
        if (args.length > 0 && args[0].equals("fix-permissions")) {
            System.out.println("Running fix-permissions...");
            String result = setupService.fixPermissions();
            System.out.println("Result: " + result);
            System.exit(0);
        }
    }
}