package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.email.Email;
import com.cloudycrew.cloudycar.models.PhoneNumber;
import com.cloudycrew.cloudycar.models.User;
import java.util.List;

/**
 * Created by Ryan on 2016-11-08.
 */

public class UserService implements IUserService
{
    private IElasticSearchService<User> elasticSearchService;
    private UserPreferences userPrefs;

    public UserService(IElasticSearchService<User> elasticSearchService, UserPreferences preferences) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public User getUser(String username) {

        String query = "{\n" +
                       "    \"query\": {\n" +
                       "        \"filtered\" : {\n" +
                       "            \"filter\" : {\n" +
                       "                \"term\" : { \"username\" : \"" + username + "\" }\n" +
                       "            }\n" +
                       "        }\n" +
                       "    }\n" +
                       "}";
        List<User> userlist = elasticSearchService.search(query);
        return userlist.size() > 0 ? userlist.get(0) : null;
    }

    @Override
    public void createUser(User user) throws DuplicateUserException, IncompleteUserException{
        User duplicateUser = this.getUser(user.getUsername());
        if(!user.verifyContactInformation()) {
            throw new IncompleteUserException();
        }
        if(duplicateUser == null) {
            elasticSearchService.create(user);
        } else {
            throw new DuplicateUserException();
        }
    }

    @Override
    public User getCurrentUser() {
        User currentUser = new User(userPrefs.getUserName());
        currentUser.setEmail(new Email(userPrefs.getEmail()));
        currentUser.setPhoneNumber(new PhoneNumber(userPrefs.getPhoneNumber()));
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        userPrefs.saveUser(user);
    }

    @Override
    public void updateUser(User user) {
        elasticSearchService.update(user);
    }
}
