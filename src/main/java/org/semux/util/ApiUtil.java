/**
 * Copyright (c) 2017 The Semux Developers
 *
 * Distributed under the MIT software license, see the accompanying file
 * LICENSE or https://opensource.org/licenses/mit-license.php
 */
package org.semux.util;

import org.semux.crypto.Hex;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApiUtil {
    private InetSocketAddress server;
    private String username;
    private String password;

    public ApiUtil(InetSocketAddress server, String username, String password) {
        this.server = server;
        this.username = username;
        this.password = password;
    }

    /**
     * Sends an API request. <br>
     * <br>
     * NOTE: Byte array parameters will be automatically converted into its Hex
     * representation.
     *
     * @param cmd
     * @param params
     * @return
     * @throws IOException
     */
    public JsonObject request(String cmd, Map<String, Object> params) throws IOException {
        // construct URL
        String url = "http://" + server.getAddress().getHostAddress() + ":" + server.getPort() + "/" + cmd;

        // construct parameters
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String v;
            if (entry.getValue() instanceof byte[]) {
                v = Hex.encode((byte[]) entry.getValue());
            } else {
                v = URLEncoder.encode(entry.getValue().toString(), "UTF-8");
            }
            sb.append("&").append(entry.getKey()).append("=").append(v);
        }
        url += sb.length() == 0 ? "" : "?" + sb.substring(1);
        sb.setLength(0);

        // request
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestProperty("Authorization",
                "Basic " + Base64.getEncoder().encodeToString(Bytes.of(username + ":" + password)));

        try (JsonReader jsonReader = Json.createReader(con.getInputStream())) {
            return jsonReader.readObject();
        }
    }

    /**
     * Sends an API request.<br>
     * <br>
     * NOTE: Byte array parameters will be automatically converted into its Hex
     * representation.
     *
     * @param cmd
     * @param params
     * @return
     * @throws IOException
     */
    public JsonObject request(String cmd, Object... params) throws IOException {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i + 1 < params.length; i += 2) {
            map.put(params[i].toString(), params[i + 1]);
        }

        return request(cmd, map);
    }
}
