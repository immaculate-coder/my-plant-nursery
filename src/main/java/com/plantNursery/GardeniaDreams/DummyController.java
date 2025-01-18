package com.plantNursery.GardeniaDreams;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DummyController {

    @GetMapping("/health-check")
    public String sayHello() {
        return "Hello from Gradenia Dreams";
    }
}
