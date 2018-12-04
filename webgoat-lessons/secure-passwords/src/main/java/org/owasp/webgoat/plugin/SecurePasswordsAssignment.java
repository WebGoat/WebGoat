package org.owasp.webgoat.plugin;


import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.jruby.RubyProcess;
import org.owasp.webgoat.assignments.AssignmentEndpoint;
import org.owasp.webgoat.assignments.AssignmentHints;
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
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AssignmentPath("SecurePasswords/assignment")
//@AssignmentHints(value = {"xss-mitigation-3-hint1", "xss-mitigation-3-hint2", "xss-mitigation-3-hint3", "xss-mitigation-3-hint4"})
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
        output.append("<b>Score: </b>" + strength.getScore()+ "/5");
        if(strength.getScore()<=1){
            output.append("<div style=\"background-color:red;width: 200px;\">&nbsp;</div></br>");
        } else if(strength.getScore()<=3){
            output.append("<div style=\"background-color:orange;width: 200px;\">&nbsp;</div></br>");
        } else{
            output.append("<div style=\"background-color:green;width: 200px;\">&nbsp;</div></br>");
        }
        output.append("<b>Estimated cracking time in seconds: </b>" + calculateTime((long) strength.getCrackTimeSeconds().getOnlineNoThrottling10perSecond()));

        if(strength.getScore() >= 4)
            return trackProgress(success().feedback("securepassword-success").output(output.toString()).build());
        else
            return trackProgress(failed().feedback("securepassword-failed").output(output.toString()).build());
    }

    public static String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        int year = day/365;
        day = day % 365;
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return (year + " years " + day + " days " + hours + " hours " + minute + " minutes " + second + " seconds");

    }
}