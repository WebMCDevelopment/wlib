mvn -pl modules/misc/devkit install $@
mvn initialize license:format spotless:apply $@
