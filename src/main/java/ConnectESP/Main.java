package ConnectESP;

import java.net.URI;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) {
        String serverUri = "ws://192.168.1.4:81"; // Địa chỉ IP của Arduino WebSocket Server
        try {
            ArduinoWebSocketClient client = new ArduinoWebSocketClient(new URI(serverUri));
            client.connect();
            System.out.println();
            // Vòng lặp đơn giản để giữ ứng dụng chạy và lắng nghe WebSocket
            while (true) {
                Thread.sleep(1000); // Chạy liên tục
            }
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
