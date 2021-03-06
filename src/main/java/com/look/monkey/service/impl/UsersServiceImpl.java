/*
 * Copyright 2011-2014 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.look.monkey.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.look.monkey.entity.QUser;
import com.look.monkey.entity.User;
import com.look.monkey.repository.UserRepository;
import com.look.monkey.repository.Abstract.AbstractRepository;
import com.look.monkey.service.UsersService;

@Service
@Transactional
public class UsersServiceImpl extends AbstractRepository<User> implements UsersService {

    @Autowired
    private UserRepository userRepository;
    
    final QUser qUser = QUser.user;

    @Autowired
    public PasswordEncoder passwordEncoder;


    @Override
    public User getUserByUserName(final String username) {
        return this.selectFrom(qUser).where(qUser.username.contains(username)).fetchOne();
    }

    @Override
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @Override
    public User save(final User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return this.userRepository.findOneByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("用户对应的用户名：%s 不存在！", username)));
    }

}
