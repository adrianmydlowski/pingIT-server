package src.pingit.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;

public class ServerUtil {

    private enum ServerImage {
        SERVER_1("server1.png"),
        SERVER_2("server2.png"),
        SERVER_3("server3.png"),
        SERVER_4("server4.png");

        private final String name;

        ServerImage(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static final ServerImage[] VALUES = values();
    }

    public static String getRandomServerImage() {
        ServerImage serverImage = ServerImage.VALUES[new Random().nextInt(ServerImage.VALUES.length)];
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("server/image/" + serverImage.getName()).build().toUriString();
    }
}
