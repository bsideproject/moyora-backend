package com.beside.ties.auth.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Map;
import java.util.Random;

/**
 * JWT Key를 제공하고 조회합니다.
 */
public class JwtKey {
    /**
     * Kid-Key List 외부로 절대 유출되어서는 안됩니다.
     * 현재 key 값은 임시로 사용 git에서 secret 기능 사용 후 변경 예정
     */
    private static final Map<String, String> SECRET_KEY_SET = Map.of(
            "key1", "ewfgtrefetfretgwertgfrewgtuiretgfoirtfgoir;tgfriwptgwotuftgf34y8efh3289r",
            "key2", "ewrewtrf43ytertgfertger354ter34tgfertfgetg324e32drfewdfredftge",
            "key3", "fuisdhfiuewflewruifeiouteou324324r32r3etfgiuewatu"
    );
    private static final String[] KID_SET = SECRET_KEY_SET.keySet().toArray(new String[0]);
    private static Random randomIndex = new Random();

    /**
     * SECRET_KEY_SET 에서 랜덤한 KEY 가져오기
     *
     * @return kid와 key Pair
     */
    public static Pair<String, Key> getRandomKey() {
        String kid = KID_SET[randomIndex.nextInt(KID_SET.length)];
        String secretKey = SECRET_KEY_SET.get(kid);
        return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * kid로 Key찾기
     *
     * @param kid kid
     * @return Key
     */
    public static Key getKey(String kid) {
        String key = SECRET_KEY_SET.getOrDefault(kid, null);
        if (key == null)
            return null;
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }
}
