package exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        System.out.println("Product Not Found from Exception");
    }
}
