package org.launchcode.watchlist.Services;

import org.launchcode.watchlist.Models.User;
import org.launchcode.watchlist.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(){}

    public void saveProfilePicture(User user, MultipartFile imageFile) throws Exception{

        if (imageFile.getContentType().startsWith("image")) {

            Path currentPath = Paths.get(".");
            Path absolutePath = currentPath.toAbsolutePath();
            String fileName = imageFile.getOriginalFilename();
            int separatorIndex = fileName.lastIndexOf(".");
            fileName = user.getUsername() + fileName.substring(separatorIndex);

            byte[] bytes = imageFile.getBytes();
            Path path = Paths.get(absolutePath + "/src/main/resources/static/images/profilepictures/" + fileName);
            Files.write(path, bytes);

            user.setProfilePicturePath(fileName);
            userRepository.save(user);
        }
    }
}
