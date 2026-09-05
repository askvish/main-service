package com.google.main.to;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record User(
        Integer userId,
        @JsonAlias("userName") String name,
        @JsonIgnore String email) {
}