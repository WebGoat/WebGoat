package org.owasp.webgoat.plugin;

import com.nulabinc.zxcvbn.Feedback;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.jruby.RubyProcess;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentPath;
import org.owasp.webgoat.assignments.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.tools.*;
import java.io.IOException;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AssignmentPath("SecurePasswords/assignment")
public class SecurePasswordsAssignment extends AssignmentEndpoint {

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public AttackResult completed(@RequestParam String password) {
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);
        StringBuffer output = new StringBuffer();
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340);

        output.append("<b>Your Password: </b>" + password + "</br>");
        output.append("<b>Length: </b>" + password.length()+ "</br>");
        output.append("<b>Estimated guesses needed to crack your password: </b>" + df.format(strength.getGuesses())+ "</br>");
        output.append("<div style=\"float: left;padding-right: 10px;\"><b>Score: </b>" + strength.getScore()+ "/4 </div>");
        if(strength.getScore()<=1){
            output.append("<div style=\"background-color:red;width: 200px;border-radius: 12px;float: left;\">&nbsp;</div></br>");
        } else if(strength.getScore()<=3){
            output.append("<div style=\"background-color:orange;width: 200px;border-radius: 12px;float: left;\">&nbsp;</div></br>");
        } else{
            output.append("<div style=\"background-color:green;width: 200px;border-radius: 12px;float: left;\">&nbsp;</div></br>");
        }
        output.append("<b>Estimated cracking time: </b>" + calculateTime((long) strength.getCrackTimeSeconds().getOnlineNoThrottling10perSecond()));
        if(strength.getFeedback().getWarning().length() != 0)
            output.append("</br><b>Warning: </b>" + strength.getFeedback().getWarning());
        // possible feedback: https://github.com/dropbox/zxcvbn/blob/master/src/feedback.coffee
        // maybe ask user to try also weak passwords to see and understand feedback?
        if(strength.getFeedback().getSuggestions().size() != 0){
            output.append("</br><b>Suggestions:</b></br><ul>");
            for(String sug: strength.getFeedback().getSuggestions()) output.append("<li>"+sug+"</li>");
            output.append("</ul></br>");
        }
        output.append("<b>Score: </b>" + strength.getScore()+ "/5 </br>");
        output.append("<b>Estimated cracking time in seconds: </b>" + calculateTime((long) strength.getCrackTimeSeconds().getOnlineNoThrottling10perSecond()));

        if(strength.getScore() >= 4)
            return trackProgress(success().feedback("securepassword-success").output(output.toString()).build());
        else
            return trackProgress(failed().feedback("securepassword-failed").output(output.toString()).build());
    }

    public static String calculateTime(long seconds) {
        int s = 1;
        int min = (60*s);
        int hr = (60*min);
        int d = (24*hr);
        int yr = (365*d);

        long years = seconds/(d)/365;
        long days = (seconds%yr)/(d);
        long hours = (seconds%d)/(hr);
        long minutes = (seconds%hr)/(min);
        long sec = (seconds%min*s);

        return (years + " years " + days + " days " + hours + " hours " + minutes + " minutes " + sec + " seconds");
    }
}