package sculture.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.CONFLICT, reason = "This user already exists")
public class UserAlreadyExistsException extends RuntimeException {
}