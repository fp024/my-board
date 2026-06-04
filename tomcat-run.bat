@ECHO OFF
SETLOCAL
ECHO [Tomcat Run...]
CALL .\set-jdk-env.bat
CALL .\mvnw.cmd -t .\toolchains.xml clean package -Ptomcat-run -DskipTests cargo:run