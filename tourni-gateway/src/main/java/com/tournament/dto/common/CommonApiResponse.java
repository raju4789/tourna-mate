package com.tournament.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
