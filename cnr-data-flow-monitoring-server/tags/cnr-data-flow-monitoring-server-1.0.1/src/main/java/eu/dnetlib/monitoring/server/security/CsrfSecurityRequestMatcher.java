package eu.dnetlib.monitoring.server.security;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * The class disables CSRF protection for /hessian/* path;
 * it won't be possible to access it programmatically otherwise.
 */
public class CsrfSecurityRequestMatcher implements RequestMatcher {
	private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
	private RegexRequestMatcher unprotectedMatcher = new RegexRequestMatcher("/hessian/*", null);

	@Override
	public boolean matches(HttpServletRequest request) {
		if(allowedMethods.matcher(request.getMethod()).matches()){
			return false;
		}
		return !unprotectedMatcher.matches(request);
	}
}
