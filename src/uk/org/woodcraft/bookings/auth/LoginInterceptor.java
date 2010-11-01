package uk.org.woodcraft.bookings.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.StrutsStatics;

import uk.org.woodcraft.bookings.datamodel.AppSetting;
import uk.org.woodcraft.bookings.datamodel.User;
import uk.org.woodcraft.bookings.persistence.CannedQueries;
import uk.org.woodcraft.bookings.utils.SessionUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.Interceptor;

/*
 * (C) 2007 Mark Menard & Vita Rara, Inc.
 *
 * Mark Menard and Vita Rara, Inc. licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/***
 * From http://www.vitarara.org/cms/struts_2_cookbook/creating_a_login_interceptor
 */
public class LoginInterceptor implements Interceptor {

	private static final long serialVersionUID = 8619880039212130618L;

	private static final Log log = LogFactory.getLog (LoginInterceptor.class);

	private static final String LOGIN_ATTEMPT = "LOGIN_ATTEMPT";
	
	private static final String LOGIN_EMAIL = "LOGIN_EMAIL";
	private static final String LOGIN_PASSWORD = "LOGIN_PASSWORD";
	
	@Override
	public void destroy() {
	}

	@Override
	public void init() {	
		log.info("Initializing LoginInterceptor");
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		// Get the action context from the invocation so we can access the
	    // HttpServletRequest and HttpSession objects.
	    final ActionContext context = invocation.getInvocationContext ();
	    HttpServletRequest request = (HttpServletRequest) context.get(StrutsStatics.HTTP_REQUEST);
	    HttpSession session =  request.getSession (true);

	    // Is there a "user" object stored in the user's HttpSession?
	    Object user = session.getAttribute (SessionConstants.USER_HANDLE);
	    if (user == null) {
	        // The user has not logged in yet.

	        // Is the user attempting to log in right now?
	        String loginAttempt = request.getParameter (LOGIN_ATTEMPT);
	        if (! StringUtils.isBlank (loginAttempt) ) { // The user is attempting to log in.

	            // Process the user's login attempt.
	            if (processLoginAttempt (request, session) ) {
	                // The login succeeded send them the login-success page.
	            	
	            	// If they were originally going somewhere else, try and send them there again
	            	String intendedUri = (String) session.getAttribute(SessionConstants.LOGIN_REDIRECT);
	            	if( intendedUri != null)
	            	{
	            		session.removeAttribute(SessionConstants.LOGIN_REDIRECT);
	            		((HttpServletResponse)context.get(StrutsStatics.HTTP_RESPONSE)).sendRedirect(intendedUri);
	            	}
	            	
	            	// Otherwise, go to the default
	                return "login-success";
	            } else {
	                // The login failed. Set an error if we can on the action.
	                Object action = invocation.getAction ();
	                if (action instanceof ValidationAware) {
	                    ((ValidationAware) action).addActionError ("Username or password incorrect.");
	                }
	            }
	        }

	        // Either the login attempt failed or the user hasn't tried to login yet, 
	        // and we need to send the login form.
	       String requestedURI = request.getRequestURI();
	       session.setAttribute(SessionConstants.LOGIN_REDIRECT, requestedURI);
	        
	        return "login";
	    } else {
	        return invocation.invoke ();
	    }
	}

	private boolean processLoginAttempt(HttpServletRequest request, HttpSession session) {
		
	    String email = request.getParameter(LOGIN_EMAIL);
	    String password = request.getParameter(LOGIN_PASSWORD);

	    User user = CannedQueries.getUserByEmail(email);
	    
	    if (user != null && user.getAccessLevel().getCanLogin() && user.checkPassword(password))
		{
	        // The user has successfully logged in. Store their user object in 
	        // their HttpSession, and their home org and unit. Then return true.
	        session.setAttribute(SessionConstants.USER_HANDLE, user);
	        SessionUtils.setCurrentUserDetails(session,  AppSetting.getDefaultEvent(), user.getOrganisation(), user.getUnit());
	        
	        return true;
	    } else {
	        // The user did not successfully log in. Return false.
	        return false;
	    }
	}

}
