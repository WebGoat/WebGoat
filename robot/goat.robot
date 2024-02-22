*** Settings ***
Documentation  Setup WebGoat Robotframework tests
Library  SeleniumLibrary  timeout=100  run_on_failure=Capture Page Screenshot
Library  String
Library  OperatingSystem

Suite Setup  Initial_Page  ${ENDPOINT}  ${BROWSER}
Suite Teardown  Close_Page

*** Variables ***
${BROWSER}  chrome
${SLEEP}  100
${DELAY}  0.25
${ENDPOINT}  http://127.0.0.1:8080/WebGoat
${ENDPOINT_WOLF}  http://127.0.0.1:9090/WebWolf
${USERNAME}  robotuser
${PASSWORD}  password
${HEADLESS}  ${FALSE}

*** Keywords ***
Initial_Page
  [Documentation]  Check the inital page
  [Arguments]  ${ENDPOINT}  ${BROWSER}
  Log To Console  Start WebGoat UI Testing
  IF  ${HEADLESS}
      Open Browser  ${ENDPOINT}  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'});add_argument("-headless");add_argument("--start-maximized")  alias=webgoat
  ELSE
      Open Browser  ${ENDPOINT}  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webgoat
  END
  Switch Browser  webgoat
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Set Window Position  ${0}  ${0}
  Set Selenium Speed  ${DELAY}
  Log To Console  Start WebWolf UI Testing
  IF  ${HEADLESS}
      Open Browser  ${ENDPOINT_WOLF}  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'});add_argument("-headless");add_argument("--start-maximized")  alias=webwolf
  ELSE
      Open Browser  ${ENDPOINT_WOLF}  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webwolf
  END
  Switch Browser  webwolf
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Set Window Position  ${500}  ${0}
  Set Selenium Speed  ${DELAY}

Close_Page
  [Documentation]  Closing the browser
  Log To Console  ==> Stop WebGoat UI Testing
  IF  ${HEADLESS}
    Switch Browser  webgoat
    Close Browser
    Switch Browser  webwolf
    Close Browser
  END

*** Test Cases ***

Check_Initial_Page
  [Tags]  WebGoatTests
  Switch Browser  webgoat
  Page Should Contain  Username
  Click Button  Sign in
  Page Should Contain  Invalid username
  Click Link  /WebGoat/registration

Check_Registration_Page
  [Tags]  WebGoatTests
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Input Text  matchingPassword  ${PASSWORD}
  Click Element  agree
  Click Button  Sign up

Check_Welcome_Page
  [Tags]  WebGoatTests
  Page Should Contain  WebGoat
  Go To  ${ENDPOINT}/login
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign in
  Page Should Contain  WebGoat

Check_Menu_Page
  [Tags]  WebGoatTests
  Click Element  css=a[category='Introduction']
  Click Element  Introduction-WebGoat
  CLick Element  Introduction-WebWolf
  Click Element  css=a[category='General']
  CLick Element  General-HTTPBasics
  Click Element  xpath=//*[.='2']
  Input Text     person  ${USERNAME}
  Click Button   Go!
  ${OUT_VALUE}   Get Text  xpath=//div[contains(@class, 'attack-feedback')]
  ${OUT_RESULT}  Evaluate  "resutobor" in """${OUT_VALUE}"""
  IF  not ${OUT_RESULT}
    Fail  "not ok"
  END

Check_WebWolf
  Switch Browser  webwolf
  location should be  ${ENDPOINT_WOLF}/login
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign In
  Go To  ${ENDPOINT_WOLF}/mail
  Go To  ${ENDPOINT_WOLF}/requests
  Go To  ${ENDPOINT_WOLF}/files

Check_JWT_Page
  Go To  ${ENDPOINT_WOLF}/jwt
  Click Element  token
  Wait Until Element Is Enabled  token  5s
  Input Text     token  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
  Click Element  secretKey
  Input Text     secretKey  none
  Sleep  2s  # Pause before reading the result
  ${OUT_VALUE}   Get Value  xpath=//textarea[@id='token']
  Log To Console  Found token ${OUT_VALUE}
  ${OUT_RESULT}  Evaluate  "ImuPnHvLdU7ULKfbD4aJU" in """${OUT_VALUE}"""
  Log To Console  Found token ${OUT_RESULT}
  Capture Page Screenshot

Check_Files_Page
  Go To  ${ENDPOINT_WOLF}/files
  Choose File  css:input[type="file"]  ${CURDIR}/goat.robot
  Click Button  Upload files
