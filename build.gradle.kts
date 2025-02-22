plugins {
    id 'java'
    id 'com.android.application' version '8.2.0' apply false
}

group = 'com.smartinvent'
version = '1.0-SNAPSHOT'


dependencies {
    testImplementation platform('org.junit:junit-bom:5.10.0')
    testImplementation 'org.junit.jupiter:junit-jupiter'
}

test {
    useJUnitPlatform()
}