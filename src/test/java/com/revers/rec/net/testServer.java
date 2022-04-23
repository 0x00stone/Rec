package com.revers.rec.net;

import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.net.Server.Server;
import com.revers.rec.util.cypher.DigestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.*;

import static com.revers.rec.util.cypher.RsaUtil.createKeys;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testServer {

    @Test
    public void testServer() throws ExecutionException, InterruptedException {
        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvdYTFZkfztFrJVnHjqiEwsLelPkO6JS7z3Wj3Oi9duXG375gWaoaEgbVpTcuxI032pmLCwL_FeV72djtzjkgLdrU7qXuhxC6n9SN3fKW6ufWkpMgKZ09XrcOZxdCpAC-Abp7lhZY5PyCq52ITOTb5glaL8vFh-iXWdV1DqkrQABwZ45fwNW_ReKykxIzIBfgalK53w1ZWPfqyNtQlOBreLRSv5zmSvT4Fuwj_HzQIyAnc_EsEVtwEh-hM--hsnxy8nFEiFSmkYaisd4LO1prwjAiWpM1A4xXk74Yq7hdEhA6U3kseC5G32yVAX7C0Ccw47MB4t7Fz8GJzWSMDLIuUXsRs3tmNboLhPZTxOdzSgF-eYsuCPvYEwGfXt9_sDv2Hd92XinMh6Mp-MSsV7eM8NN57FzM2J7u4mkaL6T_d51VMBgjZaa_Ct0rXs3YAGkdz_xdfYNTYClcLuO06hnmZ_f-rcWnjMJTJwRl3n2NAWPRyXk5xpB8J83fw24J0TalwxajR4yteMOpEL-7lR0jNxt1GAvT0sEhE3hfvF-2AJ0o8A42Gj0QlIlB-E_r7I3OB6K7L86AIcuNPSF21XJm6HyCL_fJqbCvJkLMwAnBg7G5ExNI4f_zehNG5AJcxGawhF5d6LENHD_fzGsytvBY6Yf7JuImiQfPSSU52YlNv0MCAwEAAQ";
        String privateKey = "MIIJQQIBADANBgkqhkiG9w0BAQEFAASCCSswggknAgEAAoICAQC91hMVmR_O0WslWceOqITCwt6U-Q7olLvPdaPc6L125cbfvmBZqhoSBtWlNy7EjTfamYsLAv8V5XvZ2O3OOSAt2tTupe6HELqf1I3d8pbq59aSkyApnT1etw5nF0KkAL4BunuWFljk_IKrnYhM5NvmCVovy8WH6JdZ1XUOqStAAHBnjl_A1b9F4rKTEjMgF-BqUrnfDVlY9-rI21CU4Gt4tFK_nOZK9PgW7CP8fNAjICdz8SwRW3ASH6Ez76GyfHLycUSIVKaRhqKx3gs7WmvCMCJakzUDjFeTvhiruF0SEDpTeSx4LkbfbJUBfsLQJzDjswHi3sXPwYnNZIwMsi5RexGze2Y1uguE9lPE53NKAX55iy4I-9gTAZ9e33-wO_Yd33ZeKcyHoyn4xKxXt4zw03nsXMzYnu7iaRovpP93nVUwGCNlpr8K3StezdgAaR3P_F19g1NgKVwu47TqGeZn9_6txaeMwlMnBGXefY0BY9HJeTnGkHwnzd_DbgnRNqXDFqNHjK14w6kQv7uVHSM3G3UYC9PSwSETeF-8X7YAnSjwDjYaPRCUiUH4T-vsjc4HorsvzoAhy409IXbVcmbofIIv98mpsK8mQszACcGDsbkTE0jh__N6E0bkAlzEZrCEXl3osQ0cP9_MazK28Fjph_sm4iaJB89JJTnZiU2_QwIDAQABAoICAD5LHLkWJb9mrfu0Mc1I1EPa0VJeRfuhLt39cvd_MgjtjqyGGrThoitvLbVI3eJmmsDzaJKE-bK0Osd6LVLiIQKbEAt9zL9qiGsBQ0plHAvaZulzOPce33AQrru6gMYzbiZADOTr0WDW0YRCLeNZqCUcxYjwm5GlC-FRL9fPQF-ApWH98MLlAR6MLOAg9UDfROdu3QpqJXsR42vkt9UarXmO5qZ09Ib9StmuxIJKbLxSBVBBAB5z_FseIPZbeJqKxudyrrPz3zLa1NF8FQB1g-3VgLhrHNbcuzJCi3LyDSxrwc_5b9PCD8SqK10xvPi0G5FkgMZzJsMtdaw-Bcz2vUayfE5gdmn-RU_mb6YZIcnsVoichCGnBRgl91ddEEIS_Yv_zTPre8mkracvekd4aHOwa86bmrq57hcEqcSF12KYc-02b3yNCWR3PeZ3kdKOuwtSHqyDDqPY1Iv_rp7RP4bfeiXZ2tiF_pZBVIpIGIaR2t1Ge7FcJ78SKOxHdq6uZaJ4fUKEjbvUcvOawdDQQH45GDwrBhsatoYFk0TIyfwoCXvNWFjV2I6jlG0-7Cu77c1xet9NRsR1GApVKk-beNzH9kIEy4cHitMFIwEvfbBz4Wy6mVrBovt3cWZ31cHcH_Dac-0VSrY_zvIceDAbbbTilwOTS5hLK-SRu08K_hwdAoIBAQC_rLeRwy9OKClHN3q88KaOyXhRGzD0FSN8IB9sd8NTM4hQAVXoL27NZmY1X_1pEpsbB3uJ1GlApT8zmG02F5BxEFREUSSZHOB7WOYGYGmjdb8_jclJJnIpLBVAeWLDRMUw-lAtefZzd1oGXl3bqAssAC-pw8anXYO4Y8Lx7fawOUVE83feyYSqHkNC9JRZyxa4uTezv0H6N5fv0vndCarBIpqP2RNIgbnCxHGGN_qLKrocmkTNuRgS0dLsQEXDhXHwPJixVK0_U2FVTtYvq2sTbHh6VdZdgpGZVCE3pO5-tfA_EZm-VkjUjFBWa733RlI056BCMWtRTnMMc_AR9JHtAoIBAQD9i2lcMmDy2ewX9zhwEPqkkrjZsMb7agXuDiynd2FbQbHMXf7ZmDoPLdWmFTJOnyQBVz7vj0cYbPgRskokh_zjO23G9_jNVD-TUf8CHUc8P4rUCTXYrjUgiDob3A8j2FnP7UkycFP2oBZwx5nQRLiTMCuIwICU_9GoxA6fP-ozncT2IkR4kFRsmTlB1gNT2rT8w-uXFy1Kw7DTfNJ2BaUxZegWLCXBImLagAQ6NTzwO-ad9mNI19NgOG5LXt7k_ASKRYIAlJG_Pd8tJFIWtutAa5s76ckxXmPrx_27O_8kjenwYBqs410qVA0T-FxtzIDDVmZzZiedIj6r3AnCCy_vAoIBACVrGGMSUZfWa0iF4iYkbL-PQTfEMShVQBopQzkvoCgL9V55BEZRjDX4-ZcN6DCs-y6ppUpg9zMxj0cJSLi4nPAVmf3EQXrKkmBCGJXCGJ1ajT1g9pybLsou2u89QzSPpldLZn-aQayE7kQ0A-qvwY6TcrqXh56AuQg0IYiNRg1dANLXHN2XRZjYU5UsnhFxfjwNnHwwqDaqx7R_4VtvWbBeiwHuBL-w44zMQMVNBFJKww52Z1wp-DMkVyLb4UKsOHQHwF9OAoxMQMIgo9umgT7HfDWVsEWNCdN_AwUT46NE7WPCkdsAiTNFOSHF4s31InNFjJmfgC3dPMR_laxLrHkCggEAQOfUc_atnN2us7_k4cNm9oRc79VySuAsXnGgespz0NGze1bAq-FtU39vRxH8C-ZvaCx2fwsVryoOIzGaC1oIof8PrjTCq4WatNLzzg3Cs6ismHU3JJ4MGaTmA0XFVm7HJxRefZwF0ofO1tYyCP_epEdbjamCASKwAF0YRSougUItLrk9AAQqqJZc8v6fYXMGjAJ3p_kV7iLEfI9irTXqwQeOb0aj75om5jqJWoFQdUtQUTXG2tntUZ-AlKjXtJTTcLY0foOKAAQNwteecBQpixdA6QiDKbE5MR3EVY8ZVLlDnvZrPcECk5hqtKcKukz01Yyjs3z-PRqMIqC0mskUXwKCAQAjDTQDgUAe1gEvRWELFWAg4d1Yvw19M5CTw1-3DbKJVXGZ7DvXs1xWVJGDUmwowWVR1QQr9AUyXOdSBh5zFWFu_bIhbhJZGGMmrYFFqjsXBv-MA8bKZIxWvEeLYlxJreQAGQlqZ48Jm5KnRs7w2aM4MgnEf_tcdLVIpPWxT-OkFv4etg798qmHsotNeMBR1felSW3kfwfMdcgWml4rxtAKkPPQscYksEpl8-lIEkDRjldfAL-kEchAfCQZwk3iSYOeTIV8ZXTE2f7-aMO0HpXySuDyvdYefAJGD4Wwj6gs-F3xXL-VAiuJmk7rxyMgRaiGkJKKBAdzns2E25goch32";

        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

       FutureTask<Boolean> futureTask = new FutureTask<>(new Server());
       futureTask.run();
       if(futureTask.get() == false){}
    }

    @Test
    public void testServer2() throws ExecutionException, InterruptedException {
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");

        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

        FutureTask<Boolean> futureTask = new FutureTask<>(new Server());
        futureTask.run();
        if(futureTask.get() == false){}
    }

}
