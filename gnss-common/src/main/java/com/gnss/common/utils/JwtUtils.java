package com.gnss.common.utils;

import com.gnss.common.constants.RoleTypeEnum;
import com.gnss.common.security.AuthTokenDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * <p>Description: JWT工具</p>
 * <p>Company: www.gps-pro.cn</p>
 *
 * @author huangguangbin
 * @version 1.0.1
 * @date 2017/6/19
 */
public class JwtUtils {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;
    private static final String SECRET_KEY = "mySecret";
    private static final String APP_ID_FIELD = "appId";
    private static final String ORGANIZATION_ID_FIELD = "account";
    private static final String ROLE_ID_FIELD = "roleId";
    private static final String ROLE_TYPE_FIELD = "roleType";
    private static final String LANGUAGE_FIELD = "language";

    /**
     * 生成token
     *
     * @param authTokenDetails
     * @return
     */
    public static String generateToken(AuthTokenDetails authTokenDetails) {
        Long roleId = authTokenDetails.getRoleId();
        String token =
                Jwts.builder().setSubject(authTokenDetails.getUserId().toString())
                        .claim(APP_ID_FIELD, authTokenDetails.getAppId())
                        .claim(ORGANIZATION_ID_FIELD, authTokenDetails.getOrganizationId().toString())
                        .claim(ROLE_ID_FIELD, roleId == null ? null : roleId.toString())
                        .claim(ROLE_TYPE_FIELD, authTokenDetails.getRoleType())
                        .claim(LANGUAGE_FIELD, authTokenDetails.getLanguage())
                        .setExpiration(authTokenDetails.getExpirationDate())
                        .signWith(SIGNATURE_ALGORITHM, SECRET_KEY)
                        .compact();
        return token;
    }

    /**
     * 解析token
     *
     * @param token
     * @return
     * @throws Exception
     */
    public static AuthTokenDetails parseToken(String token) throws Exception {
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        String userId = claims.getSubject();
        String appId = (String) claims.get(APP_ID_FIELD);
        String organizationId = (String) claims.get(ORGANIZATION_ID_FIELD);
        String roleId = (String) claims.get(ROLE_ID_FIELD);
        String roleType = (String) claims.get(ROLE_TYPE_FIELD);
        String language = (String) claims.get(LANGUAGE_FIELD);
        Date expirationDate = claims.getExpiration();

        AuthTokenDetails authTokenDetails = new AuthTokenDetails();
        authTokenDetails.setUserId(Long.valueOf(userId));
        authTokenDetails.setAppId(appId);
        authTokenDetails.setOrganizationId(Long.valueOf(organizationId));
        authTokenDetails.setRoleId(roleId == null ? null : Long.valueOf(roleId));
        authTokenDetails.setRoleType(RoleTypeEnum.valueOf(roleType));
        authTokenDetails.setExpirationDate(expirationDate);
        authTokenDetails.setLanguage(language);
        return authTokenDetails;
    }

}
