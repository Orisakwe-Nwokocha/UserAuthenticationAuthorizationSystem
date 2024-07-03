package com.prunny.jwt_project.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.modelmapper.Conditions.isNotNull;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Component
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(STRICT)
                .setPropertyCondition(isNotNull());
        return mapper;
    }
}
