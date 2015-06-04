
## Prepare for the workshop

The following commands should run without errors:
* `git clone https://github.com/rosatolen/xss-presentation.git`
* `cd xss-presentation`
* `./gradlew xss-presentation:test`
* `./gradlew xss-presentation:run`

## Run tests

* `./gradlew xss-presentation:test`
* `./gradlew xss-presentation:intTest`
* `./gradlew xss-presentation:uaTest`
* `./gradlew xss-presentation:appSecTest # should fail at first`

## Run the app

* `./gradlew xss-presentation:run`
* Browse to `http://localhost:8080`
* Admin username/password is `admin/password`

## Fix it!
* The `appSecTest` are there to guide you though fixing the vulnerabilities
* But remember, a passing test does not imply correctness!

## Review slides (or become a facilitator!)
* `./gradlew presentation:run`
* Open `http://localhost:8081` in a web browser

## Learn more

* OWASP XSS Cheat Sheet: *https://www.owasp.org/index.php/XSS_%28Cross_Site_Scripting%29_Prevention_Cheat_Sheet">OWASP
XSS Cheat Sheet*
* OWASP CSRF Cheat Sheet: *https://www.owasp.org/index.php/Cross-Site_Request_Forgery_%28CSRF%29_Prevention_Cheat_Sheet*
* OWASP Top 10 - A3 – Cross-Site Scripting: *https://www.owasp.org/index.php/Top_10_2013-A3-Cross-Site_Scripting_(XSS)*
* OWASP Top 10 - A8 – Cross-Site Request Forgery: *https://www.owasp.org/index.php/Top_10_2013-A8-Cross-Site_Request_Forgery_(CSRF)*
