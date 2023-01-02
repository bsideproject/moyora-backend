package com.beside.ties.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseVo<T> {
    private T body;
}
