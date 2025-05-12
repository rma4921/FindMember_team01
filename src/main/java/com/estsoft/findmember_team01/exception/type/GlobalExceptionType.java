package com.estsoft.findmember_team01.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalExceptionType {
    MEMBER_NOT_FOUND("존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    EMAIL_NOT_FOUND("존재하지 않는 이메일입니다.", HttpStatus.NOT_FOUND),
    LOGIN_REQUIRED("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    APPLICATION_NOT_FOUND("해당 지원서를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    RECRUITMENT_NOT_FOUND("존재하지 않는 모집글입니다.", HttpStatus.NOT_FOUND),
    COMMENT_NOT_FOUND("해당 댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INFORMATION_NOT_FOUND("질의 응답을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    UNAUTHORIZED_ACTION("수정 권한이 없습니다.", HttpStatus.FORBIDDEN),
    AUTHOR_NOT_EXIST("해당 작성자 정보가 없습니다.", HttpStatus.BAD_REQUEST),
    REPORT_NOT_FOUND("해당 신고 정보가 없습니다.", HttpStatus.NOT_FOUND),
    ONLY_AUTHOR_CAN_DELETE("작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN),
    ONLY_AUTHOR_CAN_UPDATE("작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN),
    UNSUPPORTED_REPORT_TYPE("지원하지 않는 신고 타입입니다.", HttpStatus.BAD_REQUEST),
    FORBIDDEN_COMMENT("MASTER 이상 등급만 댓글을 작성할 수 있습니다.", HttpStatus.FORBIDDEN),
    ;

    private final String message;
    private final HttpStatus status;

}
