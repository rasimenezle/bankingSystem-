package com.rasimenezle.bankingSystemProject.dtoResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDontResponseDto {

	private boolean success;
	private String message;
}
