package lgbt.sylvia.dispair;

import com.google.gson.JsonObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

public class WebhookMessage {
    public String icon;
    public String username;
    public String content;

    public WebhookMessage(String username, String icon, String content) {
        this.username = username;
        this.icon = icon;
        this.content = content;
    }

    public String toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", this.username);
        jsonObject.addProperty("content", this.content);
        jsonObject.addProperty("avatar_url", this.icon);
        return jsonObject.toString();
    }

    public void send(String webhook) {
        new Thread(() -> {
            try {
                HttpClient httpclient = HttpClients.createDefault();
                HttpPost httppost = new HttpPost(webhook);
                StringEntity entity = new StringEntity(this.toJson());
                entity.setContentType("application/json");
                httppost.setEntity(entity);
                httpclient.execute(httppost);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }).start();
    }
}
