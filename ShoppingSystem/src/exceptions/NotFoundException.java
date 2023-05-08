package exceptions;

public class NotFoundException extends Throwable {
    public NotFoundException() {
        System.out.println("Product Not Found from Exception");
    }
}
