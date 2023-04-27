package com.model2.mvc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.UserService;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.user.UserDao;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{
    
    @Autowired
    @Qualifier("userDaoImpl")
    private UserDao userDao;
    
    public void addUser(User user) throws Exception {
        userDao.addUser(user);
    }

    public User getUser(String userId) throws Exception {
        return userDao.getUser(userId);
    }

    public List<Map<String, Object>> getUserList(Search search) throws Exception {
        List<User> userList = userDao.getUserList(search);
        int totalCount = userDao.getTotalCount(search);

        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (User user : userList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", user);
            resultList.add(map);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("list", resultList);
        resultMap.put("totalCount", new Integer(totalCount));

        return resultList;
    }

    public void updateUser(User user) throws Exception {
        userDao.updateUser(user);
    }

    public boolean checkDuplication(String userId) throws Exception {
        boolean result = true;
        User user = userDao.getUser(userId);
        if(user != null) {
            result = false;
        }
        return result;
    }
    
    @Override
    public List<Map<String, Object>> autocomplete(String keyword) throws Exception {
        return userDao.autocomplete(keyword);
    }
}