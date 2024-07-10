*** Settings ***
Documentation  Setup WebGoat Robotframework tests
Library  SeleniumLibrary  timeout=100  run_on_failure=Capture Page Screenshot
Library  String
Library  OperatingSystem

Suite Setup  Initial Page  ${ENDPOINT}  ${BROWSER}
Suite Teardown  Close Page

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
Initial Page
  [Documentation]  Check the initial page
  [Arguments]  ${ENDPOINT}  ${BROWSER}
  Log To Console  Start WebGoat UI Testing
  ${options} =  Evaluate  sys.modules['selenium.webdriver'].ChromeOptions()  sys
  IF  ${HEADLESS}
      ${options.add_argument}  -headless
      ${options.add_argument}  --start-maximized
  END
  ${options.add_experimental_option}  prefs  {'intl.accept_languages': 'en,en_US'}
  Open Browser  ${ENDPOINT}  ${BROWSER}  options=${options}  alias=webgoat
  Switch Browser  webgoat
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Set Window Position  ${0}  ${0}
  Set Selenium Speed  ${DELAY}
  Log To Console  Start WebWolf UI Testing
  ${options} =  Evaluate  sys.modules['selenium.webdriver'].ChromeOptions()  sys
  IF  ${HEADLESS}
      ${options.add_argument}  -headless
      ${options.add_argument}  --start-maximized
  END
  ${options.add_experimental_option}  prefs  {'intl.accept_languages': 'en,en_US'}
  Open Browser  ${ENDPOINT_WOLF}  ${BROWSER}  options=${options}  alias=webwolf
  Switch Browser  webwolf
  Maximize Browser Window
  Set Window Size  ${1400}  ${1000}
  Set Window Position  ${500}  ${0}
  Set Selenium Speed  ${DELAY}

Close Page
  [Documentation]  Closing the browser
  Log To Console  ==> Stop WebGoat UI Testing
  IF  ${HEADLESS}
    Switch Browser  webgoat
    Close Browser
    Switch Browser  webwolf
    Close Browser
  END

*** Test Cases ***
Check Initial Page
  [Tags]  WebGoatTests
  Switch Browser  webgoat
  Page Should Contain  Username
  Click Button  Sign in
  Page Should Contain  Invalid username
  Click Link  /WebGoat/registration

Check Registration Page
  [Tags]  WebGoatTests
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Input Text  matchingPassword  ${PASSWORD}
  Click Element  agree
  Click Button  Sign up

Check Welcome Page
  [Tags]  WebGoatTests
  Page Should Contain  WebGoat
  Go To  ${ENDPOINT}/login
  Page Should Contain  Username
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign in
  Page Should Contain  WebGoat

Check Menu Page
  [Tags]  WebGoatTests
  Click Element  css=a[category='Introduction']
  Click Element  Introduction-WebGoat
  Click Element  Introduction-WebWolf
  Click Element  css=a[category='General']
  Click Element  General-HTTPBasics
  Click Element  xpath=//*[.='2']
  Input Text  person  ${USERNAME}
  Click Button  Go!
  ${OUT_VALUE} =  Get Text  xpath=//div[contains(@class, 'attack-feedback')]
  ${OUT_RESULT} =  Evaluate  "resutobor" in """${OUT_VALUE}"""
  IF  not ${OUT_RESULT}
    Fail  "not ok"
  END

Check WebWolf
  Switch Browser  webwolf
  Location Should Be  ${ENDPOINT_WOLF}/login
  Input Text  username  ${USERNAME}
  Input Text  password  ${PASSWORD}
  Click Button  Sign In
  Go To  ${ENDPOINT_WOLF}/mail
  Go To  ${ENDPOINT_WOLF}/requests
  Go To  ${ENDPOINT_WOLF}/files

Check JWT Page
  Go To  ${END
