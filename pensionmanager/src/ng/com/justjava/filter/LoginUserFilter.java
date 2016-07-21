package ng.com.justjava.filter;

import java.util.*;

import org.openxava.filters.*;
import org.openxava.util.*;

public class LoginUserFilter implements IFilter {

	public Object filter(Object o) throws FilterException {


			String loginUser = Users.getCurrent(); // (2)
			Object[] r = null;
			if(o == null){
				r = new Object[1];

				//result.add(null);
				r[0] =  loginUser;
				return  r;
			}if(o instanceof Object []){
				 Object [] a = (Object []) o;
				 r = new Object[a.length + 1];
				 r[0] = loginUser;
				 for (int i = 0; i < a.length; i++) {
					 r[i+1]=a[i];
				 }
				 return r;
			 }else { // (5)
				 r = new Object[2];
				 r[0] = loginUser;
				 r[1] = o;
				 return r;
			 }			

	}
}
