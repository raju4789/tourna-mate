package com.tournament.gateway.dto.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
        private int errorCode;
        private String errorMessage;
}
