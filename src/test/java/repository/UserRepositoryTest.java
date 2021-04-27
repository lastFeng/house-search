package repository;

import com.example.housesearch.HouseSearchApplication;
import com.example.housesearch.domain.User;
import com.example.housesearch.reposity.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author : guoweifeng
 * @date : 2021/4/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = HouseSearchApplication.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        Optional<User> byId = userRepository.findById(1);
        Assert.assertNotNull(byId.get());
        System.out.println(byId.get());
    }
}
