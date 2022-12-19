package org.pih.service;


import lombok.extern.slf4j.Slf4j;
import org.pih.entity.User;
import org.pih.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Async
    public CompletableFuture<List<User>> saveUser(MultipartFile file) throws Exception {
        long start = System.currentTimeMillis();

        List<User> users = parseCSVFile(file);
        log.info("Saving list of users of size {}",users.size(),""+Thread.currentThread().getName());
        users = userRepository.saveAll(users);
        long end = System.currentTimeMillis();

        log.info("Total time {}",(end - start));
        return CompletableFuture.completedFuture(users);



    }


    @Async
    public CompletableFuture<List<User>> findAllUsers(){
        log.info("get the all active users by :"+Thread.currentThread().getName());
        return CompletableFuture.completedFuture(userRepository.findAll());
    }

    public List<User> parseCSVFile(final MultipartFile file) throws Exception{
        final List<User> users= new ArrayList<>();
        try {
            try(final BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
                String line;
                while((line = br.readLine()) != null){
                    final String[] data = line.split(",");
                    final User user = new User();
                    user.setEmail(data[1]);
                    user.setName(data[0]);
                    user.setGender(data[2]);
                    users.add(user);
                }
                return users;
            }
        }catch (final IOException e){
            log.error("Failed to parseCSV file {}", e);
            throw new Exception("Failed to parseCSV {}", e);
        }
    }
}
