/*
 (C)2024 sylvxa
 All Rights Reserved
*/
package lgbt.sylvia.dispair;

import com.google.gson.JsonObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jetbrains.annotations.Nullable;

public class WebhookMessage {
    public String icon;
    public String username;
    public String content;

    public WebhookMessage(@Nullable String username, @Nullable String icon, String content) {
        this.username = username;
        this.icon = icon;
        this.content = content;
    }

    public String toJson() {
        JsonObject jsonObject = new JsonObject();
        if (this.username != null && !this.username.isEmpty())
            jsonObject.addProperty("username", this.username);
        if (this.icon != null && !this.icon.isEmpty())
            jsonObject.addProperty("avatar_url", this.icon);
        jsonObject.addProperty("content", this.content);
        return jsonObject.toString();
    }

    public void send(String webhook) {
        new Thread(
                        () -> {
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
                        })
                .start();
    }
}
