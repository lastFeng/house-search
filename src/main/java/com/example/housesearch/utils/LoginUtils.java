package com.example.housesearch.utils;

import com.example.housesearch.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.regex.Pattern;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
public class LoginUtils {
    private LoginUtils() {}

    /**
     * 手机号正则表达式
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     */
    private static final String PHONE_REGEX = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**邮箱正则表达式*/
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /***
     * 检查telephone是否输入错误
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     * @param telephone 手机号
     * @return true-正确手机号；false-错误手机号
     */
    public static boolean checkTelephone(String telephone) {
        return PHONE_PATTERN.matcher(telephone).matches();
    }

    public static boolean checkEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static User load() {
        Object principal =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            return (User)principal;
        }
        return null;
    }

    public static Integer getLoginUserId() {
        User user = load();
        if (user == null) {
            return -1;
        }
        return user.getId();
    }
}
