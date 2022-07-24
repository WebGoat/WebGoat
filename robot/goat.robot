*** Settings ***
Documentation  Setup WebGoat Robotframework tests
Library  SeleniumLibrary  timeout=100  run_on_failure=Capture Page Screenshot
Library  String

Suite Setup  Initial_Page  ${ENDPOINT}  ${BROWSER}
Suite Teardown  Close_Page

*** Variables ***
${BROWSER}  chrome
${SLEEP}  100
${DELAY}  0.25
${ENDPOINT}  http://127.0.0.1:8080/WebGoat
${ENDPOINT_WOLF}  http://127.0.0.1:9090
${USERNAME}  robotuser
${PASSWORD}  password
${HEADLESS}  ${FALSE}

*** Keywords ***
Initial_Page
  [Documentation]  Check the inital page
  [Arguments]  ${ENDPOINT}  ${BROWSER}
  Log To Console  Start WebGoat UI Testing
  IF  ${HEADLESS}
      Open Browser  ${ENDPOINT}  ${BROWSER}  options=add_argument("-headless");add_argument("--start-maximized");add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webgoat
  ELSE
      Open Browser  ${ENDPOINT}  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webgoat
  END
  IF  ${HEADLESS}
      Open Browser  ${ENDPOINT_WOLF}/WebWolf  ${BROWSER}  options=add_argument("-headless");add_argument("--start-maximized");add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webwolf
  ELSE
      Open Browser  ${ENDPOINT_WOLF}/WebWolf  ${BROWSER}  options=add_experimental_option('prefs', {'intl.accept_languages': 'en,en_US'})  alias=webwolf
  END
  Switch Browser  webgoat
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Switch Browser  webwolf
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Set Window Position  ${400}  ${200}
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
  Switch Browser  webgoat
  Page Should Contain  Username
  Click Button  Sign in
  Page Should Contain  Invalid username
  Click Link  /WebGoat/registration

Check_Registration_Page
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Input Text  matchingPassword  ${PASSWORD}
  Click Element  agree
  Click Button  Sign up

Check_Welcome_Page
  Page Should Contain  WebGoat
  Go To  ${ENDPOINT}/login
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign in
  Page Should Contain  WebGoat

Check_Menu_Page
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
  location should be  ${ENDPOINT_WOLF}/WebWolf
  Go To  ${ENDPOINT_WOLF}/mail
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign In

