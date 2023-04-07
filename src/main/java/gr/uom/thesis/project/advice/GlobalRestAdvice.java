package gr.uom.thesis.project.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalRestAdvice {



    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFoundHandler(ItemNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()),
                "We couldn't find what you were looking for");
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail genericExceptionHandler() {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
                "An unspecified error has occurred.");
    }
}
