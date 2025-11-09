package persistence;

import entities.CreditCard;
import java.util.List;
import java.util.Optional;

/**
 * @author ShaunGuiden
 */
public interface CreditCardDao {
    void addCreditCard(CreditCard card);
    Optional<CreditCard> getCreditCardByUsername(String username);
    List<CreditCard> getAllCreditCards();
    void deleteCreditCard(String username);
    boolean updateCreditCard(CreditCard card);
}
