package com.tournament.tournimanager.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse<T> {

    private boolean isSuccess;

    private T data;

    private ErrorDetails errorDetails;

    public CommonApiResponse(T data) {
        this.isSuccess = true;
        this.data = data;
        this.errorDetails = null;
    }

    public CommonApiResponse(ErrorDetails errorDetails) {
        this.isSuccess = false;
        this.data = null;
        this.errorDetails = errorDetails;
    }

}
