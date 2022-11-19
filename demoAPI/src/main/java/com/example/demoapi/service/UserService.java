package com.example.demoapi.service;


import com.example.demoapi.dto.MessageDTO;
import com.example.demoapi.dto.ResponseDTO;
import com.example.demoapi.dto.user.SearchUserDTO;
import com.example.demoapi.dto.user.UserDTO;
import com.example.demoapi.entities.User;
import com.example.demoapi.repository.UserRepo;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.NoResultException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public interface UserService {

    int create(UserDTO userDTO) throws ParseException;

    void update(UserDTO userDTO) throws ParseException;

    void delete(int id);

    UserDTO getOne(int id);

    ResponseDTO<Void> resetPassword(String email) throws MessagingException;

    ResponseDTO<List<UserDTO>> search(SearchUserDTO searchUserDTO);

    @Transactional
    @Service
    class UserServiceImpl implements UserService {

        @Autowired
        UserRepo userRepo;

        @Autowired
        MailService mailService;

        @Override
        public int create(UserDTO userDTO) throws ParseException {
            User user = new ModelMapper().createTypeMap(UserDTO.class, User.class)
                    .addMappings(map -> map.skip(User::setBirthdate)).map(userDTO);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            user.setBirthdate(sdf.parse(userDTO.getBirthdate()));

            userRepo.save(user);
            return user.getId();
        }

        @Override
        public void update(UserDTO userDTO) throws ParseException {
            User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

            User newUser = new ModelMapper().createTypeMap(UserDTO.class, User.class)
                    .addMappings(map -> map.skip(User::setBirthdate)).map(userDTO);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            newUser.setBirthdate(sdf.parse(userDTO.getBirthdate()));

            if (userDTO.getAvatar() == null) {
                newUser.setAvatar(user.getAvatar());
            }

            userRepo.save(newUser);
        }

        @Override
        public void delete(int id) {
            userRepo.deleteById(id);
        }

        @Override
        public UserDTO getOne(int id) {
            User user = userRepo.findById(id).orElseThrow(NoResultException::new);
            UserDTO userDTO = new ModelMapper().map(user, UserDTO.class);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            userDTO.setBirthdate(sdf.format(user.getBirthdate()));

            return userDTO;
        }

        @Override
        public ResponseDTO<Void> resetPassword(String email) throws MessagingException {
            MessageDTO messageDTO = new MessageDTO();
            User user = userRepo.findByEmail(email);

            ResponseDTO<Void> resp = new ResponseDTO<Void>();
            if (user != null) {
                String newPassword = RandomStringUtils.random(6, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
                user.setPassword(newPassword);
                messageDTO.setContent(newPassword);
                messageDTO.setTo(user.getEmail());
                messageDTO.setToName(user.getName());
                mailService.resetPassword(messageDTO);

                resp.setCode(HttpStatus.OK.value());
                resp.setData(null);
            } else {
                resp.setCode(HttpStatus.OK.value());
                resp.setError("CANT FIND");
            }

            return resp;
        }

        @Override
        public ResponseDTO<List<UserDTO>> search(SearchUserDTO searchUserDTO) {
            Pageable pageable = PageRequest.of(searchUserDTO.getPage(), searchUserDTO.getSize());

            Page<User> pageUser = userRepo.searchByName("%" + searchUserDTO.getKeyword() + "%", pageable);

            ResponseDTO<List<UserDTO>> resp = new ResponseDTO<List<UserDTO>>();

            resp.setCode(HttpStatus.OK.value());
            resp.setTotalPage(pageUser.getTotalPages());

            List<UserDTO> userDTOS = new ArrayList<>();
            for (User user : pageUser.getContent()) {

                UserDTO userDTO = new ModelMapper().map(user, UserDTO.class);
                userDTO.setBirthdate(new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()));

                userDTOS.add(userDTO);
            }

            resp.setData(userDTOS);
            return resp;
        }


    }


}

