package com.ncedu.nc_edu.security;

import com.ncedu.nc_edu.models.UserRole.*;
import org.dom4j.rule.Mode;
import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashMap;
import java.util.Map;

public class View {

    public static final Map<GrantedAuthority, Class> RoleViewMap = new HashMap<>();

    static {
        RoleViewMap.put(UserRoles.ANONYMOUS.getAuthority(), UnAuthorized.class);
        RoleViewMap.put(UserRoles.USER.getAuthority(), User.class);
        RoleViewMap.put(UserRoles.MODERATOR.getAuthority(), Moderator.class);
        RoleViewMap.put(UserRoles.ADMIN.getAuthority(), Admin.class);
    }
    /*
    public static interface UnAuthorizedView {}
    public static interface UserView extends UnAuthorizedView {}
    public static interface OwnerView extends UserView {}
    public static interface ModeratorView extends UserView {}
    public static interface AdminView extends ModeratorView {}
    */

    public static class UnAuthorized {}
    public static class User extends UnAuthorized {}
    public static class Owner extends User {}
    public static class Moderator extends User {}
    public static class Admin extends Moderator {}



    public static Class<?> getView(GrantedAuthority authority) {
        if (authority == null)
            return null;
        return RoleViewMap.get(authority);
    }
}
