<h1>Language learning</h1>

![img1](https://user-images.githubusercontent.com/6876210/47258669-39d9c480-d4a7-11e8-8c35-cbc4d613a5dd.png)

<h4>Create categories of words</h4>

![img2](https://user-images.githubusercontent.com/6876210/47258670-3f370f00-d4a7-11e8-9489-c8918b49094a.png)

<h4>Learn them in a quiz form</h4>

![img3](https://user-images.githubusercontent.com/6876210/47258672-4231ff80-d4a7-11e8-98c3-3ebf371ff0c4.png)

The project is aimed to help English learners to improve their vocabulary.
People which were learning foreign languages know that a strategy with just writing down lists of unknown words doesn't work.
All it can give is a storage of these words in a short-term memory, i.e. they disappear from a memory in a short period of time.
This project is trying to make your brain store collected words in a long-term memory.
It can be achieved by using active techniques using quizzes, spending more time on words
which are more difficult for a person. 
The idea is quite popular. There are multiple alternatives.

This project has been mainly done for personal purposes: to use it for learning foreign languages and
get hands-on experience on used technologies listed below.

<h3>Technologies used:</h3>
<ol>
    <li>Spring Boot</li>
    <li>Hibernate</li>
    <li>JSP</li>
    <li>Maven</li>
</ol>

The main efforts were put on back-end.
Front-end is a quick solution just to make it work as soon as possible. 


For development you can generate a self-signed certificate using <b>keytool</b> and configure it in <b>application.yml</b>:<br>
<code>
keytool -genkey -alias tomcat -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore keystore.p12 -validity 3650
</code>

Details - https://drissamri.be/blog/java/enable-https-in-spring-boot

In order to use <b>Google/Facebook</b> for authentication, you'll need to create
corresponding applications and configure application id and secret in <b>application.yml</b>.

<h3>How to start the application locally</h3>
<ol>
    <li>mvn clean package</li>
    <li>java -jar <path_to_a_created_war></li>
    <li>Open https://localhost:8443 in browser</li>
</ol> 
