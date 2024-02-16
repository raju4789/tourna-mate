package com.tournament.tournimanager.dto;

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
