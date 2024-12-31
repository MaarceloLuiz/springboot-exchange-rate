package com.marceloluiz.exchangerate.config;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class FileProperties {
    private final String readmePath = "README.md";
}
