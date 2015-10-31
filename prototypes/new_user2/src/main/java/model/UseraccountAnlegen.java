import javax.management.relation.Role;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.stereotype.Component;

/* Skip creation if database was already populated
		if (userAccountManager.get(new UserAccountIdentifier("boss")).isPresent()) {
			return;
		}  */

@Component
public class UseraccountAnlegen {
	private final UserAccountManager userAccountManager;
	
	private void NeuenUseraccountAnlegen(){
	UserAccount bossAccount = userAccountManager.create("boss", "123", Role("ROLE_BOSS", null));
	userAccountManager.save(bossAccount);

	final Role customerRole = new Role("ROLE_CUSTOMER", null);

	UserAccount ua1 = userAccountManager.create("hans", "123", customerRole);
	userAccountManager.save(ua1);
	UserAccount ua2 = userAccountManager.create("dextermorgan", "123", customerRole);
	userAccountManager.save(ua2);
	UserAccount ua3 = userAccountManager.create("earlhickey", "123", customerRole);
	userAccountManager.save(ua3);
	UserAccount ua4 = userAccountManager.create("mclovinfogell", "123", customerRole);
	userAccountManager.save(ua4);

	Customer c1 = new Customer(ua1, "wurst");
	Customer c2 = new Customer(ua2, "Miami-Dade County");
	Customer c3 = new Customer(ua3, "Camden County - Motel");
	Customer c4 = new Customer(ua4, "Los Angeles");
	}

	private org.salespointframework.useraccount.Role Role(String string, Object object) {
		// TODO Auto-generated method stub
		return null;
	}
}
