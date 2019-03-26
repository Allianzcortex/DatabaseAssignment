package com.mcda.database.project.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class ReturnTables {
    private List<String> fields;
    private List<Map<String, Object>> values;
}
