package userManagement.model;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;



public interface UserRepository extends CrudRepository<User, Long>{
	User findByUserAccount(UserAccount userAccount);
}
