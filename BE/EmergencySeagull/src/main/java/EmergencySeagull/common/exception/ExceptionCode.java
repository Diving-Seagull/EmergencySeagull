package EmergencySeagull.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    // 신고 관련 에러
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 신고를 찾을 수 없습니다."),

    // GPT 에러
    GPT_CLASSIFICATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "gpt 분류 중 에러 발생."),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}