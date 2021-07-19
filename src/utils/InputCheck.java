package utils;

import java.io.Closeable;
import java.util.Scanner;

/*
The tool is designed to prompt User to type characters to console as long as the typed symbols don't match
int or long number.
 */

public class InputCheck implements Closeable {
    Scanner userInput = new Scanner(System.in);
    private String errorText;

    public String getError(){
        return errorText;
    }

    public InputCheck(){
        this.errorText = "Entered characters don't match the required number type, try again:";
    }

    public InputCheck (String error){
        this.errorText = error;
    }

    /**
     * Allows to type characters to console, checks if these characters form an int number. If not - shows the
     * determined or default message and allows to enter characters again, till characters won't form an int number.
     * @return successfully entered int number
     */
    public int checkIntInput(){
        int numberFromInput;
        while (true){
            if (userInput.hasNextInt()){
                numberFromInput = userInput.nextInt();
                break;
            }else{
                System.out.println(this.errorText);
                userInput.next();
            }
        }
        return numberFromInput;
    }

    /**
     * Allows to type characters to console, checks if these characters form a long number. If not - shows the
     * determined or default message and allows to enter characters again, till characters won't form a long number.
     * @return successfully entered long number
     */
    public long checkLongInput(){
        long numberFromInput;
        while (true){
            if (userInput.hasNextLong()){
                numberFromInput = userInput.nextLong();
                break;
            }else{
                System.out.println(errorText);
                userInput.next();
            }
        }
        return numberFromInput;
    }

    public void close(){
        userInput.close();
    }
}