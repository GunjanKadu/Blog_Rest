package com.api.rest.util;

import com.api.rest.BlogUser;



public class GlobalInfo {
    private static BlogUser blogUser;

    public static BlogUser getBlogUser() {
        return blogUser;
    }

    public static void setBlogUser(BlogUser blogUser) {
        GlobalInfo.blogUser = blogUser;
    }
}
