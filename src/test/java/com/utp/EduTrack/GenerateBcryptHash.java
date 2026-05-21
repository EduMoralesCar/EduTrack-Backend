package com.utp.EduTrack;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/** mvn -q test-compile exec:java -Dexec.classpathScope=test -Dexec.mainClass=com.utp.EduTrack.GenerateBcryptHash -Dexec.args="Admin123!" */
public class GenerateBcryptHash {
    public static void main(String[] args) {
        String raw = args.length > 0 ? args[0] : "Admin123!";
        System.out.println(new BCryptPasswordEncoder().encode(raw));
    }
}
