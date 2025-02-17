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

    // JSON 변환 에러
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "JSON 데이터 변환 중 에러 발생."),

    // 카테고리 검증
    INVALID_MAIN_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 대분류입니다."),
    INVALID_SUB_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 소분류입니다."),

    // Google Geocoding
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 위도와 경도에 대한 주소를 찾을 수 없습니다."),
    GEOCODING_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Google Geocoding API 호출 중 오류가 발생했습니다."),

    // Address Parse
    INVALID_ADDRESS_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "잘못된 주소 형식입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}