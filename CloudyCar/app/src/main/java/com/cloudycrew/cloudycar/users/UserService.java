package com.cloudycrew.cloudycar.users;

import com.cloudycrew.cloudycar.elasticsearch.IElasticSearchService;
import com.cloudycrew.cloudycar.models.User;
import java.util.List;

/**
 * Created by Ryan on 2016-11-08.
 */

public class UserService implements IUserService
{
    IElasticSearchService<User> elasticSearchService;

    public UserService(IElasticSearchService<User> elasticSearchService) {
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
        List<User> userlist = elasticSearchService.search(user.getUsername());
        if(!user.verifyContactInformation()) {
            throw new IncompleteUserException();
        }
        if(userlist.isEmpty()) {
            elasticSearchService.create(user);
        }
        else {
            throw new DuplicateUserException();
        }
    }

    @Override
    public User getCurrentUser() {
        //Lol
        return null;
    }

    @Override
    public void updateUser(User user) {
        elasticSearchService.update(user);
    }
}
