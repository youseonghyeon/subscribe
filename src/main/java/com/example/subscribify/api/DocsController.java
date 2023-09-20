package com.example.subscribify.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocsController {

    @GetMapping("docs/api")
    public String apiDocs() {
        return "subscription/api-docs";
    }

}
