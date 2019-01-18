package org.owasp.webgoat.plugin;

import org.jcodings.util.Hash;
import org.owasp.webgoat.session.UserSessionData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by appsec on 7/18/17.
 */
public class AccountVerificationHelper {



    //simulating database storage of verification credentials
    private  static final Integer verifyUserId = new Integer(1223445);
    private static final Map<String,String> userSecQuestions = new HashMap<>();
    static {
        userSecQuestions.put("secQuestion0","Dr. Watson");
        userSecQuestions.put("secQuestion1","Baker Street");
    }

    private static final Map<Integer,Map> secQuestionStore = new HashMap<>();
    static {
        secQuestionStore.put(verifyUserId,userSecQuestions);
    }
    // end 'data store set up'

    // this is to aid feedback in the attack process and is not intended to be part of the 'vulnerable' code
    public boolean didUserLikelylCheat(HashMap<String,String> submittedAnswers) {
        boolean likely = false;

        if (submittedAnswers.size() == secQuestionStore.get(verifyUserId).size()) {
            likely = true;
        }

        if ((submittedAnswers.containsKey("secQuestion0") && submittedAnswers.get("secQuestion0").equals(secQuestionStore.get(verifyUserId).get("secQuestion0"))) &&
                (submittedAnswers.containsKey("secQuestion1") && submittedAnswers.get("secQuestion1").equals(secQuestionStore.get(verifyUserId).get("secQuestion1"))) ) {
            likely = true;
        } else {
            likely = false;
        }

        return likely;

    }
    //end of cheating check ... the method below is the one of real interest. Can you find the flaw?

    public boolean verifyAccount(Integer userId, HashMap<String,String> submittedQuestions ) {
        //short circuit if no questions are submitted
        if (submittedQuestions.entrySet().size() != secQuestionStore.get(verifyUserId).size()) {
            return false;
        }

        if (submittedQuestions.containsKey("secQuestion0") && !submittedQuestions.get("secQuestion0").equals(secQuestionStore.get(verifyUserId).get("secQuestion0"))) {
            return false;
        }

        if (submittedQuestions.containsKey("secQuestion1") && !submittedQuestions.get("secQuestion1").equals(secQuestionStore.get(verifyUserId).get("secQuestion1"))) {
            return false;
        }

        // else
        return true;

    }
}
