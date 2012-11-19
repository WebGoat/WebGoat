package org.owasp.webgoat.lessons;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.owasp.webgoat.lessons.model.HttpBasicsModel;
import org.owasp.webgoat.session.WebSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * Handles the "HTTP Basics" lesson.  Contains all 
 * mapping methods for that lesson as well as all helper methods
 * used by those mappers.
 * </p>
 * 
 */
@Controller
public class HttpBasicsController extends LessonAdapter {

	protected static Logger logger = Logger.getLogger("controller");
	
	// [url] path used by this lesson 
	private final String PAGE_PATH = "httpBasics.do";

	// The (apache) tile used by this lesson, as specified in tiles-definitions.xml 
	private String TILE_NAME = "http-basics";
	
	// ID attribute associated with the JSP's form.
	private String FORM_NAME = "command";
	
	
	/**
	 * @see {@link org.owasp.webgoat.lessons.AbstractLesson#getPath()}
	 * @see {@link org.owasp.webgoat.lessons.AbstractLesson#getLink()}
	 */
	protected String getPath() {
		return PAGE_PATH;
	}
	   
	/**
	 * Handles GET requests for this lesson.
	 * @return
	 */
    @RequestMapping(value = PAGE_PATH, method = RequestMethod.GET)
    public ModelAndView displayPage() {
    	return new ModelAndView(TILE_NAME, FORM_NAME, new HttpBasicsModel());
    }
    
    /**
     * Handles POST requests for this lesson.  Takes the user's name and displays 
     * a reversed copy of it.
     * 
     * @param httpBasicsModel
     * @param model
     * @return
     */
    @RequestMapping(value = PAGE_PATH, method = RequestMethod.POST)
    public ModelAndView processSubmit(
    		@ModelAttribute("")HttpBasicsModel httpBasicsModel, ModelMap model) {
    	
    	StringBuffer personName = new StringBuffer(httpBasicsModel.getPersonName());
    	httpBasicsModel.setPersonName(personName.reverse().toString());
    	
    	return new ModelAndView(TILE_NAME, FORM_NAME, httpBasicsModel);
    }    
    
    
	public Category getCategory()
	{
		return Category.GENERAL;
	}

	/**
	 * Gets the hints attribute of the HelloScreen object
	 * 
	 * @return The hints value
	 */
	public List<String> getHints(WebSession s)
	{
		List<String> hints = new ArrayList<String>();
		hints.add("Type in your name and press 'go'");
		hints.add("Turn on Show Parameters or other features");
		hints.add("Try to intercept the request with WebScarab");
		hints.add("Press the Show Lesson Plan button to view a lesson summary");
		hints.add("Press the Show Solution button to view a lesson solution");

		return hints;
	}

	protected String getInstructions()
	{
		return null;
	}

	public String getTitle()
	{
		// TODO: GET RID OF THE "(Spring MVC)" BELOW LATER!!!!"
		return "HTTP Basics (Spring MVC)";
	}    
}
