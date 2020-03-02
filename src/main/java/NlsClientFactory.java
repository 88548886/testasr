import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NlsClientFactory {


    private static final String appKey = "6IiNqbuvYmOqJE77";
    private static final String id = "vC2cUveCZLHoaakg";
    private static final String secret = "follNRRvhwx2Fvg7NgMOZ0FKSy1LRy";
    private static final String url = "wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1";
    private static AccessToken accessToken;
    private static NlsClient client;

    static {
        getClient();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (null != client) {
                    client.shutdown();
                }
            }
        });
    }

    public static NlsClient getClient() {
        if (null == accessToken || ((accessToken.getExpireTime() * 1000) - System.currentTimeMillis()) < TimeUnit.MINUTES.toMillis(10)) {
            int tryCounter = 0;
            while (tryCounter < 3) {
                tryCounter++;
                try {
                    accessToken = new AccessToken(id, secret);
                    accessToken.apply();
                    if (null != client) {
                        client.shutdown();
                    }
                    client = new NlsClient(url, accessToken.getToken());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return client;
    }
}
