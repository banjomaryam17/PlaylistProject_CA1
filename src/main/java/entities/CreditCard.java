package entities;

import lombok.*;
import org.jetbrains.annotations.*;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor


    public class CreditCard {
        private int username;
        private String token;      // tokenized card reference or encrypted PAN (recommended)
        private String last4;
        private String brand;
        private int expMonth;
        private int expYear

     public boolean setCCNumber(double number) {
         boolean success = false;
         if (number < 9999999 || number > 1000000000) {
             throw new IllegalArgumentException("Not Enough or Too Many CreditCard Numbers");{}
         }
         else{
             this.ccNumber = number;
             success = true;
         }
         return success;
     }
     public boolean setccExpMonth(int month) {
         boolean success = false;
         if (month < 1 || month > 12){
             throw new IllegalArgumentException("invaild CreditCard Month");
         }
         else{
             this.ccExpMonth = month;
         }
     }

}
