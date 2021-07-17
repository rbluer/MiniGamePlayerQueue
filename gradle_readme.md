Gradle Read Me

2021-07-17 - Updating gradle to v7.x
 - Had installed java 16 so cannot run the current gradle version which is v5.6.4
 - Must use -Dorg.gradle.java.home="C:\Program Files\Java\jdk1.8.0_291" to force gradle to use java 1.8
 
 To upgrade gradle:
 
  ./gradlew wrapper --gradle-version 6.0 -Dorg.gradle.java.home="C:\Program Files\Java\jdk1.8.0_291" 
  ./gradlew --version -Dorg.gradle.java.home="C:\Program Files\Java\jdk1.8.0_291" :: Will actually install the new version
  gradlew build (as normal):: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
  
  