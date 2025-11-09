package entities;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCard {

    @EqualsAndHashCode.Include
    private String username;   // user ID or foreign key reference
    @NotNull
    private String last4;   // last 4 digits of the card
    @NotNull
    private String brand;   // e.g. Visa, MasterCard
    private int expMonth;
    private int expYear;

    /**
     * takes creditcard number and only saves the last 4 using substring
     * @param number valid creditcard number
     * @return boolean based on if the number was set
     * @author ShaunGuiden
     */
    public boolean setNumber(String number) {
        if (number == null || number.length() != 16) {
            throw new IllegalArgumentException("Credit card number must be exactly 16 digits.");
        }
        else{
            this.last4 = number.substring(number.length() - 4);
            return true;
        }
    }

    /**
     * sets the expiry month by only taking in anything between 1 and 12
     * @param month
     * @return true if month was set
     */
    public boolean setExpMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid credit card expiration month.");
        }
        else {
            this.expMonth = month;
            return true;
        }
    }

    /**s
     * ets the expiry month by only taking in anything between 2025 and 2035
     * @param year
     * @return a boolean based of it the year has passed
     */
    public boolean setExpYear(int year) {
        if (year <= 25 || year >= 35) {
            throw new IllegalArgumentException("Invalid credit card expiration year.");
        }
        else{
            this.expYear = year;
            return true;
        }
    }
}
