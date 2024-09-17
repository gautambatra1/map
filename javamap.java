import static org.junit.jupiter.api.Assertions.*;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.GregorianCalendar;

public class SiphRCCAMapperTest {

    private SiphRCCAMapper mapper = Mappers.getMapper(SiphRCCAMapper.class); // Mapper initialization

    private RetrieveCreditCardAccountResponse retrieveCreditCardAccount;
    private Account account;
    private Payment currentPayment;
    private RepaymentRisk repaymentRisk;
    private Payment nextPayment;
    private Payment lastPayment;
    private Amount principalBalance;
    private Amount minimumDueAmount;
    private Amount totalPastDueAmount;
    private Amount lastStatementBalance;
    private Amount paidAmount;
    private Amount interestSavingBalance;
    private XMLGregorianCalendar nextPaymentDueDate;
    private XMLGregorianCalendar lastPaymentPaidDate;

    @BeforeEach
    public void setUp() throws DatatypeConfigurationException {
        // Initialize the mock objects
        retrieveCreditCardAccount = new RetrieveCreditCardAccountResponse();
        account = new Account();
        currentPayment = new Payment();
        repaymentRisk = new RepaymentRisk();
        nextPayment = new Payment();
        lastPayment = new Payment();
        principalBalance = new Amount();
        minimumDueAmount = new Amount();
        totalPastDueAmount = new Amount();
        lastStatementBalance = new Amount();
        paidAmount = new Amount();
        interestSavingBalance = new Amount();

        // Set values directly
        principalBalance.setAmount(1000.0);
        minimumDueAmount.setAmount(100.0);
        totalPastDueAmount.setAmount(50.0);
        lastStatementBalance.setAmount(1200.0);
        paidAmount.setAmount(500.0);
        interestSavingBalance.setAmount(20.0);

        // Create XMLGregorianCalendar dates
        nextPaymentDueDate = createXMLGregorianCalendar("2023-12-01");
        lastPaymentPaidDate = createXMLGregorianCalendar("2023-10-01");

        // Set values in account
        currentPayment.setMinimumDueAmount(minimumDueAmount);
        currentPayment.setInterestSavingBalance(interestSavingBalance);
        nextPayment.setDueDate(nextPaymentDueDate);
        lastPayment.setPaidAmount(paidAmount);
        lastPayment.setPaidDate(lastPaymentPaidDate);

        repaymentRisk.setTotalPastDueAmount(totalPastDueAmount);

        // Set the account object values
        account.setPrincipalBalance(principalBalance);
        account.setCurrentPayment(currentPayment);
        account.setRepaymentRisk(repaymentRisk);
        account.setNextPayment(nextPayment);
        account.setLastStatementBalance(lastStatementBalance);
        account.setLastPayment(lastPayment);

        // Set the account in the response object
        retrieveCreditCardAccount.setAccount(account);
    }

    @Test
    public void testMapAccountDetails() {
        // Create target object
        ConsumerCardAccountDO consumerCardAccountDO = new ConsumerCardAccountDO();

        // Execute the mapping
        ConsumerCardAccountDO result = mapper.mapAccountDetails(retrieveCreditCardAccount, consumerCardAccountDO);

        // Assertions
        assertNotNull(result);
        assertEquals(1000.0, result.getTotalOutstandingBalance());
        assertEquals(100.0, result.getMinPaymentDue());
        assertEquals(50.0, result.getPastDueAmt());
        assertEquals(nextPaymentDueDate.toString(), result.getPmntDueDate().toString());
        assertEquals(1200.0, result.getStatementBalance());
        assertEquals(500.0, result.getLastPaymentAmount());
        assertEquals(lastPaymentPaidDate.toString(), result.getLastPaymentDate().toString());
        assertEquals(20.0, result.getIntSavingBalance());
        // Add more assertions for other fields as necessary
    }

    // Utility method to create XMLGregorianCalendar from a String date
    private XMLGregorianCalendar createXMLGregorianCalendar(String dateStr) throws DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(java.sql.Date.valueOf(dateStr));
        return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
    }
}
