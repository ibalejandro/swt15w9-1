package app.repository;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;

import app.model.User;



public interface UserRepository extends CrudRepository<User, Long>{
	User findByUserAccount(UserAccount userAccount);
}
