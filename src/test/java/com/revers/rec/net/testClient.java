package com.revers.rec.net;

import com.revers.rec.Kademlia.Bucket.RoutingTable;
import com.revers.rec.RecApplication;
import com.revers.rec.config.AccountConfig;
import com.revers.rec.net.Client.ClientOperation;
import com.revers.rec.util.ResultUtil;
import com.revers.rec.util.cypher.DigestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.revers.rec.util.cypher.RsaUtil.createKeys;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecApplication.class)
public class testClient {
    @Autowired
    RoutingTable routingTable;

    @Test
    public void testPing() throws ExecutionException, InterruptedException {
        ResultUtil ping = ClientOperation.ping("127.0.0.1", 30000);
        System.out.println(ping.getFlag());
        System.out.println(ping.getMsg());
        System.out.println(ping.getData());
    }

    @Test
    public void testHandShake() throws NoSuchAlgorithmException, ExecutionException, InterruptedException {

        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        System.out.println(publicKey);
        System.out.println(privateKey);
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

        ResultUtil handshake = ClientOperation.handShake("127.0.0.1",30000);
        System.out.println(handshake.getFlag());
        System.out.println(handshake.getMsg());
        System.out.println(handshake.getData());
    }
    @Test
    public void testCommunication() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, ExecutionException, InvalidKeyException, InterruptedException {
        Map<String, String> keyMap = createKeys(4096);
        String publicKey = keyMap.get("publicKey");
        String privateKey = keyMap.get("privateKey");
        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);
        ResultUtil handshake = ClientOperation.handShake("127.0.0.1",30000);
        String destPublicKey ="MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAzk94fzt5Iyhm2XlNeqzqaQM27_yyBQgpC2TaP42YLbfKJlOf19oipY17JjWCLdH01iFForNSgGxEqwjt7IOQox7GGJSjGJuVaZZAe6NHsy2mivtmZ7-o90dAozUWq0h3_q3PVlq2-jTgvW_yZHgkOimG2iI7MyWTYbQGvS-q9ClMbJIyGTaCKh_hokUvDqYz-3uVDVAhw6MgiA1Gv_f7MJEQs7tDJoonlDSaep6apOy9bjDWtA68ERcGcMgGpq2dMFSR3vWT8AsPFdql3A2e6mcZ5zTi-39gvRcw3DLmfV3T_kWRr2cTZ7iiQixyKkZQcEFMahpT9LYTYemZlWxt3Z02PavoycruZWJ25Q6UL6uvbSxQZy-mZ9YfTUrbUeGztiy_Nh0riRJTGjqSHI7WZsT6ySWFHmfkKCw6etKlj6ulczkAH2J1Oy7zctP6dxstUZh9W2KFaAJLTgMb8Stre84dDseRgTDup8QJSFL7m0k9ZBMqkONG6nl9UhcAOVQjYjGpk-bOPnTaeV5PwE2xMJGWrdnPnaGuWRGs78a0U0MxsjsQ3tnz2qf_c2c9iRhcGJCNdaqkoIieAC0IW4ddRxQhZmgjx1_6TdYt0SKY5lDqKqt3VnPj2945ZcfSX4VLzk7Slr0ZuhQVbIXxAblONg0pibS8O43OTY4V__pCZL0CAwEAAQ";
        ResultUtil communicate = ClientOperation.communicate(destPublicKey,"test");
        System.out.println(communicate.getFlag());
        System.out.println(communicate.getMsg());
        System.out.println(communicate.getData());
    }

    @Test
    public void testCommunicationa3() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, ExecutionException, InvalidKeyException, InterruptedException {

        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAtKghahT7TCel4lNT7Af5HiwC2ES1APqZzjheHSaGZ58UellGRopNsQ1qIKxduA41uT5AgCZejE1Z2l4s7zzP4u07WOeqY3piS09o_7M-eG7qkDFxE6LlYgz_bPvKXlHxQC9lxkEYclBJFzetQlrI9LkYER0qoxgF3RU7owAmF4UB6XjWqGbuPndrm9i7HkdhL0ATLG82R5EnD_WK_spo0AFsHobBJ5mK4jjUG4VrReRFgipd4Scxde6SMuLRKCpm5G4voikqU86J2GssU7ip-pT6Dv6fGigYhxDSapvs-7Bx7b4zBcCfPAHeieUkPDjNGS7Dst1L5g9jf5UXSpTwI4ngBrPi8x7-xEtPHn8BwlC5aQh420c5rHEX3_9kDbj2_zfUpStewljrKuAQsM791U2x885H2j6pzMmCGlr3nynSBqk89XxeX23HQlpDl_4r6zAzODkwgaEte_pjn7Qy8gLfQSTzHHiYWPAircIgkyXHSrUdfgzbOrVsXL7uULUUK_coaqTkYpRpQVyBG5o35p0AQr4CZhKoPewDCb0kGtj2unl_7GvPrhn3bNqMcofEb00CHvjlQ1gASupmjwyHUWUHAENgi7wUC4fC0y8Ez6sJgiYCEEb3uP2icJawVNWJdLdX9StY-2q2M9I5IhHavAtMS_CheEX7xNEiAof-4c8CAwEAAQ";
        String privateKey = "MIIJQgIBADANBgkqhkiG9w0BAQEFAASCCSwwggkoAgEAAoICAQC0qCFqFPtMJ6XiU1PsB_keLALYRLUA-pnOOF4dJoZnnxR6WUZGik2xDWogrF24DjW5PkCAJl6MTVnaXizvPM_i7TtY56pjemJLT2j_sz54buqQMXETouViDP9s-8peUfFAL2XGQRhyUEkXN61CWsj0uRgRHSqjGAXdFTujACYXhQHpeNaoZu4-d2ub2LseR2EvQBMsbzZHkScP9Yr-ymjQAWwehsEnmYriONQbhWtF5EWCKl3hJzF17pIy4tEoKmbkbi-iKSpTzonYayxTuKn6lPoO_p8aKBiHENJqm-z7sHHtvjMFwJ88Ad6J5SQ8OM0ZLsOy3UvmD2N_lRdKlPAjieAGs-LzHv7ES08efwHCULlpCHjbRzmscRff_2QNuPb_N9SlK17CWOsq4BCwzv3VTbHzzkfaPqnMyYIaWvefKdIGqTz1fF5fbcdCWkOX_ivrMDM4OTCBoS17-mOftDLyAt9BJPMceJhY8CKtwiCTJcdKtR1-DNs6tWxcvu5QtRQr9yhqpORilGlBXIEbmjfmnQBCvgJmEqg97AMJvSQa2Pa6eX_sa8-uGfds2oxyh8RvTQIe-OVDWABK6maPDIdRZQcAQ2CLvBQLh8LTLwTPqwmCJgIQRve4_aJwlrBU1Yl0t1f1K1j7arYz0jkiEdq8C0xL8KF4RfvE0SICh_7hzwIDAQABAoICAEd3zgQ_-axGcRcInQYen1xiz9tLzwlsmkUdQooor_o1Ui4WvDjyxMp6wpXbapYCNRmr-WtZAoVhol7Da-ZW-ezNqfFBcMQ66cDeUOz7NYcCZhQWIyRACJvSBYhRUd0fcJ_SQGJ-ZRPwFAI12fqKnL5HrTA0CCaOPd-Y40dYKzmZukhpMA5d7flTWARG6LuRRG6jXBJzipckYr4rBspGO9GyBlpLOAWY9fsJvUuH7Aixxhe49_u2_BeqAjdUSKcpoxvSRVDTFTYjWp_9wgL65m_F2OjurPV1kEJ6kqtvNQDa7C8_08J-UeA04xtNRZj3ZZgFSpErf7erNdMGobi1FAxoyTTMunmi-ZXckRBjRZ16Nr-mz0a_f3YwXcPNR23kRhTiRcvIX6E9gWTa5dTTI1SuSGrOmcFyeuSRmV0MhdQfhbVoCf-xdgO81nHEZgtzLqw5fKxqDX8JEcGluqi4O5Kh9NDhbZsaC5qdxcbprh-x_Vw1P-EpMtyEJgaUx69haIO05_fae69Z3DwCYgJlR8Vpuk-d6nWtuJdG4hrxkUTB8aXY07lfwCGwxWpZp53VRpu0mfNBWLSDZoEiZCgxh_JYtc9hfICMe-7bTyy-HTjFNtjd7slWpp68mgDJS_UM0b7n7RHq08wTZuJP98708SULhLenYTjlrKpb0iBoqKQFAoIBAQDhmYexuIULcqvA3s57cEqoZLUnMpDqSGXsypncnFTEXiwYd_3AhDnCjfr2tIvDaiaM9kZuSTPeg2ytUr05RaUnUp7ImAHvogZjE6KwHjWEtM-X84gK7acdYSCwJ_ltEW4RLnLgUTlIE6VB88vzd32YaIemLxMm0M_T7AIdrEP6H43YcI0BD9U4SSCAzgcT_yMtFd2JL904LBjn2RKjtYKsgQh5DVC8O6HXMmPemNSTVKWnBw1xrVRLqezreGj_u7Z2rTq4vyZoWTEH8E1JVYJbiKseXVTPXhfshwTbVbMRP6fqOufvm9dnQ8Xj_LAnpVT-f93B0R7eLHtiHKHpxrrzAoIBAQDNADYwF53Ewoc8yOxCAwWLbV_U0vc7ByZMpu3IQybNbUOBcbb9995HMoXO4XtemzuvLlWCqrmMd182Ld3NPRS9hifIUg0ky1g1erBUI8vjRQXC9wptbSsDNJXJ_ZOi_c1fUCPmBi2VjX-BziESInKUYpiYBF5Wh-FVw186bAILrpg2DcmPwECzvLy-br-2jHgLMqpId1Jrvp8sbTWXMibtB5i6tNntYo4BzBlKXy4m3nnl_OL_qGaaM-5XRUqXkGgMZp3txGnxfjQg9j3xKKaF_Lqk4vDw2WCTvRhQEeQjMygTTbPom2CUOxfQvbeN4A0diZYRdaDmU1UZmwRCFXy1AoIBADEh38Sb3BXnLnK905hcl1XfQPi9cYQkqlYMtb-vKGLv44icsV35Lh8qDssl_P5dIvI2PJaOSMagNs30pBNSEKrkuoITYPCQh_WR9C-JFLzHKCL-vdUrdubWHWlf-1l-n0sMo6Yo6mHBLY-UigmHL4o-0bQmsnQUq_6Ketd9ifxyXNETihH-IcC1ZGfI9w-q1D1gcrQLAZHXQMFVrFKNGF17aJ38gWXNUuzsI4X6IOnJWJ5s0ce9KQulgVyzB6MSfaWRCqXrs3CsZBpHgn2DKvXGaQYe63aSFoHD0hH2sx4BkA9aLuMnPJ1XzERf5vlrb3xR2rj4U1GtvF1cwQKJ7nMCggEANfogSnltaSlvQmoFCzyQcS3TVc5Wmow_YV9yrJBB30UtyR-P9z8Un9qFsPtd6IMnK_EfQtCBGqMzxqg9BpVWqLByOSShoVaJW0t6sizaunSqF7PEYxujf6yQ5CaIgRoiwT3wtdhamE4xjJQh0QyQ7tEnLJ-51z7f7h0LLe7SexD1swXeVKNiZfEEIYA3qnFCCnDEtV_1q7j9YE1BFxZ1eWPOqxqDZyXUDlUXM6XsGP_rSMtVWcuVeHTEBOaRxWaYfutmGfzliFiakTNFDAKQFid9XX0Dw-b4ru3TQQH09--4VZzYCh8fRuhoVOYfNpQP3_8iLGa0fvDiL2PJszzFIQKCAQEAmXJvtJ5pGZGCwISjw0hz5BJ1tivEYx0Z5Twvm8Qvzv28cPgOtUr2-P9ptFfwfzcgFBHgFzSG9b1tkCm41-8YoFvnpegeEy_NTt4_z1R9r24LjGOZ-Cn47j249le80exXH0B1tzAQQDjPEUOsZqYO5WmtNnaJpCd_OO9GrThQSGqAt5t6_rTw_GhR_58opB3QMkpJkWWlFTnUO3WodGP188HGASGNBAR-z_hAT4t_MJHeWgtYR7JoeTyCNfbU3Ly4LGdcbJk1PFm6kWYcXavpDQFZucr_Uv0PjXp7UqiMapBKdOVkLolZTlKlA0faRQI6qFOuZfHX9ZVnrYaBy_K6hQ";
        AccountConfig.setId(DigestUtil.Sha1AndSha256(publicKey));
        AccountConfig.setPublicKey(publicKey);
        AccountConfig.setPrivateKey(privateKey);
        AccountConfig.setIpv6("127.0.0.2");
        AccountConfig.setIpv6Port(30000);

        ClientOperation.handShake("127.0.0.1",30000);
        String destPublicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAzk94fzt5Iyhm2XlNeqzqaQM27_yyBQgpC2TaP42YLbfKJlOf19oipY17JjWCLdH01iFForNSgGxEqwjt7IOQox7GGJSjGJuVaZZAe6NHsy2mivtmZ7-o90dAozUWq0h3_q3PVlq2-jTgvW_yZHgkOimG2iI7MyWTYbQGvS-q9ClMbJIyGTaCKh_hokUvDqYz-3uVDVAhw6MgiA1Gv_f7MJEQs7tDJoonlDSaep6apOy9bjDWtA68ERcGcMgGpq2dMFSR3vWT8AsPFdql3A2e6mcZ5zTi-39gvRcw3DLmfV3T_kWRr2cTZ7iiQixyKkZQcEFMahpT9LYTYemZlWxt3Z02PavoycruZWJ25Q6UL6uvbSxQZy-mZ9YfTUrbUeGztiy_Nh0riRJTGjqSHI7WZsT6ySWFHmfkKCw6etKlj6ulczkAH2J1Oy7zctP6dxstUZh9W2KFaAJLTgMb8Stre84dDseRgTDup8QJSFL7m0k9ZBMqkONG6nl9UhcAOVQjYjGpk-bOPnTaeV5PwE2xMJGWrdnPnaGuWRGs78a0U0MxsjsQ3tnz2qf_c2c9iRhcGJCNdaqkoIieAC0IW4ddRxQhZmgjx1_6TdYt0SKY5lDqKqt3VnPj2945ZcfSX4VLzk7Slr0ZuhQVbIXxAblONg0pibS8O43OTY4V__pCZL0CAwEAAQ";
        ResultUtil communicate = ClientOperation.communicate(destPublicKey,"testa2");
        System.out.println(communicate.getFlag());
        System.out.println(communicate.getMsg());
        System.out.println(communicate.getData());
    }

}
