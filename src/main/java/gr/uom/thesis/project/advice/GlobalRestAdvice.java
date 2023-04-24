package gr.uom.thesis.project.advice;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalRestAdvice {


    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail notFoundHandler(ItemNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()),
                "We couldn't find what you were looking for");
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail genericExceptionHandler(Exception e) {
        e.printStackTrace();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
                "An unspecified error has occurred.");
        problemDetail.setProperty("exceptionClass", e.getClass());
        problemDetail.setProperty("exceptionMsg", e.getLocalizedMessage());
        return problemDetail;

    }

    @ExceptionHandler(ItemAlreadyAnalyzedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ProblemDetail itemAlreadyAnalyzedHandler(ItemAlreadyAnalyzedException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                e.getProjectName() + " has already been analyzed");
    }
}
