@ECHO OFF
SETLOCAL
ECHO [Tomcat Stop...]
CALL .\set-jdk-env.bat
CALL .\mvnw.cmd -t .\toolchains.xml -Ptomcat-run cargo:stop