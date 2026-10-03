@echo off
mvn initialize license:format spotless:apply %*
