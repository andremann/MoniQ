package eu.dnetlib.monitoring.user.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by andrea on 09/11/15.
 */
@Controller
@RequestMapping("/users")
public class UserController {
    private static final Log log = LogFactory.getLog(UserController.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> user(Principal user) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", user.getName());
        map.put("roles", AuthorityUtils.authorityListToSet(((Authentication) user).getAuthorities()));
        return map;
    }

}
